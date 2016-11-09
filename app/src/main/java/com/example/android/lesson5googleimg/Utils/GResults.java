package com.example.android.lesson5googleimg.Utils;

import java.util.ArrayList;
import java.util.List;

public class GResults {

    public String link;
    public List<GResults> items;
    public Boolean isLoading = false;

    public GResults() {
        items = new ArrayList<>();
    }

    public List<GResults> getItems() {
        return items;
    }

    public String getLink(int i) {
        return items.get(i).toString();
    }

    public String toString() {
        return String.format("%s", link);
    }

}
