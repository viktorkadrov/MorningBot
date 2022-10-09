package ru.morning.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.morning.bot.parser.ParserFactory;
import ru.morning.bot.parser.ParserInterface;

public class Main {
    public static void main(String[] args) {
        ParserInterface jokesParser = ParserFactory.getTextJokesParser();
        jokesParser.parse();
        try {
            Bot bot = new Bot();
            bot.setDbSize(jokesParser.getIndex());
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
