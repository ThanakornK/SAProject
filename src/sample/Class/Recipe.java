package sample.Class;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Recipe {
    private String rec_name;
//    private double rec_price;
    private ObservableList<IngRecipe> ingList = FXCollections.observableArrayList();

    public Recipe(String rec_name) {
        this.rec_name = rec_name;
//        this.rec_price = rec_price;
    }

    public String getRec_name() {
        return rec_name;
    }

//    public double getRec_price() {
//        return rec_price;
//    }

    public ObservableList<IngRecipe> getIngList() {
        return ingList;
    }

//    public void setRec_price(double rec_price) {
//        this.rec_price = rec_price;
//    }

    public void addIngList(IngRecipe ingRecipe){
        ingList.add(ingRecipe);
    }
}
