package com.nomthy.pricecompare;

public class Price {
    String shopname;
    String shopaddress;
    Double price;

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShopname() {
        return shopname;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public Double getPrice() {
        return price;
    }
}
