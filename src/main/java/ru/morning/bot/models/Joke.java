package ru.morning.bot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jokes")
public class Joke {
    @Id
    private long id;
    @Column(name = "joke")
    private String text;

    @Column(name = "type")
    private int type;

    public Joke() {}

    public Joke(long id, String text, int type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
