package com.epam.esm.validator.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private final Tag tag = new Tag(1L, "tag1");
    private final TagDto tagDto = new TagDto(1L, "tag1");

    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void testFindAllTagsShouldReturnListOfAllTags() {
        List<Tag> tags = List.of(tag);
        Pageable pageable = PageRequest.of(0, 5);
        Page<Tag> page = new PageImpl<>(tags, pageable, 5);
        Page<TagDto> expectedPage = new PageImpl<>(List.of(tagDto), page.getPageable(), 5);

        when(tagRepository.findAll(pageable)).thenReturn(page);
        when(tagMapper.toDto(any())).thenReturn(tagDto);

        Page<TagDto> actualPage = tagService.findAll(pageable);

        assertEquals(expectedPage, actualPage);
        verify(tagRepository).findAll(pageable);
    }

    @Test
    void testFindByIdShouldReturnFoundTagWithGivenId() {
        final long ID_ONE = 1;
        when(tagRepository.findById(ID_ONE)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(any())).thenReturn(tagDto);

        TagDto actual = tagService.findById(ID_ONE);
        assertEquals(tagDto, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfTagNotFound() {
        when(tagRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(EntityNotPresentException.class, () -> tagService.findById(0));
    }

    @Test
    void testSaveTagShouldReturnCreatedTag() {
        when(tagRepository.save(any())).thenReturn(tag);
        when(tagMapper.toDto(any())).thenReturn(tagDto);

        TagDto actual = tagService.save(tagDto);

        assertEquals(tagDto, actual);
    }

    @Test
    void testDeleteByIdShouldDeleteTagWithGivenId() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        tagService.deleteById(1);
        tagService.deleteById(2);

        verify(tagRepository, times(2)).deleteById(anyLong());
    }
}
