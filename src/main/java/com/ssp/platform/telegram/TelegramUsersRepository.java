package com.ssp.platform.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUsersRepository extends JpaRepository<TelegramUsersEntity, Long> {

}
