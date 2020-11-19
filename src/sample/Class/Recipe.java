package sample.Class;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Recipe {
    private String rec_name;
    private ObservableList<IngRecipe> ingList = FXCollections.observableArrayList();

    public Recipe(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_name() {
        return rec_name;
    }

    public ObservableList<IngRecipe> getIngList() {
        return ingList;
    }

    public void addIngList(IngRecipe ingRecipe){
        ingList.add(ingRecipe);
    }
}
