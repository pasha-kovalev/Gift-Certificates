package com.epam.esm.validator.impl;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.exception.EntityNotValidException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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
class GiftCertificateServiceImplTest {
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final GiftCertificate certificate =
            GiftCertificate.builder()
                    .id(1L)
                    .description("desc")
                    .duration(30L)
                    .price(BigDecimal.TEN)
                    .name("name")
                    .createDate(dateTime)
                    .lastUpdateDate(dateTime)
                    .tags(new HashSet<>())
                    .build();
    private final GiftCertificateDto certificateDto =
            new GiftCertificateDto(
                    1L, "name", "desc", BigDecimal.TEN, 30L, dateTime, dateTime, new HashSet<>());
    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private GiftCertificateMapper certificateMapper;

    @Mock
    private GiftCertificateValidator validator;

    @InjectMocks
    private GiftCertificateServiceImpl certificateService;

    @Test
    void testFindAllCertificatesShouldReturnListOfAllCertificates() {
        List<GiftCertificate> certificates = List.of(certificate);
        Pageable pageable = PageRequest.of(0, 5);
        Page<GiftCertificate> page = new PageImpl<>(certificates, pageable, 5);

        Page<GiftCertificateDto> expectedPage = new PageImpl<>(List.of(certificateDto), page.getPageable(), page.getTotalElements());

        when(certificateRepository.findAll(pageable)).thenReturn(page);
        when(certificateMapper.toDto(any())).thenReturn(certificateDto);

        Page<GiftCertificateDto> actualPage = certificateService.findAll(pageable);
        assertEquals(expectedPage, actualPage);
        verify(certificateRepository).findAll(pageable);
    }

    @Test
    void testFindByIdShouldReturnFoundCertificateWithGivenId() {
        final long ID_ONE = 1;
        when(certificateRepository.findById(ID_ONE)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(any())).thenReturn(certificateDto);

        GiftCertificateDto actual = certificateService.findById(ID_ONE);

        assertEquals(certificateDto, actual);
        verify(certificateRepository).findById(anyLong());
    }

    @Test
    void testFindByIdShouldThrowExceptionIfCertificateNotFound() {
        when(certificateRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(EntityNotPresentException.class, () -> certificateService.findById(0));
    }

    @Test
    void testSaveCertificateShouldReturnCreatedCertificate() {
        when(validator.isValid(certificateDto)).thenReturn(true);
        when(certificateRepository.save(any())).thenReturn(certificate);
        when(certificateMapper.toDto(any())).thenReturn(certificateDto);

        GiftCertificateDto actual = certificateService.save(certificateDto);
        certificateDto.setTags(new HashSet<>());

        assertEquals(certificateDto, actual);
        verify(certificateRepository).save(Mockito.any());
    }

    @Test
    void testSaveCertificateShouldThrowExceptionWhenCertificateNotCorrect() {
        when(validator.isValid(certificateDto)).thenReturn(false);
        assertThrows(
                EntityNotValidException.class,
                () -> {
                    certificateService.save(certificateDto);
                });
    }

    @Test
    void testDeleteByIdShouldDeleteCertificateWithGivenId() {
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.of(certificate));

        certificateService.deleteById(1);
        certificateService.deleteById(2);

        verify(certificateRepository, times(2)).deleteById(anyLong());
    }
}
