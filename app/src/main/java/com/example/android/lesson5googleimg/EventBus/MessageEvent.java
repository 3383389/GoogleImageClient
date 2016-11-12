package com.example.android.lesson5googleimg.EventBus;

public class MessageEvent {
    public final Messages message;
    public final String str;

    public MessageEvent(Messages message, String q) {
        this.message = message;
        this.str = q;
    }

    public MessageEvent(Messages message) {
        this.message = message;
        this.str = null;
    }

}
