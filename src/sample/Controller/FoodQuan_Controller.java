package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodQuan_Controller {

    @FXML
    private Button FoodPlanBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private AnchorPane selectMenu_con;


    @FXML
    private Button menuPageBtn;

    @FXML
    private Button menuConfirmBtn;

    @FXML
    private AnchorPane showMenu_con;

    @FXML
    private Button menuPageBtn1;

    @FXML
    private Button menuConfirmBtn1;

    @FXML
    private TableView<RecipeReport> recPlanQuan_table;

    @FXML
    private TableView<IngReport> ingPlaQuan_table;

    @FXML
    private DatePicker dateSale;

    @FXML
    private TableColumn<RecipeReport, String> rec_nameCol;

    @FXML
    private TableColumn<RecipeReport, Double> rec_totalCol;

    @FXML
    private TableColumn<IngReport, String> ing_nameCol;

    @FXML
    private TableColumn<IngReport, Double> ing_quanCol;

    @FXML
    private TextField menu_name_field;

    private String menuSelect = "";

    private AlertBox alertBox;

    private ObservableList<RecipeReport> recList = FXCollections.observableArrayList();
    private ObservableList<IngReport> ingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (menuSelect != ""){
                    readSetRecipe_tb(recList);

                    rec_nameCol.setCellValueFactory(new PropertyValueFactory<RecipeReport,String>("recNameReport"));
                    rec_totalCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, Double>("total_fqReport"));
                    setRecipeReportColumnDouble(rec_totalCol);

                    readSetIng_tb(ingList);
                    ing_nameCol.setCellValueFactory(new PropertyValueFactory<IngReport, String>("Ing_name"));
                    ing_quanCol.setCellValueFactory(new PropertyValueFactory<IngReport, Double>("Ing_quan"));
                    setIngReportColumnDouble(ing_quanCol);
                }
            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    public void setIngReportColumnDouble(TableColumn<IngReport, Double> tableCol) {
        tableCol.setCellFactory(tc -> new TableCell<IngReport, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f",item));
                }
            }
        });
    }

    public void setRecipeReportColumnDouble(TableColumn<RecipeReport, Double> tableCol) {
        tableCol.setCellFactory(tc -> new TableCell<RecipeReport, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f",item));
                }
            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    public void setMenuSelect(String str) {
        menuSelect = str;
    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void go_menu_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    public void handleSearchBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/FoodMenu_search_select_page.fxml",event);
        Screen sc = Screen.getPrimary();
        cs.changeStageAction(sc);

    }

    @FXML
    public void handleCheckStoreBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Ingredient_store_page.fxml");
        cs.newWindow();

    }

    @FXML
    public void go_FoodLeft_page(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/FoodLeft_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    @FXML
    public void handleRecomBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/RecommendFoodQuan_page.fxml");
        cs.newWindow();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleMenuConfirm(ActionEvent event) {

    }

    public void readSetRecipe_tb(ObservableList<RecipeReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MenuRecipe.Rec_name, MenuRecipe.Recommend_fq " +
                    "FROM MenuRecipe " +
                    "WHERE MenuRecipe.Menu_name = ? ;";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuSelect);
            rs = ps.executeQuery();

            while(rs.next()) {
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");

                RecipeReport rp = new RecipeReport(regName,recommendFq,0.0);
                list.add(rp);

            }
            recPlanQuan_table.setItems(list);


        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void readSetIng_tb(ObservableList<IngReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT MenuRecipe.Rec_name, Ingredient.Ing_name, SUM(IngRec.Ing_quan), Ingredient.Ing_price, Ingredient.Ing_amount " +
                    "FROM (( IngRec " +
                    "INNER JOIN MenuRecipe ON MenuRecipe.Rec_name = IngRec.Rec_name) INNER JOIN Ingredient ON Ingredient.Ing_name = IngRec.Ing_name) " +
                    "WHERE MenuRecipe.Menu_name = ? " +
                    "GROUP BY Ingredient.Ing_name;";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuSelect);
            rs = ps.executeQuery();
            while (rs.next()) {

                String ing_name = rs.getString("Ing_name");
                double ing_amount = rs.getDouble("Ing_amount");
                double ing_quan = rs.getDouble("SUM(IngRec.Ing_quan)");
                double ing_price = rs.getDouble("Ing_price");

                Ingredient newIng = new Ingredient(ing_name,ing_price,ing_amount);
                IngReport newIngReport = new IngReport(newIng,ing_quan,newIng.getIng_price()*ing_quan);
                list.add(newIngReport);

            }
            ingPlaQuan_table.setItems(list);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }







}