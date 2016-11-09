package com.example.android.lesson5googleimg.EventBus;

public class MessageEvent {
    public final Messages message;
    public final String query;

    public MessageEvent(Messages message, String q) {
        this.message = message;
        this.query = q;
    }

    public MessageEvent(Messages message) {
        this.message = message;
        this.query = null;
    }

}
