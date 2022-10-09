package ru.morning.bot;

import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.morning.bot.files.Reply;
import ru.morning.bot.files.Sticker;
import ru.morning.bot.keyboards.InlineKeyboardUserResponse;
import ru.morning.bot.keyboards.ReplyKeyboardMaker;
import ru.morning.bot.models.BotUser;
import ru.morning.bot.models.HibernateSessionFactoryCreator;
import ru.morning.bot.models.Joke;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

public class Bot extends TelegramLongPollingBot {
    private final String config = "src/main/resources/config.properties";
    private final Properties properties = new Properties();
    Bot() {
        try {
            properties.load(new FileInputStream(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Random random = new Random();
    private int dbSize;
    private SessionFactory sessionFactory;

    public void setDbSize(int dbSize) {
        this.dbSize = dbSize;
    }

    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.name");
    }

    @Override
    public String getBotToken() {
        return properties.getProperty("bot.token");
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            sessionFactory = HibernateSessionFactoryCreator.createSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            handleMessage(message);
        }
    }

    private void handleMessage(Message message) {
        String chatId = message.getChatId().toString();
        addUser(message.getFrom());
        Session session = sessionFactory.openSession();
        BotUser botUser = session.get(BotUser.class, chatId);
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message
                    .getEntities()
                    .stream()
                    .filter(msg -> "bot_command".equals(msg.getType()))
                    .findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText();
                switch (command) {
                    case "/help":
                        try {
                            execute(SendMessage.builder()
                                    .chatId(message.getChatId().toString())
                                    .text("Говорят, смех продлевает жизнь")
                                    .build());
                            send(chatId, new InputFile(Sticker.DEAD_SMILE.getId()));
                            break;
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    case "/start":
                        ReplyKeyboardMaker replyKeyboardMaker = new ReplyKeyboardMaker();
                        try {
                            execute(SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Самое время хорошо посмеяться! Поехали?")
                                    .replyMarkup(replyKeyboardMaker
                                            .getMainMenuKeyboard()).build());
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        send(chatId, new InputFile(Sticker.TYPING_SMILE.getId()));
                        break;
                    case "/getnumber":
                        String reply = "Сейчас в базе " + dbSize + " шуток!";
                        send(chatId, reply);
                        send(chatId,new InputFile(Sticker.POPCORN_SMILE.getId()));
                        break;
                    case "/change_source":
                        InlineKeyboardUserResponse inlineKeyboardUserResponse = new InlineKeyboardUserResponse();
                        send(chatId, "Выберите источник шуток:", inlineKeyboardUserResponse.getSurveyKeyboardMarkup());
                        break;
                }
                session.close();
            }
        } else if (message.getText().equals(Reply.JOKE.getText())) {
            session = sessionFactory.openSession();
            List<Object[]> result = session.createNativeQuery("select * from jokes where type = " + botUser.getChoice()).list();
            Object[] joke = result.get(random.nextInt(result.size()));
            send(chatId, joke[1].toString());
            send(chatId, "Понравилась шутка?", new InlineKeyboardUserResponse().getInlineKeyboardMarkup());
            session.close();
        } else if (message.hasText()) {
            try {
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Я вас не понимаю...")
                        .build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleCallback(CallbackQuery callback) {
        String chatId = callback.getMessage().getChatId().toString();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        BotUser botUser = session.get(BotUser.class, chatId);
        session.getTransaction().commit();
        session.beginTransaction();
        switch (callback.getData()) {
            case "good_joke":
                send(chatId, "А вы ценитель!");
                break;
            case "bad_joke":
                send(chatId, new InputFile(Sticker.CRYING_SMILE.getId()));
                break;
            case "type_1":
                send(chatId, new InputFile(Sticker.OK_SMILE.getId()));
                botUser.setChoice(1);
                session.update(botUser);
                break;
            case "type_2":
                send(chatId, new InputFile(Sticker.OK_SMILE.getId()));
                botUser.setChoice(2);
                session.update(botUser);
                break;
        }
        session.getTransaction().commit();
        session.close();
    }


    private void send(String chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(String chatId, InputFile sticker) {
        try {
            execute(SendSticker.builder()
                    .chatId(chatId)
                    .sticker(sticker)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(String chatId, String text, InlineKeyboardMarkup markup) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(markup)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void addUser(User user) {
        Session session = sessionFactory.openSession();
        BotUser botUser = new BotUser();
        botUser.setUser(user);
        NativeQuery result = session.createNativeQuery("select * from users where id=" + user.getId());
        if (result.list().size() == 0) {
            session.beginTransaction();
            session.save(botUser);
            session.getTransaction().commit();
        }
        session.close();
    }
}
