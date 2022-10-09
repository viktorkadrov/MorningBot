package ru.morning.bot.files;

public enum Reply {
    JOKE("Анекдот!"),
    NEWS("Новость");

    public String getText() {
        return text;
    }

    private final String text;
    Reply(String text){
        this.text = text;
    }

}

