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

public class Recipe_edit_Controller{

    @FXML
    private Button back_btn, add_rec_btn, recSearchBtn;

    @FXML
    private TextField add_rec_name_field, add_rec_price_field;

    @FXML
    private TextField add_ing_name, add_ing_quan;

    @FXML
    private TableView<IngRecipe> ingTable;

    @FXML
    private TableColumn<IngRecipe, String> ing_name;

    @FXML
    private TableColumn<IngRecipe, Double> ing_quan;

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<IngRecipe> selectRecIngList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList(); // Used to store local ingredient

    private ObservableList<IngRecipe> tempList = FXCollections.observableArrayList();

    Recipe selectRecipe;

    AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllIng(ingredientList);
                readAllRec(recipesList);

                ing_name.setCellValueFactory(new PropertyValueFactory<>("ingName"));
                ing_quan.setCellValueFactory(new PropertyValueFactory<>("ingQuan"));


                if(selectRecipe != null){
                    add_rec_price_field.setDisable(false);
                    add_ing_name.setDisable(false);
                    add_ing_quan.setDisable(false);
                    add_rec_name_field.setText(selectRecipe.getRec_name());

                    add_rec_price_field.setText(String.valueOf(selectRecipe.getRec_price()));

                    for(IngRecipe i: selectRecipe.getIngList()){
                        selectRecIngList.add(i);
                    }
                    ingTable.setItems(selectRecIngList);
                    ingTable.refresh();
                }
                else{
                    System.out.println("null naja");
                }

            }
        });
    }

    //----------------------------------------- Normal method ----------------------------------------------------------

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
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");
                double recPrice = rs.getDouble("Rec_price");

                Recipe readRec = new Recipe(recName, recPrice);

                String sql2 = String.format("SELECT * FROM IngRecipe WHERE Rec_name = '%s'",readRec.getRec_name());
                ps2 = con.prepareStatement(sql2);
                rs2 = ps2.executeQuery();
                while (rs2.next()){
                    String ingName = rs2.getString("Ing_name");
                    String rec_Name = rs2.getString("Rec_name");
                    double ingQuan = rs2.getDouble("Ing_quan");
                    System.out.println(String.format("Added %s into %s", ingName, rec_Name));
                    IngRecipe temp = new IngRecipe(ingName, recName, ingQuan);
                    temp.calculateTotalCost(ingredientList);
                    System.out.println(String.format("%.2f is Total cost of %s in %s recipe",temp.getTotalCost(), temp.getIngName(), readRec.getRec_name()));
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void setSelectRecipe(Recipe rec){
        this.selectRecipe = rec;
    }

    public int isInSelectRecIngList(String ingName) {
        if (selectRecIngList.isEmpty()) {
            return 0;
        }
        for (IngRecipe i : selectRecIngList) {
            if (ingName.equals(i.getIngName())) {
                return -1;
            }
        }
        return 0;
    }

    public int isInIngList(String ing_name) {

        for (Ingredient ing : ingredientList) {
            if (ing_name.equals(ing.getIng_name())) {
                return ingredientList.indexOf(ing);
            }
        }
        return -1;
    }

    public int isInRecList(String rec_name) {

        for (Recipe rec : recipesList) {
            if (rec_name.equals(rec.getRec_name())) {
                return recipesList.indexOf(rec);
            }
        }
        return -1;
    }

    public int isDouble(String input) {
        try {
            Double.parseDouble(input);
            return 0;
        } catch (NumberFormatException e) {
            return 1;
//            alertBox.alertERR("err", "กรอกข้อมูลไม่ถูกต้อง");
        }
    }

    //---------------------------------------- Normal button method ----------------------------------------------------

    @FXML
    public void handleClearBtn(){
        add_rec_name_field.clear();
        add_rec_price_field.clear();
        add_ing_name.clear();
        add_ing_quan.clear();
        add_ing_name.setDisable(true);
        add_ing_quan.setDisable(true);
        selectRecIngList.clear();
        selectRecipe = null;
    }



    //--------------------------------------- Change page method -------------------------------------------------------

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    @FXML
    public void handleAddPageBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleSearchBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_search_select.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleAddIngBtn(ActionEvent event) { // Add Ing. button

        if (isDouble(add_ing_quan.getText()) == 0) {

            if (add_ing_name.getText().isEmpty()) {
                alertBox.alertERR("err", "กรูณากรอกชื่อวัตถุดิบ");
            } else if (add_ing_quan.getText().isEmpty()) {
                alertBox.alertERR("err", "กรุณากรอกปริมาณวัตถุดิบ");
            } else if (Double.parseDouble(add_ing_quan.getText()) <= 0) {
                alertBox.alertERR("err", "ปริมาณวัตถุดิบที่ใช้ห้ามติดลบหรือเท่ากับ0");
            } else if (isInSelectRecIngList(add_ing_name.getText()) == -1) {
                alertBox.alertERR("err", "มีวัตถุดิบนี้อยู่แล้ว");
            } else {

                if (isInIngList(add_ing_name.getText()) == -1) {  // If there is no ingredient in database that has the same name in textfield alert user
                    alertBox.alertERR("err", "ไม่มีวัตถุดิบนี้ในระบบ");
                } else {
                    IngRecipe temp = new IngRecipe(ingredientList.get(isInIngList(add_ing_name.getText())).getIng_name(), selectRecipe.getRec_name(), Double.parseDouble(add_ing_quan.getText()));
                    recipeIngList.add(ingredientList.get(isInIngList(add_ing_name.getText())));
//                    ingQuantity.put(ingredientList.get(isInIngList(add_ing_name.getText())), add_ing_quan.getText());
                    add_ing_name.clear();
                    add_ing_quan.clear();

                    tempList.add(temp);
                    selectRecIngList.add(temp);
                    ingTable.refresh();
                }
            }
        } else {
            alertBox.alertERR("err", "ข้อมูลที่กรอกไม่ถูกต้อง");
        }
    }


    @FXML
    void handleEditRecBtn(ActionEvent event){

        if (alertBox.alertConfirm("ยืนยันการแก้ไขสูตรอาหารหรือไม่") == 0) {

            if (isDouble(add_rec_price_field.getText()) == 0) {

                if (add_rec_name_field.getText().isEmpty()) {
                    alertBox.alertERR("err", "กรุณากรอกชื่อสูตรอาหาร");
                } else if (add_rec_price_field.getText().isEmpty()) {
                    alertBox.alertERR("err", "กรุณากรอกราคาของสูตรอาหาร");
                } else if (selectRecIngList.isEmpty()) {
                    alertBox.alertERR("err", "ไม่มีวัตุดิบที่ใช้");
                } else if (Double.parseDouble(add_rec_price_field.getText()) <= 0) {
                    alertBox.alertERR("err", "ราคาอาหารห้ามติดลบ");
                } else {

                    ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                    paraCommands.add(new ParaCommand("int", add_rec_price_field.getText()));
                    paraCommands.add(new ParaCommand("str", add_rec_name_field.getText()));

                    if (dbConnect.updateRecord("UPDATE Recipe set Rec_price = ? WHERE Rec_name = ? ", paraCommands) == 0) {
                        System.out.println("Update Successful");
                    }

                    if (!tempList.isEmpty()){
                        paraCommands.clear();
                        for (IngRecipe i : tempList) {
                            paraCommands.add(new ParaCommand("str", i.getIngName()));
                            paraCommands.add(new ParaCommand("str", selectRecipe.getRec_name()));
                            paraCommands.add(new ParaCommand("double", String.valueOf(i.getIngQuan())));
                            if (dbConnect.insertRecord("INSERT  INTO IngRecipe( Ing_Name, Rec_Name, Ing_Quan) VALUES(?,?,?)", paraCommands) == 0) {
                                paraCommands.clear();
                            }
                        }
                        tempList.clear();
                    }

                }
            } else {
                alertBox.alertERR("err", "กรอกข้อมูลไม่ถูกต้อง");
            }
        } else {
            System.out.println("Terminate");
        }
    }





}
