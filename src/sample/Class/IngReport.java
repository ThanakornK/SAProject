package sample.Class;

public class IngReport {
    private Ingredient Ing;
    private String Ing_name;
    private Double Ing_am;
    private Double Ing_quan;
    private Double Ing_add;
    private Double Ing_cost;

    public IngReport(Ingredient ing, Double ing_usage, Double ing_cost) {
        this.Ing = ing;
        this.Ing_quan = ing_usage;
        this.Ing_cost = ing_cost;
        this.Ing_add = ing.getIng_amount() - this.Ing_quan;
        this.Ing_name = ing.getIng_name();
        this.Ing_am = ing.getIng_amount();
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

    public Double getIng_am() {
        return Ing_am;
    }

    public String getIng_name() {
        return Ing_name;
    }
}
