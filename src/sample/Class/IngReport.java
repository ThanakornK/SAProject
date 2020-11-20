package sample.Class;

public class IngReport {
    private Ingredient Ing;
    private String Ing_name;
    private Double Ing_amount;
    private Double Ing_quan;
    private Double Ing_add;
    private Double Ing_cost;
    private Double Ing_add2;
    private Double Ing_originCost;

    public IngReport(Ingredient ing, Double ing_usage) {
        this.Ing = ing;
        this.Ing_quan = ing_usage;
        this.Ing_add = ing.getIng_amount() - this.Ing_quan;
        this.Ing_add2 = Ing_add;
        this.Ing_name = ing.getIng_name();
        this.Ing_amount = ing.getIng_amount();
        this.Ing_originCost = Ing_add2*ing.getIng_price();
    }

    public IngReport(Ingredient ing, Double ing_usage, Double ing_cost) {
        this.Ing = ing;
        this.Ing_quan = ing_usage;
        this.Ing_cost = ing_cost;
        this.Ing_add = ing.getIng_amount() - this.Ing_quan;
        this.Ing_add2 = Ing_add;
        this.Ing_name = ing.getIng_name();
        this.Ing_amount = ing.getIng_amount();
    }

    public Ingredient getIng() {
        return Ing;
    }

    public Double getIng_quan() {
        return Ing_quan;
    }

    public Double getIng_cost() {
        return Ing_cost;
    }

    public Double getIng_add() {
        return Ing_add;
    }

    public Double getIng_amount() {
        return Ing_amount;
    }

    public String getIng_name() {
        return Ing_name;
    }

    public Double getIng_add2() {
        return Ing_add2;
    }

    public void setIng_add2(Double ing_add2) {
        Ing_add2 = ing_add2;
    }

    public Double getIng_originCost() {
        return Ing_originCost;
    }
}
