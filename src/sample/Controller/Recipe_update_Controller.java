package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe_update_Controller {

    @FXML
    private TextField add_ing_name, add_ing_quan;

    @FXML
    private Button add_btn, clear_btn, add_rec_btn, back_btn, addRecBox, editRecBtn;

    @FXML
    private TextField add_rec_name_field, add_rec_price_field;

    @FXML
    private TableView<IngRecipe> ingTable;

    @FXML
    private TableColumn<IngRecipe, String> ing_name;

    @FXML
    private TableColumn<IngRecipe, Double> ing_quan;

    private AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList(); // Used to store all recipe in database

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList(); // Used to store all ingredient in database

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList(); // Used to store local ingredient

    private ObservableList<IngRecipe> addIngInfo = FXCollections.observableArrayList();

    private Map<Ingredient,String> ingQuantity = new HashMap<>();   // Hashmap used to store ing_name and ing_quan

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllIng(ingredientList);
                readAllRec(recipeList);

                ing_name.setCellValueFactory(new PropertyValueFactory<>("ingName"));
                ing_quan.setCellValueFactory(new PropertyValueFactory<>("ingQuan"));

            }
        });
    }

    //---------------------------------------------- Common method -------------------------------------------

    private void readAllIng(ObservableList<Ingredient> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Ingredient";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String ingName = rs.getString("Ing_name");
                double ingPrice = rs.getDouble("Ing_price");
                int ingAmount = rs.getInt("Ing_amount");

                Ingredient newIngredient = new Ingredient(ingName,ingPrice,ingAmount);
                list.add(newIngredient);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllRec(ObservableList<Recipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");
                double recPrice = rs.getDouble("Rec_price");

                list.add(new Recipe(recName,recPrice));

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public int isInRecList(String rec_name){

        for(Recipe rec: recipeList){
            if(rec_name.equals(rec.getRec_name())){
                return recipeList.indexOf(rec);
            }
        }
        return -1;
    }

    public int isInIngList(String ing_name){

        for(Ingredient ing: ingredientList){
            if(ing_name.equals(ing.getIng_name())){
                return ingredientList.indexOf(ing);
            }
        }
        return -1;
    }

    public int isInAddList(String ingName){
        if(addIngInfo.isEmpty()){
            return 0;
        }
        for(IngRecipe i: addIngInfo){
            if(ingName.equals(i.getIngName())){
                return -1;
            }
        }
        return 0;
    }

    public void clearTextField(){

        add_rec_name_field.clear();
        add_rec_price_field.clear();
        add_ing_name.clear();
        add_ing_quan.clear();
    }


    @FXML
    void handleClearBtn(ActionEvent event) {

        add_ing_name.clear();
        add_ing_quan.clear();
        add_rec_name_field.clear();
        add_rec_price_field.clear();
    }

    //---------------------------------------------- Change page method ----------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleEditBtn(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_edit_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();

    }

    //---------------------------------------------- Database method -------------------------------------------

    @FXML
    void handleAddBtn(ActionEvent event) { // Add Ing. button

        if(add_ing_name.getText().isEmpty()){
            alertBox.alertERR("err", "กรูณากรอกชื่อวัตถุดิบ");
        }
        else if (add_ing_quan.getText().isEmpty()){
            alertBox.alertERR("err","กรุณากรอกปริมาณวัตถุดิบ");
        }
        else if (Double.parseDouble(add_ing_quan.getText()) <= 0){
            alertBox.alertERR("err", "ปริมาณวัตถุดิบที่ใช้ห้ามติดลบหรือเท่ากับ0");
        }
        else if (isInAddList(add_ing_name.getText()) == -1){
            alertBox.alertERR("err", "มีวัตถุดิบนี้อยู่แล้ว");
        }
        else{

            if(isInIngList(add_ing_name.getText()) == -1){  // If there is no ingredient in database that has the same name in textfield alert user
                alertBox.alertERR("err","ไม่มีวัตถุดิบนี้ในระบบ");
            }

            else{
                IngRecipe temp = new IngRecipe(ingredientList.get(isInIngList(add_ing_name.getText())).getIng_name(), "temp", Double.parseDouble(add_ing_quan.getText()));
                recipeIngList.add(ingredientList.get(isInIngList(add_ing_name.getText())));
                ingQuantity.put(ingredientList.get(isInIngList(add_ing_name.getText())), add_ing_quan.getText());
                add_ing_name.clear();
                add_ing_quan.clear();
                System.out.println("Insert recipe ingredients in to list");

                addIngInfo.add(temp);
                ingTable.setItems(addIngInfo);
                ingTable.refresh();
            }



        }
    }

    @FXML
    void handleRecBtn(ActionEvent event) {           // Insert recipe into database

        if(add_rec_name_field.getText().isEmpty()){
            alertBox.alertERR("err","กรุณากรอกชื่อสูตรอาหาร");
        }

        else if(add_rec_price_field.getText().isEmpty()){
            alertBox.alertERR("err","กรุณากรอกราคาของสูตรอาหาร");
        }

        else if(recipeIngList.isEmpty()){
            alertBox.alertERR("err","ไม่มีวัตุดิบที่ใช้");
        }

        else if(Double.parseDouble(add_rec_price_field.getText()) <= 0){
            alertBox.alertERR("err", "ราคาอาหารห้ามติดลบ");
        }

        else{

            if(isInRecList(add_rec_name_field.getText()) == -1){

                ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                paraCommands.add(new ParaCommand("str", add_rec_name_field.getText()));
                paraCommands.add(new ParaCommand("int", add_rec_price_field.getText()));

                if(dbConnect.insertRecord("INSERT  INTO recipe( rec_name, rec_price) VALUES(?,?)", paraCommands) == 0) {

                    Recipe addRecipe = new Recipe(add_rec_name_field.getText(), Double.parseDouble(add_rec_price_field.getText()));
                    for (Ingredient ing : recipeIngList) {
                        IngRecipe temp = new IngRecipe(ing.getIng_name(), addRecipe.getRec_name() ,Double.parseDouble(ingQuantity.get(ing)));
                        addRecipe.addIngList(temp);
//                        System.out.println("Added " + ing.getIng_name() + " and " + Double.parseDouble(ingQuantity.get(ing)) + " to recently added recipe"); //For debug only
                    }

                    paraCommands.clear();
                    for(IngRecipe inr: addRecipe.getIngList()){        //Insert value in to IngRecipe table in database
                        paraCommands.add(new ParaCommand("str", inr.getIngName()));
                        paraCommands.add(new ParaCommand("str", addRecipe.getRec_name()));
                        paraCommands.add(new ParaCommand("double", String.valueOf(inr.getIngQuan())));

                        if(dbConnect.insertRecord("INSERT  INTO ingrecipe( ing_name, rec_name, ing_quan) VALUES(?,?,?)", paraCommands) == 0) {
                            System.out.println("Insert data into IngRecipe");
                            paraCommands.clear();
                        }
                    }
                    recipeList.add(addRecipe);
                    clearTextField();
                    recipeIngList.clear();
                    ingTable.refresh();
                }


            }
            else{
                alertBox.alertERR("err","สูตรอาหารนี้มีอยู่แล้ว");
            }

            ingQuantity.clear();
            addIngInfo.clear();
            ingTable.refresh();
        }
    }
}