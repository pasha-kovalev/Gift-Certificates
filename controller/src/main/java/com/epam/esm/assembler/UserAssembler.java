package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
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
public class UserAssembler implements SimpleRepresentationModelAssembler<UserDto> {
    private static final Class<UserController> CONTROLLER_CLASS = UserController.class;
    private static final String ALL_TAGS_REL = "users";

    @Override
    public void addLinks(EntityModel<UserDto> resource) {
        resource.add(linkTo(methodOn(CONTROLLER_CLASS).one(Objects.requireNonNull(resource.getContent()).getId()))
                        .withSelfRel(),
                linkTo(methodOn(CONTROLLER_CLASS).all(PAGE_SIZE_DEFAULT, PAGE_NUMBER_DEFAULT, null))
                        .withRel(ALL_TAGS_REL));

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDto>> resources) {
        resources.forEach(RepresentationModel::removeLinks);
        resources.forEach(r -> r.add(linkTo(methodOn(CONTROLLER_CLASS).one(Objects.requireNonNull(r.getContent()).getId()))
                .withSelfRel()));
    }
}
