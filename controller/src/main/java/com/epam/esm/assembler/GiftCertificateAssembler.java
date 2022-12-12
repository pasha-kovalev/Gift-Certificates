package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.epam.esm.util.PaginationUtil.PAGE_NUMBER_DEFAULT;
import static com.epam.esm.util.PaginationUtil.PAGE_SIZE_DEFAULT;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements SimpleRepresentationModelAssembler<GiftCertificateDto> {
    public static final String UNTIE_TAG_REL = "untie";
    private static final Class<GiftCertificateController> CONTROLLER_CLASS = GiftCertificateController.class;
    private static final String ALL_CERTIFICATES_REL = "certificates";

    @Override
    public void addLinks(EntityModel<GiftCertificateDto> resource) {
        GiftCertificateDto giftCertificateDto = Objects.requireNonNull(resource.getContent());
        Long id = giftCertificateDto.getId();
        resource.add(
                linkTo(methodOn(CONTROLLER_CLASS).one(id))
                        .withSelfRel()
                        .andAffordance(afford(methodOn(CONTROLLER_CLASS).update(id, null)))
                        .andAffordance(afford(methodOn(CONTROLLER_CLASS).delete(id))),
                linkTo(methodOn(CONTROLLER_CLASS).all(null, null, PAGE_SIZE_DEFAULT, PAGE_NUMBER_DEFAULT, null))
                        .withRel(ALL_CERTIFICATES_REL));
        giftCertificateDto.getTags().forEach(t -> t.add(linkTo(methodOn(TagController.class)
                .one(Objects.requireNonNull(t).getId()))
                .withSelfRel()));
        giftCertificateDto
                .getTags()
                .forEach(t -> t.add(linkTo(methodOn(CONTROLLER_CLASS).untieTag(id, t.getId())).withRel(UNTIE_TAG_REL)));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<GiftCertificateDto>> resources) {
        resources.forEach(RepresentationModel::removeLinks);
        resources.forEach(r -> linkTo(methodOn(CONTROLLER_CLASS).one(Objects.requireNonNull(r.getContent()).getId()))
                .withSelfRel());
    }
}
