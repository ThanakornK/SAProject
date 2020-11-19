package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu_Controller {

    @FXML
    private Button recipe_page_btn,home_btn,ing_page_btn,menu_page_btn,update_menu_btn, delete_btn;

    @FXML
    private ListView<MenuRecipe> listViewMenu;

    @FXML
    private ListView<String> listViewRec;

    @FXML
    private TextField menu_name_field;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<MenuRecipe> menuList = FXCollections.observableArrayList();

    private ObservableList<String> recNameList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    private  ObservableList<Recipe> selectMenuRec = FXCollections.observableArrayList();

    MenuRecipe selectMenu;

    AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllIng(ingredientList);
                readAllRec(recipesList);  // read recipe from database
                readAllMenuName(menuList);  // read menu from database
                if(!menu_name_field.getText().isEmpty()){

                }



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
            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    private void readAllMenuName(ObservableList<MenuRecipe> list){   // For listing menu name
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Menu_name FROM MenuRecipe GROUP BY Menu_name";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String menuName = rs.getString("Menu_name");

                MenuRecipe nameOnly = new MenuRecipe(menuName);
                list.add(nameOnly);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllMenuRec(ObservableList<String> list){ // For listing recipe
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = String.format("SELECT Rec_name FROM MenuRecipe WHERE Menu_name = '%s'", menu_name_field.getText());
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");

                list.add(recName);


        }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
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
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllIng(ObservableList<Ingredient> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Ingredient";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ingName = rs.getString("Ing_name");
                double ingPrice = rs.getDouble("Ing_price");
                int ingAmount = rs.getInt("Ing_amount");

                Ingredient newIngredient = new Ingredient(ingName, ingPrice, ingAmount);
                list.add(newIngredient);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    @FXML
    void getSelectMenu(MouseEvent event) {

        recNameList.clear();

        if (listViewMenu.getSelectionModel().getSelectedItem() != null){   // check select row not null

            menu_name_field.setText(listViewMenu.getSelectionModel().getSelectedItem().getMenu_name());

            delete_btn.setVisible(true);
            delete_btn.setDisable(false);

            readAllMenuRec(recNameList);


            listViewRec.setCellFactory(param -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });
            listViewRec.setItems(recNameList);

        }
    }

    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    public void handleReBtn(){
        if( !(menu_name_field.getText().isEmpty() && recNameList.isEmpty()) ){
            menu_name_field.clear();
            recNameList.clear();
            listViewRec.refresh();
        }
    }


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
    void handle_update_rec(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_update_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleDelBtn(){
        if( alertBox.alertConfirm("คุณยืนยันที่จะลบเมนูนี้หรือไม่") == 0){
            if(dbConnect.deleteRecord("delete FROM MenuRecipe WHERE Menu_name = ?", "str", menu_name_field.getText()) == 0){
                System.out.println("Delete successful");
            } else {
                alertBox.normalAlert("err", "การลบข้อมูลผิดพลาด");
            }
        }
    }


}