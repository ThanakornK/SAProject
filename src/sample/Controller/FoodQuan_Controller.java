package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private ObservableList<RecipeReport> copyQuan = FXCollections.observableArrayList();

    private DBConnect dbConnect = new DBConnect();
    private String foodDate;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dateSale.setValue(LocalDate.now());
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

    @FXML
    private void getDateAction(ActionEvent event) {
        if (dateSale.getValue() != null) {
            Locale lc = new Locale("en","EN");
            String currentDate = new SimpleDateFormat("yyyy-MM-dd",lc).format(new Date());
            String selectDate = dateSale.getValue().toString();
            if (isDateOfInterestValid("yyyy-MM-dd",currentDate,selectDate)){
                foodDate = currentDate;
            }else{
                AlertBox alertBox = new AlertBox();
                alertBox.alertERR("err","กรุณาเลือกเวลาปัจจุบันหรืออนาคต");
                dateSale.setValue(LocalDate.now());
            }

        } else {
            AlertBox alertBox = new AlertBox();
            alertBox.alertERR("err","กรุณาเลือกวันที่ขาย");
        }
    }

    public static boolean isDateOfInterestValid(String dateformat, String currentDate, String dateOfInterest) {

        String format = dateformat;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date cd = null;
        Date doi = null;

        try {
            cd = sdf.parse(currentDate);
            doi = sdf.parse(dateOfInterest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = cd.getTime() - doi.getTime();
        int diffDays = (int) (diff / (24 * 1000 * 60 * 60));

        if (diffDays > 0) {
            return false;
        } else {
            return true;
        }
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

    public void setCopyQuan(ObservableList<RecipeReport> list) {
        copyQuan = list;
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
        ChangeScene cs = new ChangeScene("../Fxml/RecommendFoodQuan_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleMenuConfirm(ActionEvent event) {
        insertFoodQuan();
    }

    public void readSetRecipe_tb(ObservableList<RecipeReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * " +
                    "FROM MenuRecipe " +
                    "WHERE MenuRecipe.Menu_name = ? ;";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuSelect);
            rs = ps.executeQuery();

            while(rs.next()) {
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");
                int recId = rs.getInt("MenuRec_ID");
                RecipeReport rp = new RecipeReport(regName,recommendFq,0.0);
                rp.setID(recId);
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

    public void insertFoodQuan(){
        for (RecipeReport rp: recList) {
            ArrayList<ParaCommand> paraCommands = new ArrayList<>();
            System.out.println(foodDate+rp.getID()+ rp.getTotal_fqReport());
            paraCommands.add(new ParaCommand("str", foodDate));
            paraCommands.add(new ParaCommand("int", String.valueOf(rp.getID())));
            paraCommands.add(new ParaCommand("double", String.valueOf(rp.getTotal_fqReport()))); // get total foodquan

            if(dbConnect.insertRecord("INSERT INTO FoodQuan ( Food_date, MenuRec_ID, Total_fq) VALUES (?,?,?)", paraCommands) == 0){
                System.out.println("Insert success");
                paraCommands.clear();
            }
        }

    }







}