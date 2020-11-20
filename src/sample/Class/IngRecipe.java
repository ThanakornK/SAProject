package sample.Class;


import javafx.collections.ObservableList;

public class IngRecipe {
    private String ingName;
    private String recName;
    private double ingQuan;
    private double totalCost;

    public IngRecipe(String ingName, String recName, double recQuan){
        this.ingName = ingName;
        this.recName = recName;
        this.ingQuan = recQuan;
    }

    public String getIngName() {
        return ingName;
    }

    public Double getIngQuan() {
        return ingQuan;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setIngQuan(Double recQuan) {
        this.ingQuan = recQuan;
    }

    public void calculateTotalCost(ObservableList<Ingredient> ingredients){
        for(Ingredient i: ingredients){
            if(i.getIng_name().equals(ingName)){
                totalCost = i.getIng_price()*this.ingQuan;
            }
            else{
                continue;
            }
        }
    }

}
