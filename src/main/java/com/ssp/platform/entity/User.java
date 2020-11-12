package com.ssp.platform.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "account"),
                @UniqueConstraint(columnNames = "telephone")
        })
public class User
{
    @Id
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    /**
     * номер счета
     */
    @NotNull
    private String account;

    @NotNull
    private BigInteger telephone;

    /**
     * название компании
     */
    @NotNull
    private String company;

    /**
     * описание компании
     */
    @NotNull
    private String description;

    /**
     * адресс компании
     */
    @NotNull
    private String address;

    @NotNull
    private BigInteger inn;

    /**
     * Контактное лицо - ФИО
     */
    @NotNull
    private String contact;

    public User()
    {
    }

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
