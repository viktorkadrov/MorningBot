package ru.morning.bot.models;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryCreator {
    public static SessionFactory createSessionFactory() {
        return new Configuration()
                .configure()
                .addAnnotatedClass(BotUser.class)
                .addAnnotatedClass(Joke.class)
                .addAnnotatedClass(Article.class)
                .buildSessionFactory();
    }
}
