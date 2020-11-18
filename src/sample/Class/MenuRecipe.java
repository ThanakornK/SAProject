package sample.Class;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MenuRecipe {
    private int menuRec_ID;
    private String menu_name;
    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();
    private Double recommend_fq;

    public MenuRecipe(int menuRec_ID, String menu_name, ObservableList<Recipe> recipeList) {
        this.menuRec_ID = menuRec_ID;
        this.menu_name = menu_name;
        this.recipeList = recipeList;
    }

    public MenuRecipe( String menu_name, ObservableList<Recipe> recipeList) {
        this.menu_name = menu_name;
        this.recipeList = recipeList;
    }

    public MenuRecipe ( String menu_name ){
        this.menu_name = menu_name;
    }

    public int getMenuRec_ID() {
        return menuRec_ID;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public ObservableList<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(ObservableList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public Double getRecommend_fq() {
        return recommend_fq;
    }

    public void setRecommend_fq(Double recommend_fq) {
        this.recommend_fq = recommend_fq;
    }

    public void addRecList(Recipe rec){
        this.recipeList.add(rec);
    }
}
