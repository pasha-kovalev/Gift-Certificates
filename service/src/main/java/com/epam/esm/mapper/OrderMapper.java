package com.epam.esm.mapper;

import com.epam.esm.domain.GiftCertificateOrders;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.CertificateOrdersDto;
import com.epam.esm.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for Order
 */
@Mapper
public interface OrderMapper {
    @Named("certificatesToCertificateOrdersSet")
    static Set<CertificateOrdersDto> convertCertificatesSetToDtoSet(
            Set<GiftCertificateOrders> certificates) {
        return certificates.stream()
                .map(c -> new CertificateOrdersDto(
                        c.getGiftCertificate().getId(), c.getQuantity(), c.getPrice(),
                        c.getDuration()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Mapping(
            source = "certificates",
            target = "certificateOrdersSet",
            qualifiedByName = "certificatesToCertificateOrdersSet")
    OrderDto toDto(Order entity);

    Order toEntity(OrderDto dto);
}
