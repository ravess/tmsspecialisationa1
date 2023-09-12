package com.tms.a1.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "accounts")
public class User {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Nonnull
    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Nonnull
    @Column(name = "is_active")
    private int is_active;

    @Column(name = "`groups`")
    private String groups;

}
