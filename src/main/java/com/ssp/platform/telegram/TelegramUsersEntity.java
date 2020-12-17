package com.ssp.platform.telegram;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "telegram_users")
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUsersEntity {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

}
