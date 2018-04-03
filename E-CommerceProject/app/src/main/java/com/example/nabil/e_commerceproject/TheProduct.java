package com.example.nabil.e_commerceproject;

/**
 * Created by Nabil on 08-Dec-17.
 */
public class TheProduct  {
    public String prodname,imgname;
    double price;
    int quantity,catIDf;

    public TheProduct() {
        this.prodname = "";
        this.price = 0;
        this.quantity = 0;
        this.imgname = "";
        this.catIDf = 0;
    }

    public TheProduct(String prodname, double price, int quantity, String imgname, int catIDf) {
        this.prodname = prodname;
        this.price = price;
        this.quantity = quantity;
        this.imgname = imgname;
        this.catIDf = catIDf;
    }
}
