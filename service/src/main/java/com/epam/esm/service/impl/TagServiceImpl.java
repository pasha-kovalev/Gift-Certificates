package com.epam.esm.service.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.TAG_NOT_PRESENT;

@Transactional
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public org.springframework.data.domain.Page<TagDto> findAll(Pageable pageable) {
        org.springframework.data.domain.Page<Tag> page = tagRepository.findAll(pageable);
        return page.map(tagMapper::toDto);
    }

    @Override
    public TagDto findById(long id) {
        return tagRepository
                .findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new EntityNotPresentException(TAG_NOT_PRESENT, id));
    }

    @Override
    public TagDto save(TagDto tagDto) {
        Tag savedTag = tagRepository.save(tagMapper.toEntity(tagDto));
        return tagMapper.toDto(savedTag);
    }

    @Override
    public void deleteById(long id) {
        if (tagRepository.findById(id).isPresent()) {
            tagRepository.deleteById(id);
        } else {
            throw new EntityNotPresentException(TAG_NOT_PRESENT, id);
        }
    }

    @Override
    public Page<TagDto> findMostUsedTagOfUserWithHighestCostOfAllOrders(Pageable pageable) {
        return tagRepository.findMostUsedTagsOfUserWithHighestCostOfAllOrders(pageable).map(tagMapper::toDto);
    }
}
