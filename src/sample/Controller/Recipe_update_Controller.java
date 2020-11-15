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
    private TableView<Ingredient> ingTable;

    @FXML
    private TableColumn<Ingredient, String> ing_name;

    @FXML
    private TableColumn<Ingredient, Integer> ing_amount;

    private AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList(); // Used to store all recipe in database

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList(); // Used to store all ingredient in database

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList(); // Used to store local ingredient name and quantity

    private Map<Ingredient,String> ingQuantity = new HashMap<>();   // Hashmap used to store ing_name and ing_quan

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAll(ingredientList);

                ing_name.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("ing_name"));
                ing_amount.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("ing_amount"));

                ingTable.setItems(recipeIngList);

            }
        });
    }

    //---------------------------------------------- Common method -------------------------------------------

    private void readAll(ObservableList<Ingredient> list){
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

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
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
        else{

            if(isInIngList(add_ing_name.getText()) == -1){  // If there is no ingredient in database that has the same name in textfield alert user
                alertBox.alertERR("err","ไม่มีวัตถุดิบนี้ในระบบ");
            }

            else{
                recipeIngList.add(ingredientList.get(isInIngList(add_ing_name.getText())));
                ingQuantity.put(ingredientList.get(isInIngList(add_ing_name.getText())), add_ing_quan.getText());
                add_ing_name.clear();
                add_ing_quan.clear();
                System.out.println("Insert recipe ingredients in to list");
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

        else{

            if(isInRecList(add_rec_name_field.getText()) == -1){

                ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                paraCommands.add(new ParaCommand("str", add_rec_name_field.getText()));
                paraCommands.add(new ParaCommand("int", add_rec_price_field.getText()));

                if(dbConnect.insertRecord("INSERT  INTO recipe( rec_name, rec_price) VALUES(?,?)", paraCommands) == 0) {

                    Recipe addRecipe = new Recipe(add_rec_name_field.getText(), Double.parseDouble(add_rec_price_field.getText()));
                    for (Ingredient ing : recipeIngList) {
                        addRecipe.addIngList(ing, Double.parseDouble(ingQuantity.get(ing)));
//                        System.out.println("Added " + ing.getIng_name() + " and " + Double.parseDouble(ingQuantity.get(ing)) + " to recently added recipe"); //For debug only
                    }
                    paraCommands.clear();
                    for(IngRecipe inr: addRecipe.getIngList()){        //Insert value in to IngRecipe table in database
                        paraCommands.add(new ParaCommand("str", inr.getIngName()));
                        paraCommands.add(new ParaCommand("str", addRecipe.getRec_name()));
                        paraCommands.add(new ParaCommand("double", String.valueOf(inr.getIngQuan())));

                        if(dbConnect.insertRecord("INSERT  INTO ingrecipe( ing_name, rec_name, ing_quan) VALUES(?,?,?)", paraCommands) == 0) {
                            System.out.println("Insert data into IngRecipe");
                        }
                    }
                    recipeList.add(addRecipe);
                    clearTextField();
                }


            }
            else{
                alertBox.alertERR("err","สูตรอาหารนี้มีอยู่แล้ว");
            }
            recipeIngList.clear();
            ingQuantity.clear();
        }
    }
}