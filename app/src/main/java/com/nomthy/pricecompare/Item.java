package com.nomthy.pricecompare;

import java.util.ArrayList;

public class Item {
    String name;
    ArrayList<Price> prices;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrices(ArrayList prices) {
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Price> getPrices() {
        return prices;
    }
}
