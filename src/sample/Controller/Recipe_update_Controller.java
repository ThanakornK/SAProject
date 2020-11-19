package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TextField add_ing_name, add_ing_quan, update_ing_name, update_ing_quan;

    @FXML
    private Button add_btn, clear_btn, add_rec_btn, back_btn, addRecBox, editRecBtn, update_btn, delete_btn, add_box_btn, update_box_btn;

    @FXML
    private TextField add_rec_name_field;

    @FXML
    private TableView<IngRecipe> ingTable;

    @FXML
    private TableColumn<IngRecipe, String> ing_name;

    @FXML
    private TableColumn<IngRecipe, Double> ing_quan;

    @FXML
    private TableColumn<IngRecipe, IngRecipe> delCol;

    private AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList(); // Used to store all recipe in database

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList(); // Used to store all ingredient in database

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList(); // Used to store local ingredient

    private ObservableList<IngRecipe> addIngInfo = FXCollections.observableArrayList();

    private Map<Ingredient, String> ingQuantity = new HashMap<>();   // Hashmap used to store ing_name and ing_quan

    @FXML
    public void initialize() {
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
                    System.out.println(String.format("%.2f is Total cost of %s in %s recipe",temp.getTotalCost(), temp.getIngName(), readRec.getRec_name()));
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public int isInRecList(String rec_name) {

        for (Recipe rec : recipeList) {
            if (rec_name.equals(rec.getRec_name())) {
                return recipeList.indexOf(rec);
            }
        }
        return -1;
    }

    public int isInIngList(String ing_name) {

        for (Ingredient ing : ingredientList) {
            if (ing_name.equals(ing.getIng_name())) {
                return ingredientList.indexOf(ing);
            }
        }
        return -1;
    }

    public int isInAddList(String ingName) {
        if (addIngInfo.isEmpty()) {
            return 0;
        }
        for (IngRecipe i : addIngInfo) {
            if (ingName.equals(i.getIngName())) {
                return -1;
            }
        }
        return 0;
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

    public void clearTextField() {

        add_rec_name_field.clear();
        add_ing_name.clear();
        add_ing_quan.clear();
    }


    @FXML
    void handleClearBtn(ActionEvent event) {

        add_ing_name.clear();
        add_ing_quan.clear();
        add_rec_name_field.clear();
        addIngInfo.clear();
    }

    public void getSelectedRow() {

        if (update_btn.isVisible() && (ingTable.getSelectionModel().getSelectedItem() != null)) {
            System.out.println(ingTable.getSelectionModel().getSelectedItem().getIngName() + " is selected");
            update_ing_name.setText(ingTable.getSelectionModel().getSelectedItem().getIngName());
            update_ing_quan.setText(String.valueOf(ingTable.getSelectionModel().getSelectedItem().getIngQuan()));
        }

    }

    @FXML
    void handleDeleteBtn() {

        for (IngRecipe i : addIngInfo) {
            if (i.getIngName().equals(update_ing_name.getText())) {
                addIngInfo.remove(i);
                break;
            } else {
                continue;
            }
        }
        update_ing_name.clear();
        update_ing_quan.clear();

    }

    @FXML
    void handleUpdateBtn() {
        if((!update_ing_quan.getText().isEmpty()) && (isDouble(update_ing_quan.getText()) == 0) && ( Double.parseDouble(update_ing_quan.getText()) > 0) ) {
            for (IngRecipe i : addIngInfo) {
                if (i.getIngName().equals(update_ing_name.getText())) {
                    addIngInfo.get(addIngInfo.indexOf(i)).setIngQuan(Double.parseDouble(update_ing_quan.getText()));
                    ingTable.refresh();
                    break;
                } else {
                    continue;
                }
            }
        } else {
            alertBox.normalAlert("err", "กรอกข้อมูลไม่ถูกต้อง");
        }
        update_ing_name.clear();
        update_ing_quan.clear();

    }

    //---------------------------------------------- Change page method ----------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_page.fxml", event);
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

    @FXML
    void changeUpdateBox() {

        add_ing_name.clear();
        add_ing_quan.clear();
        add_box_btn.setStyle("-fx-background-color: #79DC63");
        update_box_btn.setStyle("-fx-background-color: lightgreen");
        update_btn.setDisable(false);
        update_btn.setVisible(true);
        delete_btn.setDisable(false);
        delete_btn.setVisible(true);
        add_btn.setDisable(true);
        add_btn.setVisible(false);
        add_ing_name.setDisable(true);
        add_ing_name.setVisible(false);
        add_ing_quan.setDisable(true);
        add_ing_quan.setVisible(false);
        update_ing_name.setDisable(false);
        update_ing_name.setVisible(true);
        update_ing_quan.setDisable(false);
        update_ing_quan.setVisible(true);
        add_ing_name.clear();
        add_ing_quan.clear();

    }

    @FXML
    void changeAddBox() {

        add_ing_name.clear();
        add_ing_quan.clear();
        add_box_btn.setStyle("-fx-background-color: lightgreen");
        update_box_btn.setStyle("-fx-background-color: #79DC63");
        update_btn.setDisable(true);
        update_btn.setVisible(false);
        delete_btn.setDisable(true);
        delete_btn.setVisible(false);
        add_btn.setDisable(false);
        add_btn.setVisible(true);
        add_ing_name.setDisable(false);
        add_ing_name.setVisible(true);
        add_ing_quan.setDisable(false);
        add_ing_quan.setVisible(true);
        update_ing_name.setDisable(true);
        update_ing_name.setVisible(false);
        update_ing_quan.setDisable(true);
        update_ing_quan.setVisible(false);
        update_ing_name.clear();
        update_ing_quan.clear();

    }


    //---------------------------------------------- Database method -------------------------------------------

    @FXML
    void handleAddIngBtn(ActionEvent event) { // Add Ing. button

        if (isDouble(add_ing_quan.getText()) == 0) {

            if (add_ing_name.getText().isEmpty()) {
                alertBox.normalAlert("err", "กรูณากรอกชื่อวัตถุดิบ");
            } else if (add_ing_quan.getText().isEmpty()) {
                alertBox.normalAlert("err", "กรุณากรอกปริมาณวัตถุดิบ");
            } else if (Double.parseDouble(add_ing_quan.getText()) <= 0) {
                alertBox.normalAlert("err", "ปริมาณวัตถุดิบที่ใช้ห้ามติดลบหรือเท่ากับ0");
            } else if (isInAddList(add_ing_name.getText()) == -1) {
                alertBox.normalAlert("err", "มีวัตถุดิบนี้อยู่แล้ว");
            } else {

                if (isInIngList(add_ing_name.getText()) == -1) {  // If there is no ingredient in database that has the same name in textfield alert user
                    alertBox.normalAlert("err", "ไม่มีวัตถุดิบนี้ในระบบ");
                } else {
                    IngRecipe temp = new IngRecipe(ingredientList.get(isInIngList(add_ing_name.getText())).getIng_name(), "temp", Double.parseDouble(add_ing_quan.getText()));
                    recipeIngList.add(ingredientList.get(isInIngList(add_ing_name.getText())));
                    ingQuantity.put(ingredientList.get(isInIngList(add_ing_name.getText())), add_ing_quan.getText());
                    add_ing_name.clear();
                    add_ing_quan.clear();

                    addIngInfo.add(temp);
                    ingTable.setItems(addIngInfo);
                    ingTable.refresh();
                }
            }
        } else {
            alertBox.normalAlert("err", "ข้อมูลที่กรอกไม่ถูกต้อง");
        }
    }

    @FXML
    void handleAddRecBtn(ActionEvent event) {           // Insert recipe into database

        if (alertBox.alertConfirm("ยืนยันการเพิ่มสูตรอาหารหรือไม่") == 0) {

                if (add_rec_name_field.getText().isEmpty()) {
                    alertBox.normalAlert("err", "กรุณากรอกชื่อสูตรอาหาร");
                } else if (addIngInfo.isEmpty()) {
                    alertBox.normalAlert("err", "ไม่มีวัตุดิบที่ใช้");
                } else {


                    if (isInRecList(add_rec_name_field.getText()) == -1) {

                        ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                        paraCommands.add(new ParaCommand("str", add_rec_name_field.getText()));

                        if (dbConnect.insertRecord("INSERT  INTO recipe( rec_name ) VALUES(?)", paraCommands) == 0) {

                            Recipe addRecipe = new Recipe(add_rec_name_field.getText());
                            for (IngRecipe ing : addIngInfo) {
                                IngRecipe temp = new IngRecipe(ing.getIngName(), addRecipe.getRec_name(), ing.getIngQuan());
                                addRecipe.addIngList(temp);
                            }

                            paraCommands.clear();
                            for (IngRecipe inr : addRecipe.getIngList()) {        //Insert value in to IngRecipe table in database
                                paraCommands.add(new ParaCommand("str", inr.getIngName()));
                                paraCommands.add(new ParaCommand("str", addRecipe.getRec_name()));
                                paraCommands.add(new ParaCommand("double", String.valueOf(inr.getIngQuan())));

                                if (dbConnect.insertRecord("INSERT  INTO IngRec ( ing_name, rec_name, ing_quan) VALUES(?,?,?)", paraCommands) == 0) {
                                    System.out.println("Insert data into IngRecipe");
                                    paraCommands.clear();
                                }
                            }
                            recipeList.add(addRecipe);
                            clearTextField();
                            recipeIngList.clear();
                            ingTable.refresh();
                        }


                    } else {
                        alertBox.normalAlert("err", "สูตรอาหารนี้มีอยู่แล้ว");
                    }
                }
                ingQuantity.clear();
                addIngInfo.clear();
                ingTable.refresh();
        } else {
            System.out.println("Terminate");
        }

    }
}