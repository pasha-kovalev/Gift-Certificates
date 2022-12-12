package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;

/**
 * Represents Tag entity from database
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Audited
@Table(name = "tag")
@Entity
public class Tag implements BaseEntity {
    @Serial
    private static final long serialVersionUID = 1611302132574963486L;

    /**
     * Tag identifier in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tag name
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
