package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                //@UniqueConstraint(columnNames = "email"),     могут быть пустыми
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    //TODO поменять в будущем чтобы начинались с маленькой буквы, возможно сделать enum так же как и role
    /**
     * Статус в системе (Не подтвержден|Подтвержден|Данные изучаются|Заблокирвоан) (NotApproved|Approved|Review|Banned)
     */
    @NotBlank
    private String status;

    @OneToMany(mappedBy="author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Purchase> purchases;

    @OneToMany(mappedBy="author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Purchase> supplies;

    public User()
    {
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
}
