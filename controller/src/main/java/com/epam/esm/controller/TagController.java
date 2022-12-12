package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Controller for Tag entity
 */
@Tag(name = "Tags", description = "Controller for Tag entity")
@RequestMapping("/tags")
@SecurityRequirement(name = "oauth2")
@Validated
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TagController {
    private final TagService tagService;
    private final TagAssembler tagAssembler;

    @Autowired
    public TagController(TagService tagService, TagAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @Operation(summary = "Get all Tags for page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tag",
                    content = {@Content(mediaType = "application/prs.hal-forms+json",
                            schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource",
                    content = @Content)})
    @GetMapping
    public PagedModel<EntityModel<TagDto>> all(
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_TEXT) @Positive int size,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_TEXT) @Positive int page,
            PagedResourcesAssembler<TagDto> pagedResourcesAssembler) {
        Page<TagDto> tagDtoPage = tagService.findAll(PageRequest.of(page - 1, size));
        return pagedResourcesAssembler.toModel(tagDtoPage, tagAssembler);
    }

    @Operation(summary = "Get a Tag by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Tag",
                    content = {@Content(mediaType = "application/prs.hal-forms+json",
                            schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found",
                    content = @Content)})
    @GetMapping("{id}")
    public EntityModel<TagDto> one(@Parameter(description = "id of Tag to be searched")
                                   @PathVariable @Positive Long id) {
        return tagAssembler.toModel(tagService.findById(id));
    }

    @Operation(summary = "Get a top tag that used by the richest user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Tag",
                    content = {@Content(mediaType = "application/prs.hal-forms+json",
                            schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource",
                    content = @Content)})
    @GetMapping("/top-by-top")
    public PagedModel<EntityModel<TagDto>> mostWidelyUsedByRichestUser(
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_TEXT) @Positive int size,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_TEXT) @Positive int page,
            PagedResourcesAssembler<TagDto> pagedResourcesAssembler) {
        Page<TagDto> resultPage = tagService.findMostUsedTagOfUserWithHighestCostOfAllOrders(PageRequest.of(page - 1, size));
        return pagedResourcesAssembler.toModel(resultPage, tagAssembler);
    }

    @Operation(summary = "Create a new Tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Tag created",
                    content = {@Content(mediaType = "application/prs.hal-forms+json",
                            schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Tag supplied",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource",
                    content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDto> create(@Parameter(description = "Tag to be created")
                                      @Valid @RequestBody TagDto newTag) {
        return tagAssembler.toModel(tagService.save(newTag));
    }

    @Operation(summary = "Delete a Tag by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found",
                    content = @Content)})
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@Parameter(description = "id of book to be searched")
                                       @PathVariable @Positive Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
