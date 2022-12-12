package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface represents service for Gift Certificate entity
 */
public interface GiftCertificateService extends BaseDtoService<GiftCertificateDto> {
    /**
     * Find all Gift Certificate by given parameters
     *
     * @param search   the search
     * @param sort     the sort
     * @param pageable the pagination information
     * @return the page
     */
    Page<GiftCertificateDto> findAllBy(String search, String sort, Pageable pageable);

    /**
     * Updates Gift Certificate
     *
     * @param id                 the Gift Certificate id
     * @param giftCertificateDto the Gift Certificate
     * @return the updated Gift Certificate
     */
    GiftCertificateDto update(long id, GiftCertificateDto giftCertificateDto);

    /**
     * Deletes row from join table
     *
     * @param certificateId Certificate id
     * @param tagId         Tag id
     */
    void untieTag(long certificateId, long tagId);
}
