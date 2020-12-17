package com.ssp.platform.telegram;

import java.util.List;

public interface TelegramUsersService {

    void save(TelegramUsersEntity usersEntity);

    List<TelegramUsersEntity> getAllUsers();

    boolean exists(Long chatId);

}
