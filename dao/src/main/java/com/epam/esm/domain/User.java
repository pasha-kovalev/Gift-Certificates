package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents User entity from database
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
@Audited
@Entity
public class User implements BaseEntity {

    /**
     * User identifier in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User name
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * User password
     */
    @Column(name = "password")
    private String password;

    /**
     * User role
     */
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
