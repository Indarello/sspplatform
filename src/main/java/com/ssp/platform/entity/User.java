package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
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
     * роль в системе (сторонняя компания|сотрудник ssp) firm|employee
     */
    @NotBlank
    private String role;

    //TODO удаление пользователя должно удалить закупки, пока что выдает ошибку на удалении
    @OneToMany(mappedBy="author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Purchase> purchases;

    public User()
    {
    }

    public User(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
