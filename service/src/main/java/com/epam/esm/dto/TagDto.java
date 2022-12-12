package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * DTO that transfers {@link com.epam.esm.domain.Tag} content
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Relation(itemRelation = "tag", collectionRelation = "tags")
public class TagDto extends RepresentationModel<TagDto> implements BaseDto {
    /**
     * Certificate identifier in database
     */
    @Null
    private Long id;

    /**
     * Tag name
     */
    @NotBlank(message = "validator.tag.name.notEmpty")
    @Size(min = 2, max = 45, message = "validator.tag.name.size")
    private String name;
}
