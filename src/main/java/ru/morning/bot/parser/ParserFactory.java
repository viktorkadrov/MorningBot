package ru.morning.bot.parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.morning.bot.models.Article;
import ru.morning.bot.models.HibernateSessionFactoryCreator;
import ru.morning.bot.models.Joke;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

public class ParserFactory {
    public static ParserInterface getTextJokesParser() {
        return new TextJokesParser();
    }
    private static class TextJokesParser implements ParserInterface{
        private final Properties properties = new Properties();
        private int index;
        private final String config = "src/main/resources/config.properties";
        @Override
        public void parse() {
            int firstSourcePages, secondSourcePages;
            String userAgent, firstSource, secondSource;
            try {
                properties.load(new FileInputStream(config));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            firstSourcePages = Integer.parseInt(properties.getProperty("firstSourcePagesNumber"));
            secondSourcePages = Integer.parseInt(properties.getProperty("secondSourcePagesNumber"));
            firstSource = properties.getProperty("firstSource");
            secondSource = properties.getProperty("secondSource");
            userAgent = properties.getProperty("userAgent");
            SessionFactory sessionFactory = HibernateSessionFactoryCreator.createSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.createNativeQuery("delete from jokes").executeUpdate();
            for(int page = 1; page < firstSourcePages; page++) {
                try {
                    Document document = Jsoup.connect(firstSource + page)
                            .userAgent(userAgent)
                            .get();
                    Elements listElements = document.select("div.topicbox");
                    for (Element e : listElements.select("div.text")) {
                        session.save(new Joke(index, e.text(), 1));
                        index++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int page = 1; page < secondSourcePages; page++) {
                try {
                    Document parsedDoc1 = Jsoup.connect(secondSource + page)
                            .userAgent(userAgent)
                            .get();
                    Elements listElements = parsedDoc1.select("div.tecst");
                    for (Element e : listElements) {
                        e.children().remove();
                        session.save(new Joke(index, e.text(), 2));
                        index++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.getTransaction().commit();
            session.close();
        }

        @Override
        public int getIndex() {
            return index;
        }
    }
    private static class NewsParser implements ParserInterface {
        private final Properties properties = new Properties();
        private int index;
        private final String config = "properties/config.properties";
        @Override
        public void parse() {

        }
        @Override
        public int getIndex() {
            return index;
        }
    }
}
