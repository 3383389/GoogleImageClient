package com.example.android.lesson5googleimg.models;

import java.util.ArrayList;
import java.util.List;

public class GoogleResults {

    public String link;
    public List<GoogleResults> items;

    public GoogleResults() {
        items = new ArrayList<>();
    }

    public List<GoogleResults> getItems() {
        return items;
    }

    public String getLink(int i) {
        return items.get(i).toString();
    }

    public String toString() {
        return String.format("%s", link);
    }

}
