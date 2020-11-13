package sample.Class;

public class Recipe {
    private String rec_name;
    private double rec_price;

    public Recipe(String rec_name, double rec_price) {
        this.rec_name = rec_name;
        this.rec_price = rec_price;
    }

    public String getRec_name() {
        return rec_name;
    }

    public double getRec_price() {
        return rec_price;
    }

    public void setRec_price(double rec_price) {
        this.rec_price = rec_price;
    }
}
