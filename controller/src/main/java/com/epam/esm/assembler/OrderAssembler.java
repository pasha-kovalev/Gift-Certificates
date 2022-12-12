package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.epam.esm.util.PaginationUtil.PAGE_NUMBER_DEFAULT;
import static com.epam.esm.util.PaginationUtil.PAGE_SIZE_DEFAULT;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements SimpleRepresentationModelAssembler<OrderDto> {
    private static final Class<OrderController> CONTROLLER_CLASS = OrderController.class;
    private static final String ALL_TAGS_REL = "orders";

    @Override
    public void addLinks(EntityModel<OrderDto> resource) {
        OrderDto orderDto = Objects.requireNonNull(resource.getContent());
        resource.add(linkTo(methodOn(CONTROLLER_CLASS).one(orderDto.getId()))
                        .withSelfRel(),
                linkTo(methodOn(CONTROLLER_CLASS).all(PAGE_SIZE_DEFAULT, PAGE_NUMBER_DEFAULT, orderDto.getUser().getId(), null))
                        .withRel(ALL_TAGS_REL));

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<OrderDto>> resources) {
        resources.forEach(RepresentationModel::removeLinks);
        resources.forEach(r -> r.add(linkTo(methodOn(CONTROLLER_CLASS).one(Objects.requireNonNull(r.getContent()).getId()))
                .withSelfRel()));
    }
}
