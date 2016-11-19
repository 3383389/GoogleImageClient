package com.example.android.lesson5googleimg.utils.eventBus;

public class MessageEvent {
    public final Messages message;
    public final String str;
    public final Integer position;

    public MessageEvent(Messages message, String q) {
        this.message = message;
        this.str = q;
        this.position = null;
    }

    public MessageEvent(Messages message, Integer pos) {
        this.message = message;
        this.str = null;
        this.position = pos;
    }

    public MessageEvent(Messages message) {
        this.message = message;
        this.str = null;
        this.position = null;
    }

}
