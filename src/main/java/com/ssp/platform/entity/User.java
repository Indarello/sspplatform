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
                //@UniqueConstraint(columnNames = "email"),     могут быть пустыми
                //@UniqueConstraint(columnNames = "account"),
                //@UniqueConstraint(columnNames = "telephone"),
                //@UniqueConstraint(columnNames = "inn")
                @UniqueConstraint(columnNames = "username")
        })
public class User
{
    @Id
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /**
     * Имя контактного лица
     */
    @NotBlank
    private String firstName;

    /**
     * Фамилия контактного лица
     */
    @NotBlank
    private String lastName;

    /**
     * Отчество контактного лица
     */
    @NotNull
    private String patronymic;

    /**
     * название компании
     */
    @NotNull
    private String firmName;

    /**
     * описание компании
     */
    @NotNull
    private String description;

    /**
     * Адрес компании
     */
    @NotNull
    private String address;

    /**
     * Вид деятельности компании
     * Например: Делаем лучшие веб сервисы
     */
    @NotNull
    private String activity;

    /**
     * Стек технологии
     */
    @NotNull
    private String technology;

    @NotNull
    private String inn;

    /**
     * номер счета
     */
    @NotNull
    private String account;

    @NotNull
    private String telephone;

    @NotNull
    @Email
    private String email;

    /**
     * роль в системе (сторонняя компания|сотрудник ssp) firm|employee
     */
    @NotBlank
    private String role;

    /**
     * Статус в системе (Не подтвержден|Подтвержден|Данные изучаются|Заблокирвоан) (NotApproved|Approved|Review|Banned)
     */
    @NotBlank
    private String status;

    public User()
    {
    }

    /**
     * конструктор для создания поставщинка
     */
    public User(String username, String password, String firstName, String lastName, String firmName, String activity, String inn, String email)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = "";
        this.firmName = firmName;
        this.description = "";
        this.address = "";
        this.activity = activity;
        this.technology = "";
        this.inn = inn;
        this.account = "";
        this.telephone = "";
        this.email = email;
        this.role = "firm";
        this.status = "NotApproved";
    }

    /**
     * конструктор для создания сотрудника компании
     */
    public User(String username, String password, String firstName, String lastName)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = "";
        this.firmName = "";
        this.description = "";
        this.address = "";
        this.activity = "";
        this.technology = "";
        this.inn = "";
        this.account = "";
        this.telephone = "";
        this.email = "";
        this.role = "employee";
        this.status = "Approved";
    }
}
