package com.epam.esm.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import org.mapstruct.Mapper;

/**
 * Mapper for Tag
 */
@Mapper
public interface TagMapper {
    TagDto toDto(Tag entity);

    Tag toEntity(TagDto dto);
}
