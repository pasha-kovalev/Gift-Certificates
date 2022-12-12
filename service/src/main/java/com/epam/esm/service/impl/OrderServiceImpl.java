package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.GiftCertificateOrders;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.CertificateOrdersDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.exception.ResourceBundleServiceErrorMessageKey;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.GIFT_CERTIFICATE_NOT_PRESENT;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateService certificateService;
    private final UserService userService;
    private final GiftCertificateRepository certificateRepository;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            GiftCertificateService certificateService,
            UserService userService,
            GiftCertificateRepository certificateRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.certificateService = certificateService;
        this.userService = userService;
        this.certificateRepository = certificateRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderDto findById(long id) {
        return orderRepository
                .findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(
                        () ->
                                new EntityNotPresentException(
                                        ResourceBundleServiceErrorMessageKey.ORDER_NOT_PRESENT, id));
    }

    @Override
    public Page<OrderDto> findByUserId(long userId, Pageable pageable) {
        userService.findById(userId);
        return orderRepository.findByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        addUserToOrder(orderDto);
        BigDecimal totalCost = countTotalCost(orderDto.getCertificateOrdersSet());
        orderDto.setTotal(totalCost);
        Order newOrder = orderMapper.toEntity(orderDto);
        addCertificateQuantitySet(orderDto, newOrder);
        return orderMapper.toDto(orderRepository.save(newOrder));
    }

    private void addUserToOrder(OrderDto orderDto) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.findByName(principal).getId();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        orderDto.setUser(userDto);
    }

    private void addCertificateQuantitySet(OrderDto orderDto, Order newOrder) {
        Set<GiftCertificateOrders> giftCertificateOrdersSet = new HashSet<>(newOrder.getCertificates());
        giftCertificateOrdersSet.addAll(
                orderDto.getCertificateOrdersSet().stream()
                        .map(e -> processGiftCertificateOrders(newOrder, e))
                        .toList()
        );
        newOrder.setCertificates(giftCertificateOrdersSet);
    }

    private GiftCertificateOrders processGiftCertificateOrders(
            Order newOrder, CertificateOrdersDto certificateOrdersDto) {
        GiftCertificate giftCertificate =
                certificateRepository.findById(certificateOrdersDto.getCertificateId())
                        .orElseThrow(
                                () -> new EntityNotPresentException(
                                        GIFT_CERTIFICATE_NOT_PRESENT, certificateOrdersDto.getCertificateId()
                                )
                        );
        GiftCertificateOrders giftCertificateOrders = new GiftCertificateOrders();
        giftCertificateOrders.setOrder(newOrder);
        giftCertificateOrders.setGiftCertificate(giftCertificate);
        giftCertificateOrders.setQuantity(certificateOrdersDto.getQuantity());
        giftCertificateOrders.setPrice(giftCertificate.getPrice());
        giftCertificateOrders.setDuration(giftCertificate.getDuration());

        return giftCertificateOrders;
    }

    private BigDecimal countTotalCost(Set<CertificateOrdersDto> certificateQuantitySet) {
        BigDecimal result = BigDecimal.ZERO;
        for (CertificateOrdersDto dto : certificateQuantitySet) {
            BigDecimal dtoPrice = certificateService.findById(dto.getCertificateId()).getPrice();
            result = result.add(calculateCost(dto.getQuantity(), dtoPrice));
        }
        return result.setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal calculateCost(int itemQuantity, BigDecimal itemPrice) {
        return itemPrice.multiply(BigDecimal.valueOf(itemQuantity));
    }
}
