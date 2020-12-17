package com.ssp.platform.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUsersServiceImpl implements TelegramUsersService{

    private final TelegramUsersRepository usersRepository;

    public TelegramUsersServiceImpl(TelegramUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void save(TelegramUsersEntity usersEntity) {
        System.out.println(usersEntity.getChatId() + " " + usersEntity.getName());
        usersRepository.save(usersEntity);
    }

    @Override
    public List<TelegramUsersEntity> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public boolean exists(Long chatId) {
        return usersRepository.existsById(chatId);
    }
}
