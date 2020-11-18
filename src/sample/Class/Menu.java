package sample.Class;

import java.util.List;

public class Menu {

    private String menu_name;
    private List<Recipe> recipeList;

    public Menu(String menu_name, List<Recipe> recipeList) {
        this.menu_name = menu_name;
        this.recipeList = recipeList;
    }

//    public String getMenu_name() {
//        return menu_name;
//    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
