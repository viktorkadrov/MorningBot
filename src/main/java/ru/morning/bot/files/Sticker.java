package ru.morning.bot.files;

public enum Sticker {
    DEAD_SMILE("CAACAgIAAxkBAAEEtV5ifCR1Ig-ATLmGfyRt9c_KIt9vtQACSgEAAntOKhA9uYlnbMSMBSQE"),
    CRYING_SMILE("CAACAgIAAxkBAAEEuKlif3F6t7pC7HixMhp-yhYEjfj5mQACcwEAAntOKhDPNF5G-K1qVSQE"),
    TYPING_SMILE("CAACAgIAAxkBAAEEuK9if3N2NPGuxJBjSSxQf-qFeviQaAACewEAAntOKhCj7UGbbsk8uCQE"),
    POPCORN_SMILE("CAACAgIAAxkBAAEE0ldijqmZ40afisQyahImQWnBpQ6hngACXwEAAntOKhCpcl2sDH6u9iQE"),

    OK_SMILE("CAACAgIAAxkBAAEE11pikhm_UEu1kx55hAABayqFDu1NHbIAAmwBAAJ7TioQTA1I3E1UljAkBA");
    private final String id;
    Sticker(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
