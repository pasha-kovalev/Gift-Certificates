package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static com.epam.esm.util.PaginationUtil.PAGE_NUMBER_DEFAULT_TEXT;
import static com.epam.esm.util.PaginationUtil.PAGE_SIZE_DEFAULT_TEXT;

/**
 * Controller for Gift Certificate entity
 */
@RestController
@SecurityRequirement(name = "oauth2")
@Validated
@RequestMapping("/gift-certificates")
@CrossOrigin(origins = "http://localhost:3000")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateAssembler giftCertificateAssembler;

    @Autowired
    public GiftCertificateController(
            GiftCertificateService giftCertificateService,
            GiftCertificateAssembler giftCertificateAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateAssembler = giftCertificateAssembler;
    }

    /**
     * Retrieves all Gift Certificates for page
     *
     * @param search the search query param
     * @param sort   the sort query param
     * @param size   the page size
     * @param page   the page number
     * @return the paged entity model of found Gift Certificates
     */
    @GetMapping
    public PagedModel<EntityModel<GiftCertificateDto>> all(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_TEXT) @Positive int size,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_TEXT) @Positive int page,
            PagedResourcesAssembler<GiftCertificateDto> pagedResourcesAssembler) {
        Page<GiftCertificateDto> resultPage = giftCertificateService.findAllBy(search, sort,
                PageRequest.of(page - 1, size));
        return pagedResourcesAssembler.toModel(resultPage, giftCertificateAssembler);
    }

    /**
     * Retrieves existing Gift Certificate by its id
     *
     * @param id the Gift Certificate id
     * @return found Gift Certificate
     */
    @GetMapping("{id}")
    public EntityModel<GiftCertificateDto> one(@PathVariable @Positive Long id) {
        return giftCertificateAssembler.toModel(giftCertificateService.findById(id));
    }

    /**
     * Creates a new Gift Certificate
     *
     * @param certificateDto details of new Certificate
     * @return created Gift Certificate
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificateDto) {
        return giftCertificateAssembler.toModel(
                giftCertificateService.save(certificateDto));
    }

    /**
     * Updates existing Gift Certificate
     *
     * @param id             the Gift Certificate id
     * @param certificateDto the certificate dto
     * @return updated Gift Certificate
     */
    @PatchMapping("{id}")
    public EntityModel<GiftCertificateDto> update(
            @PathVariable @Positive Long id, @RequestBody @Valid GiftCertificateDto certificateDto) {
        return giftCertificateAssembler.toModel(
                giftCertificateService.update(id, certificateDto));
    }

    /**
     * Removes an existing Gift Certificate by its id
     *
     * @param id Gift Certificate id
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        giftCertificateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{certificateId}/tags/{tagId}")
    public ResponseEntity<Void> untieTag(@PathVariable Long certificateId, @PathVariable Long tagId) {
        giftCertificateService.untieTag(certificateId, tagId);
        return ResponseEntity.noContent().build();
    }
}
