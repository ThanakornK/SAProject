package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Class.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList();

    private Map<Ingredient,String> ingQuantity = new HashMap<>();

    private DBConnect dbConnect = new DBConnect();


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

    //---------------------------------------------- Button method -------------------------------------------

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
    void handleBackBtn(ActionEvent event) {

        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleClearBtn(ActionEvent event) {

        add_ing_name.clear();
        add_ing_quan.clear();
        add_rec_name_field.clear();
        add_rec_price_field.clear();
    }

    @FXML
    void handleEditBtn(ActionEvent event) {

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

                Map<String,String> insertPara = new HashMap<>();
                insertPara.put("str",add_rec_name_field.getText());
                insertPara.put("int",add_rec_price_field.getText());

                if(dbConnect.insertRecord("INSERT  INTO recipe( rec_name, rec_price) VALUES(?,?)",insertPara) == 0){

                    Recipe addRecipe = new Recipe(add_rec_name_field.getText(), Double.parseDouble(add_rec_price_field.getText()));
                    for(Ingredient ing: recipeIngList) {
                        addRecipe.addIngList(ing, Double.parseDouble(ingQuantity.get(ing)));
////                        System.out.println("Added " + ing.getIng_name() + " and " + Double.parseDouble(ingQuantity.get(ing)) + " to recently added recipe");
                    }
////                    for(IngRecipe ir: addRecipe.getIngList()){
////                        System.out.println("IngName: " + ir.getIngName() + " ingQuan: " + ir.getRecQuan());
////                    }
//                    for(IngRecipe inr: addRecipe.getIngList()){
//                        insertPara.replace("str",inr.getIngName());
//                        insertPara.replace("str",add_rec_name_field.getText());
//                        insertPara.replace("double",String.valueOf(inr.getRecQuan()));
//                        if(dbConnect.insertRecord("INSERT  INTO ingrecipe( ing_name, rec_name, ing_quan) VALUES(?,?,?)",insertPara) == 0){
//                            System.out.println("Added data to IngRecipe successful");
//                        }
//                        else{
//                            break;
//                        }
//                    }

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