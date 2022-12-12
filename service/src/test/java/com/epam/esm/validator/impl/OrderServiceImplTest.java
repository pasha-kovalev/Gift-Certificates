package com.epam.esm.validator.impl;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.GiftCertificateOrders;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.dto.CertificateOrdersDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.UserService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private final UserDto userDto = new UserDto(1L, "name", "pass", Role.USER);
    private final User user = new User(1L, "name", "pass", Role.USER);
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final GiftCertificate certificate = GiftCertificate.builder().id(1L).build();
    private final GiftCertificateOrders giftCertificateOrders =
            new GiftCertificateOrders(null, null, certificate, 1, BigDecimal.ZERO, 1);
    private final Order order =
            new Order(
                    1L, user, dateTime, BigDecimal.TEN, Set.of(giftCertificateOrders));
    private final CertificateOrdersDto certificateOrdersDto = new CertificateOrdersDto(1L, 1, BigDecimal.ZERO, 1L);
    private final OrderDto orderDto =
            new OrderDto(
                    1L, userDto, dateTime, BigDecimal.TEN, Set.of(certificateOrdersDto));
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserService userService;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private GiftCertificateServiceImpl certificateService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testFindAllCertificatesShouldReturnListOfAllCertificates() {
        List<Order> orders = List.of(order);
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> page = new PageImpl<>(orders, pageable, 5);

        Page<OrderDto> expectedPage = new PageImpl<>(List.of(orderDto), page.getPageable(), 5);

        when(orderRepository.findAll(pageable)).thenReturn(page);
        when(orderMapper.toDto(any())).thenReturn(orderDto);

        Page<OrderDto> actualPage = orderService.findAll(pageable);
        assertEquals(expectedPage, actualPage);
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void testFindByIdShouldReturnFoundCertificateWithGivenId() {
        final long ID_ONE = 1;
        when(orderRepository.findById(ID_ONE)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any())).thenReturn(orderDto);

        OrderDto actual = orderService.findById(ID_ONE);
        assertEquals(orderDto, actual);
        verify(orderRepository).findById(Mockito.anyLong());
    }

    @Test
    void testFindByIdShouldThrowExceptionIfCertificateNotFound() {
        when(orderRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(EntityNotPresentException.class, () -> orderService.findById(0));
    }

    @Test
    void testSaveCertificateShouldReturnCreatedCertificate() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn("");

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setDuration(10L);
        giftCertificate.setPrice(BigDecimal.TEN);
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setPrice(BigDecimal.TEN);

        when(orderRepository.save(any())).thenReturn(order);
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
        when(certificateService.findById(anyLong())).thenReturn(giftCertificateDto);
        when(userService.findByName(anyString())).thenReturn(userDto);
        when(orderMapper.toDto(any())).thenReturn(orderDto);
        when(orderMapper.toEntity(any())).thenReturn(order);

        OrderDto actual = orderService.save(orderDto);
        orderDto.setUser(userDto);
        orderDto.setTotal(new BigDecimal(orderDto.getTotal().longValue()));

        assertEquals(orderDto, actual);
        verify(orderRepository).save(Mockito.any());
    }
}
