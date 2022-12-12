package com.epam.esm.mapper;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;

import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2022-07-25T17:04:37+0300",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toDto(Order entity) {
        if (entity == null) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setCertificateOrdersSet(OrderMapper.convertCertificatesSetToDtoSet(entity.getCertificates()));
        orderDto.setId(entity.getId());
        orderDto.setUser(userToUserDto(entity.getUser()));
        orderDto.setCreateDate(entity.getCreateDate());
        orderDto.setTotal(entity.getTotal());

        return orderDto;
    }

    @Override
    public Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();

        order.setId(dto.getId());
        order.setUser(userDtoToUser(dto.getUser()));
        order.setCreateDate(dto.getCreateDate());
        order.setTotal(dto.getTotal());

        return order;
    }

    protected UserDto userToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());

        return userDto;
    }

    protected User userDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        return user;
    }
}
