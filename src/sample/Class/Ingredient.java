package sample.Class;

import javafx.scene.control.Button;

public class Ingredient {
    private String Ing_name;
    private double Ing_price;
    private int Ing_amount;

    public Ingredient(String ing_name, double ing_price, int ing_amount) {
        this.Ing_name = ing_name;
        this.Ing_price = ing_price;
        this.Ing_amount = ing_amount;

    }

    public String getIng_name() {
        return Ing_name;
    }

    public double getIng_price() {
        return Ing_price;
    }

    public void setIng_price(double ing_price) {
        Ing_price = ing_price;
    }

    public int getIng_amount() {
        return Ing_amount;
    }

    public void setIng_amount(int ing_amount) {
        Ing_amount = ing_amount;
    }

}
