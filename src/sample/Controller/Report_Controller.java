package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Report_Controller {

    @FXML
    private TableView<RecipeReport> recipe_table;

    @FXML
    private TableColumn<RecipeReport, String> recipe_nameCol;

    @FXML
    private TableColumn<RecipeReport, Double> recQuanCol;

    @FXML
    private TableColumn<RecipeReport, Double> planQuanCol;

    @FXML
    private TableView<IngReport> ing_table;

    @FXML
    private TableColumn<IngReport, String> ing_nameCol;

    @FXML
    private TableColumn<IngReport, Double> ing_amountCol;

    @FXML
    private TableColumn<IngReport, Double> need_quanCol;

    @FXML
    private TableColumn<IngReport, Double> add_quanCol;

    @FXML
    private TableColumn<IngReport, Double> ing_costCol;

    @FXML
    private Button newMenuBtn, chooseMenuBtn, back_btn, pdfBtn;

    @FXML
    private Text menu_name_text;

    private Menu selectMenu, lastMenuUpdate;

    private AlertBox alertBox = new AlertBox();

    private ObservableList<RecipeReport> recipeReportList = FXCollections.observableArrayList();

    private ObservableList<IngReport> ingReportList = FXCollections.observableArrayList();

    private String menuChoose = "";

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                recipe_nameCol.setCellValueFactory( new PropertyValueFactory<RecipeReport, String>("recNameReport"));
                recQuanCol.setCellValueFactory( new PropertyValueFactory<RecipeReport, Double>("recommend_fqReport"));
                planQuanCol.setCellValueFactory( new PropertyValueFactory<RecipeReport, Double>("total_fqReport"));

                ing_nameCol.setCellValueFactory( new PropertyValueFactory<IngReport, String>("Ing_name"));
                ing_amountCol.setCellValueFactory( new PropertyValueFactory<IngReport, Double>("Ing_am"));
                need_quanCol.setCellValueFactory( new PropertyValueFactory<IngReport, Double>("Ing_quan"));
                add_quanCol.setCellValueFactory( new PropertyValueFactory<IngReport, Double>("Ing_add"));
                ing_costCol.setCellValueFactory( new PropertyValueFactory<IngReport, Double>("Ing_cost"));

                if (menuChoose == ""){
                    //- - - - - - - - - -  Recipe Report - - - - - - - - - - - - -
                    readSetRecipeReport(recipeReportList);

                    setRecipeReportColumnDouble(recQuanCol); setRecipeReportColumnDouble(planQuanCol);
                    recipe_table.setItems(recipeReportList);

                    //- - - - - - - -  - - - -  - - - - - - - - - - - - - - - - - - - -

                    readSetIngReport(ingReportList);

                    setIngReportColumnDouble(ing_amountCol); setIngReportColumnDouble(add_quanCol);
                    setIngReportColumnDouble(ing_costCol); setIngReportColumnDouble(need_quanCol);

                    ing_table.setItems(ingReportList);
                }else {
                    clearList();
                    System.out.println(menuChoose);
                    readSetChooseIngReport(ingReportList);
                    readSetChooseRecipeReport(recipeReportList);
                }


            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    @FXML
    public void setSelectMenu(Menu menu) {
        selectMenu = menu;
    }

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

    public void setMenuChoose(String menuName) {
        menuChoose = menuName;
    }

    public void clearList() {
        recipeReportList.clear();
        ingReportList.clear();
    }

    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    void handlePdfBtn(ActionEvent event) {

    }

    @FXML
    void handleNewMenuBtn(ActionEvent event) {
        recipeReportList.clear();
        ingReportList.clear();
        readSetRecipeReport(recipeReportList);
        readSetIngReport(ingReportList);
        recipe_table.setItems(recipeReportList);
        ing_table.setItems(ingReportList);
    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void handleChooseMenuBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/Menu_report_select_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }


    //-------------------------------------------- database method -----------------------------------------------------

    private void readSetRecipeReport(ObservableList<RecipeReport> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MenuRecipe.Menu_name, MenuRecipe.Rec_name, MenuRecipe.Recommend_fq, FoodQuan.Total_fq " +
                    "FROM MenuRecipe " +
                    "INNER JOIN FoodQuan ON FoodQuan.MenuRec_ID = MenuRecipe.MenuRec_ID " +
                    "WHERE FoodQuan.Food_date = (SELECT max(FoodQuan.Food_date) FROM FoodQuan); ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                String menu_name = rs.getString("Menu_name");
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");
                double total_fq = rs.getDouble("Total_fq");

                menu_name_text.setText(menu_name);

                RecipeReport rp = new RecipeReport(regName,recommendFq,total_fq);
                list.add(rp);

            }



        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readSetIngReport(ObservableList<IngReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Ingredient.Ing_name, Ingredient.Ing_amount, SUM (IngRec.Ing_quan), Ingredient.Ing_price " +
                    "FROM Ingredient " +
                    "INNER JOIN IngRec ON Ingredient.Ing_name = IngRec.Ing_name " +
                    "GROUP BY Ingredient.Ing_name";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ing_name = rs.getString("Ing_name");
                double ing_amount = rs.getDouble("Ing_amount");
                double ing_quan = rs.getDouble("SUM (IngRec.Ing_quan)");
                double ing_price = rs.getDouble("Ing_price");

                Ingredient newIng = new Ingredient(ing_name,ing_price,ing_amount);
                IngReport newIngReport = new IngReport(newIng,ing_quan,newIng.getIng_price()*ing_quan);
                list.add(newIngReport);

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void readSetChooseRecipeReport(ObservableList<RecipeReport> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MenuRecipe.Menu_name, MenuRecipe.Rec_name, MenuRecipe.Recommend_fq, FoodQuan.Total_fq , FoodQuan.Food_date " +
                    "FROM MenuRecipe " +
                    "INNER JOIN FoodQuan ON FoodQuan.MenuRec_ID = MenuRecipe.MenuRec_ID " +
                    "WHERE  MenuRecipe.Menu_name = ? AND FoodQuan.Food_date = (SELECT max(FoodQuan.Food_date) FROM FoodQuan WHERE FoodQuan.MenuRec_ID = MenuRecipe.MenuRec_ID) ; ";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuChoose);
            rs = ps.executeQuery();

            while(rs.next()) {
                String menu_name = rs.getString("Menu_name");
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");
                double total_fq = rs.getDouble("Total_fq");

                menu_name_text.setText(menu_name);
                RecipeReport rp = new RecipeReport(regName,recommendFq,total_fq);
                list.add(rp);

            }
            recipe_table.setItems(list);


        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readSetChooseIngReport(ObservableList<IngReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT Ingredient.Ing_name, Ingredient.Ing_amount, SUM (IngRec.Ing_quan), Ingredient.Ing_price FROM (( IngRec INNER JOIN MenuRecipe ON IngRec.Rec_name = MenuRecipe.Rec_name ) INNER JOIN Ingredient ON IngRec.Ing_name = Ingredient.Ing_name) WHERE MenuRecipe.Menu_name = ? GROUP BY Ingredient.Ing_name;";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuChoose);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ing_name = rs.getString("Ing_name");
                double ing_amount = rs.getDouble("Ing_amount");
                double ing_quan = rs.getDouble("SUM (IngRec.Ing_quan)");
                double ing_price = rs.getDouble("Ing_price");

                Ingredient newIng = new Ingredient(ing_name,ing_price,ing_amount);
                IngReport newIngReport = new IngReport(newIng,ing_quan,newIng.getIng_price()*ing_quan);
                list.add(newIngReport);

            }
            ing_table.setItems(list);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
