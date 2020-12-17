package com.ssp.platform.telegram;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface BotHandler {

    List<PartialBotApiMethod<? extends Serializable>> handle(String message);

}
