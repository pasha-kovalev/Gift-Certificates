package com.epam.esm.mapper;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotValidException;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.ENTITY_INVALID_UPDATE;

/**
 * Mapper for Gift Certificate
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GiftCertificateMapper {

    GiftCertificateDto toDto(GiftCertificate entity);

    GiftCertificate toEntity(GiftCertificateDto dto);

    @InheritConfiguration
    default void update(GiftCertificateDto update, @MappingTarget GiftCertificate destination) {
        if (update == null) {
            return;
        }

        if (update.getPrice() != null || update.getId() != null || update.getName() != null
                || update.getDescription() != null || update.getCreateDate() != null
                || update.getLastUpdateDate() != null) {
            throw new EntityNotValidException(ENTITY_INVALID_UPDATE, 0L);
        }
        destination.setDuration(update.getDuration());
        destination.setTags(new HashSet<>());
        update(update.getTags(), destination.getTags());
    }

    default void update(Set<TagDto> update, @MappingTarget Set<Tag> destination) {
        TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
        update.forEach(t -> destination.add(tagMapper.toEntity(t)));
    }

    void update(TagDto update, @MappingTarget Tag destination);
}
