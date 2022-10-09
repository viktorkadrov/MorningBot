package ru.morning.bot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.telegram.telegrambots.meta.api.objects.User;

@Entity
@Table(name = "users")
public class BotUser {
    @Id
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "subscription")
    private boolean subscription;
    @Column(name = "choice")
    private int choice = 1;

    public void setUser(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
