package com.epam.esm.mapper;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.GiftCertificate.GiftCertificateBuilder;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2022-07-25T17:04:37+0300",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
@Component
public class GiftCertificateMapperImpl implements GiftCertificateMapper {

    @Override
    public GiftCertificateDto toDto(GiftCertificate entity) {
        if (entity == null) {
            return null;
        }

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();

        giftCertificateDto.setTags(tagSetToTagDtoSet(entity.getTags()));
        giftCertificateDto.setId(entity.getId());
        giftCertificateDto.setName(entity.getName());
        giftCertificateDto.setDescription(entity.getDescription());
        giftCertificateDto.setPrice(entity.getPrice());
        if (entity.getDuration() != null) {
            giftCertificateDto.setDuration(entity.getDuration());
        }
        giftCertificateDto.setCreateDate(entity.getCreateDate());
        giftCertificateDto.setLastUpdateDate(entity.getLastUpdateDate());

        return giftCertificateDto;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDto dto) {
        if (dto == null) {
            return null;
        }

        GiftCertificateBuilder giftCertificate = GiftCertificate.builder();

        giftCertificate.id(dto.getId());
        giftCertificate.name(dto.getName());
        giftCertificate.description(dto.getDescription());
        giftCertificate.price(dto.getPrice());
        giftCertificate.duration(dto.getDuration());
        giftCertificate.createDate(dto.getCreateDate());
        giftCertificate.lastUpdateDate(dto.getLastUpdateDate());
        giftCertificate.tags(tagDtoSetToTagSet(dto.getTags()));

        return giftCertificate.build();
    }

    @Override
    public void update(TagDto update, Tag destination) {
        if (update == null) {
            return;
        }

        if (update.getId() != null) {
            destination.setId(update.getId());
        }
        if (update.getName() != null) {
            destination.setName(update.getName());
        }
    }

    protected TagDto tagToTagDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagDto tagDto = new TagDto();

        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());

        return tagDto;
    }

    protected Set<TagDto> tagSetToTagDtoSet(Set<Tag> set) {
        if (set == null) {
            return null;
        }

        Set<TagDto> set1 = new HashSet<TagDto>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (Tag tag : set) {
            set1.add(tagToTagDto(tag));
        }

        return set1;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if (tagDto == null) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());

        return tag;
    }

    protected Set<Tag> tagDtoSetToTagSet(Set<TagDto> set) {
        if (set == null) {
            return null;
        }

        Set<Tag> set1 = new HashSet<Tag>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TagDto tagDto : set) {
            set1.add(tagDtoToTag(tagDto));
        }

        return set1;
    }
}
