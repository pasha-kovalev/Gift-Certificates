package com.epam.esm.service.impl;

import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.builder.SimpleQueryBuilder;
import com.epam.esm.builder.specification.GiftCertificateSpecificationBuilder;
import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.exception.EntityNotValidException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.builder.RequestQueryParameters.DESCRIPTION;
import static com.epam.esm.builder.RequestQueryParameters.NAME;
import static com.epam.esm.builder.RequestQueryParameters.TAG_NAME;
import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.ENTITY_INVALID_CREATE;
import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.GIFT_CERTIFICATE_NOT_PRESENT;
import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.TAG_NOT_PRESENT_FOR_CERTIFICATE;

@Transactional
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String NAME_FIELD_NAME = "name";
    private static final String CREATE_DATE_FIELD_NAME = "create_date";
    private static final String LAST_UPDATE_DATE_FIELD_NAME = "last_update_date";
    private static final Set<String> FIELDS_AVAILABLE_TO_SEARCH = Set.of(TAG_NAME, DESCRIPTION, NAME);
    private static final Set<String> FIELDS_AVAILABLE_TO_ORDER_BY =
            Set.of(CREATE_DATE_FIELD_NAME, LAST_UPDATE_DATE_FIELD_NAME, NAME_FIELD_NAME);
    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateMapper certificateMapper;
    private final TagMapper tagMapper;
    private final GiftCertificateValidator validator;

    @Autowired
    public GiftCertificateServiceImpl(
            GiftCertificateRepository certificateRepository, TagRepository tagRepository,
            GiftCertificateMapper certificateMapper, TagMapper tagMapper, GiftCertificateValidator validator) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
        this.validator = validator;
    }

    @Lookup
    public GiftCertificateSpecificationBuilder createGiftCertificateSpecificationBuilder() {
        return null;
    }

    @Override
    public Page<GiftCertificateDto> findAll(Pageable pageable) {
        return certificateRepository.findAll(pageable).map(certificateMapper::toDto);
    }

    @Override
    public Page<GiftCertificateDto> findAllBy(String search, String sort, Pageable pageable) {
        if ((search == null) && (sort == null)) {
            return findAll(pageable);
        }
        SimpleQueryBuilder<GiftCertificate> builder = QueryBuilder.createGiftCertificateBuilder();
        Specification<GiftCertificate> giftCertificateSpecification = builder
                .buildSearchSpecification(search, FIELDS_AVAILABLE_TO_SEARCH,
                        createGiftCertificateSpecificationBuilder());
        Sort ordersSort = builder.buildOrderList(sort, FIELDS_AVAILABLE_TO_ORDER_BY);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ordersSort);
        return certificateRepository.findAll(giftCertificateSpecification, pageRequest).map(certificateMapper::toDto);
    }

    @Override
    public GiftCertificateDto findById(long id) {
        return certificateRepository
                .findById(id)
                .map(certificateMapper::toDto)
                .orElseThrow(() -> new EntityNotPresentException(GIFT_CERTIFICATE_NOT_PRESENT, id));
    }

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) {
        if (!validator.isValid(giftCertificateDto)) {
            throw new EntityNotValidException(ENTITY_INVALID_CREATE, 0);
        }
        manageTags(giftCertificateDto);
        GiftCertificate newCertificate =
                certificateRepository.save(certificateMapper.toEntity(giftCertificateDto));
        return certificateMapper.toDto(newCertificate);
    }

    @Override
    public void deleteById(long id) {
        if (certificateRepository.findById(id).isPresent()) {
            certificateRepository.deleteById(id);
        } else {
            throw new EntityNotPresentException(GIFT_CERTIFICATE_NOT_PRESENT, id);
        }
    }

    @Override
    public GiftCertificateDto update(long id, GiftCertificateDto giftCertificateDto) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.findById(id);
        GiftCertificate giftCertificate =
                optionalGiftCertificate.orElseThrow(
                        () -> new EntityNotPresentException(GIFT_CERTIFICATE_NOT_PRESENT, id));
        manageTags(giftCertificateDto);
        certificateMapper.update(giftCertificateDto, giftCertificate);
        return certificateMapper.toDto(certificateRepository.save(giftCertificate));
    }

    @Override
    public void untieTag(long certificateId, long tagId) {
        GiftCertificateDto certificateDto = findById(certificateId);
        boolean isRemoved = certificateDto.getTags().removeIf(t -> t.getId() == tagId);
        if (!isRemoved) {
            throw new EntityNotPresentException(TAG_NOT_PRESENT_FOR_CERTIFICATE, tagId);
        }
        certificateRepository.save(certificateMapper.toEntity(certificateDto));
    }

    private void manageTags(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getTags().isEmpty()) {
            giftCertificateDto.setTags(null);
        } else {
            for (TagDto tag : giftCertificateDto.getTags()) {
                Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tag.setId(tagOptional.get().getId());
                } else {
                    Tag savedTag = tagRepository.save(tagMapper.toEntity(tag));
                    tag.setId(savedTag.getId());
                }
            }
        }
    }
}
