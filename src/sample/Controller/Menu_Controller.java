package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.Ingredient;
import sample.Class.Menu;
import sample.Class.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu_Controller {
    @FXML
    private Button menu_page_btn;

    @FXML
    private Button ing_page_btn;

    @FXML
    private Button recipe_page_btn;

    @FXML
    private Button home_btn;

    @FXML
    private Text recipe_name;

    @FXML
    private TableView<?> ing_list;

    @FXML
    private ListView<Menu> listViewMenu;

    @FXML
    private Button update_rec_btn;

    @FXML
    private Button sale_his_btn;

    @FXML
    private TextField menu_name_field;

    ObservableList<Menu> menuList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {


                Recipe ex1 = new Recipe("Egg fried rice",35);
                Recipe ex2 = new Recipe("Gang Green Sweet",45);
                List<Recipe> recipeList = new ArrayList<>();
                recipeList.add(ex1); recipeList.add(ex2);
                Menu menu1 = new Menu("ตลาดนัดคลองข้าง",recipeList);
                Menu menu2 = new Menu("ตลาดนัดคลองไกล",recipeList);

                menuList.add(menu1); menuList.add(menu2);

                listViewMenu.setCellFactory(param -> new ListCell<Menu>() {
                    @Override
                    protected void updateItem(Menu item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getMenu_name() == null) {
                            setText(null);
                        } else {
                            setText(item.getMenu_name());
                        }
                    }
                });

                FilteredList<Menu> menuFilterList = new FilteredList<>(menuList, p -> true);
                menu_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
                    menuFilterList.setPredicate(menu -> {
                        if (newValue == null || newValue.isEmpty()){
                            return true;
                        }

                        if (menu.getMenu_name().indexOf(newValue) != -1){
                            return true;
                        } else{
                            return false;
                        }
                    });

                });

                // wrap filter list to sorted list
                SortedList<Menu> menuSortedList = new SortedList<>(menuFilterList);

                listViewMenu.setItems(menuSortedList);

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    @FXML
    void getSelectMenu(MouseEvent event) {
        if (listViewMenu.getSelectionModel().getSelectedItem() != null){   // check select row not null

            sale_his_btn.setDisable(false);
            sale_his_btn.setVisible(true);

            menu_name_field.setText(listViewMenu.getSelectionModel().getSelectedItem().getMenu_name());

        }
    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void go_ing_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Ingredient_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void go_recipe_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Recipe_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void handleHomeBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void handle_sale_his(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_sale_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("เพิ่ม/แก้ไข เมนู");
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    @FXML
    void handle_update_rec(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_update_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    //-------------------------------------------- database method -----------------------------------------------------


}
