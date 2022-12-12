package com.epam.esm.domain;

import java.io.Serializable;

/**
 * Interface that represents entities
 */
public interface BaseEntity extends Serializable {
    Long getId();

    void setId(Long id);
}
