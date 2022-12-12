package com.epam.esm.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;

import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2022-07-25T17:04:37+0300",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public TagDto toDto(Tag entity) {
        if (entity == null) {
            return null;
        }

        TagDto tagDto = new TagDto();

        tagDto.setId(entity.getId());
        tagDto.setName(entity.getName());

        return tagDto;
    }

    @Override
    public Tag toEntity(TagDto dto) {
        if (dto == null) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId(dto.getId());
        tag.setName(dto.getName());

        return tag;
    }
}
