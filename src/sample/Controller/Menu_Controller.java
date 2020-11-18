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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.*;
import sample.Class.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Menu_Controller {

    @FXML
    private Button recipe_page_btn,home_btn,ing_page_btn,menu_page_btn,update_rec_btn,sale_his_btn;

    @FXML
    private Text recipe_name;

    @FXML
    private TableView<?> ing_list;

    @FXML
    private TableColumn<MenuRecipe, String> menu_name;

    @FXML
    private ListView<MenuRecipe> listViewMenu;

    @FXML
    private TextField menu_name_field;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<MenuRecipe> menuList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    Recipe selectRecipe;

    AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllMenu(menuList);  // read menuRecipe from database
                readAllRec(recipesList);  // read recipe from database

                listViewMenu.setCellFactory(param -> new ListCell<MenuRecipe>() {
                    @Override
                    protected void updateItem(MenuRecipe item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getMenu_name() == null) {
                            setText(null);
                        } else {
                            setText(item.getMenu_name());
                        }
                    }
                });

                FilteredList<MenuRecipe> menuFilterList = new FilteredList<>(menuList, p -> true);
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
                SortedList<MenuRecipe> menuSortedList = new SortedList<>(menuFilterList);

                listViewMenu.setItems(menuSortedList);
//NullPointerException
                menu_name.setCellValueFactory(new PropertyValueFactory<>("MenuName"));
            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    private void readAllMenu(ObservableList<MenuRecipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM MenuRecipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String MenuName = rs.getString("Menu_name");

                MenuRecipe newMenu = new MenuRecipe(MenuName,recipesList);
                list.add(newMenu);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllRec(ObservableList<Recipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");

                Recipe readRec = new Recipe(recName);

                String sql2 = String.format("SELECT * FROM IngRec WHERE Rec_name = '%s'",readRec.getRec_name());
                ps2 = con.prepareStatement(sql2);
                rs2 = ps2.executeQuery();
                while (rs2.next()){
                    String ingName = rs2.getString("Ing_name");
                    String rec_Name = rs2.getString("Rec_name");
                    double ingQuan = rs2.getDouble("Ing_quan");
                    System.out.println(String.format("Added %s into %s", ingName, rec_Name));
                    IngRecipe temp = new IngRecipe(ingName, recName, ingQuan);
                    temp.calculateTotalCost(ingredientList);
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

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
