package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Class.AlertBox;
import sample.Class.Ingredient;
import sample.Class.DBConnect;
import sample.Class.Menu;
import sample.Class.ParaCommand;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Ingredient_Controller {
    @FXML
    private TextField add_ing_name, add_ing_price, add_ing_amount, update_ing_name, update_ing_price, update_ing_amount;

    @FXML
    private Button add_btn, delete_btn, search_btn, update_btn, recipe_page_btn, add_box_btn, update_box_btn;

    @FXML
    private TableView<Ingredient> ingredient_table;

    @FXML
    private TableColumn<Ingredient, String> ing_nameCol;

    @FXML
    private TableColumn<Ingredient, Double> ing_priceCol;

    @FXML
    private TableColumn<Ingredient, Integer> ing_amountCol;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                readAll(ingredientList);

                ing_nameCol.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("Ing_name"));
                ing_priceCol.setCellValueFactory(new PropertyValueFactory<Ingredient, Double>("Ing_price"));

                ing_priceCol.setCellFactory(tc -> new TableCell<Ingredient, Double>() {
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

                ing_amountCol.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("Ing_amount"));

                FilteredList<Ingredient> ingFilterList = new FilteredList<>(ingredientList, p -> true);
                update_ing_name.textProperty().addListener((observable, oldValue, newValue) -> {
                    ingFilterList.setPredicate(menu -> {
                        if (newValue == null || newValue.isEmpty()){
                            return true;
                        }

                        if (menu.getIng_name().indexOf(newValue) != -1){
                            return true;
                        } else{
                            return false;
                        }
                    });

                });

                // wrap filter list to sorted list
                SortedList<Ingredient> ingSortedList = new SortedList<>(ingFilterList);

                ingredient_table.setItems(ingSortedList);

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

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

    public void clearTextField(){
        add_ing_name.clear();
        add_ing_price.clear();
        add_ing_amount.clear();

        update_ing_name.clear();
        update_ing_price.clear();
        update_ing_amount.clear();

        update_ing_price.setDisable(true);
        update_ing_amount.setDisable(true);

        update_ing_name.setEditable(true);
        update_ing_price.setEditable(false);
        update_ing_price.setEditable(false);

    }

    public void getSelectedRow(){
        if (update_btn.isVisible() && (ingredient_table.getSelectionModel().getSelectedItem() != null)){
            update_ing_name.setText(ingredient_table.getSelectionModel().getSelectedItem().getIng_name());
            update_ing_price.setText(String.format("%.2f",ingredient_table.getSelectionModel().getSelectedItem().getIng_price()));
            update_ing_amount.setText(String.valueOf(ingredient_table.getSelectionModel().getSelectedItem().getIng_amount()));

            update_ing_name.setEditable(false);
            update_ing_price.setDisable(false);
            update_ing_amount.setDisable(false);
            update_ing_price.setEditable(true);
            update_ing_amount.setEditable(true);
        }
    }

    public int isInList(String ingName) {                   // method to find index from ing name
        for (Ingredient ing: ingredientList) {
            if (ingName.equals(ing.getIng_name())) {
                return ingredientList.indexOf(ing);
            }
        }
        return -1;
    }

    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    public void handleClearBtn() {
        clearTextField();

        update_ing_price.setEditable(false);
        update_ing_amount.setEditable(false);

        update_ing_price.setDisable(true);
        update_ing_amount.setDisable(true);

    }

    @FXML
    public void handleSearchBtn() {
        if (update_ing_name.getText().isEmpty()){                   // check update_ing_name is Empty?
            alertBox.alertERR("err","กรุณากรอกชื่อวัตถุดิบที่ต้องการค้นหา");

        } else {
            if (isInList(update_ing_name.getText()) != -1) {                                                                // show ing data and set update field can edit
                int indexSearch = isInList(update_ing_name.getText());
                update_ing_price.setText(String.valueOf(ingredientList.get(indexSearch).getIng_price()));
                update_ing_amount.setText(String.valueOf(ingredientList.get(indexSearch).getIng_amount()));

                update_ing_price.setEditable(true);
                update_ing_amount.setEditable(true);
                update_ing_price.setDisable(false);
                update_ing_amount.setDisable(false);

                update_ing_name.setEditable(false);

            } else {                                                        // check if ing name don't exist
                alertBox.alertERR("err","ไม่พบวัตถุดิบ");
                clearTextField();

            }

        }
    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    public void handleHomeBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();

    }

    public void changeUpdateBox(){                     // just change field to update field
        clearTextField();
        add_box_btn.setStyle("-fx-background-color: #79DC63");
        update_box_btn.setStyle("-fx-background-color: lightgreen");

        update_ing_name.setVisible(true);
        update_ing_price.setVisible(true);
        update_ing_amount.setVisible(true);

        update_ing_name.setEditable(true);
        update_ing_price.setEditable(false);
        update_ing_amount.setEditable(false);

        update_ing_price.setDisable(true);
        update_ing_amount.setDisable(true);

        add_ing_name.setEditable(false);
        add_ing_price.setEditable(false);
        add_ing_amount.setEditable(false);

        add_ing_name.setVisible(false);
        add_ing_price.setVisible(false);
        add_ing_amount.setVisible(false);

        add_btn.setVisible(false);
        delete_btn.setVisible(true);
        update_btn.setVisible(true);
    }

    public void changeAddBox(){                        // just change field to add field
        clearTextField();
        add_box_btn.setStyle("-fx-background-color: lightgreen");
        update_box_btn.setStyle("-fx-background-color: #79DC63");

        add_ing_name.setEditable(true);
        add_ing_price.setEditable(true);
        add_ing_amount.setEditable(true);

        add_ing_name.setVisible(true);
        add_ing_price.setVisible(true);
        add_ing_amount.setVisible(true);

        update_ing_name.setVisible(false);
        update_ing_price.setVisible(false);
        update_ing_amount.setVisible(false);

        update_ing_name.setEditable(false);
        update_ing_price.setEditable(false);
        update_ing_amount.setEditable(false);

        delete_btn.setVisible(false);
        update_btn.setVisible(false);
        add_btn.setVisible(true);

    }

    @FXML
    public void goto_recipe_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Recipe_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    public void go_menu_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    public void deleteRecord(){

        if (update_ing_name.getText().isEmpty()) {                              // check if name field empty

            alertBox.alertERR("err","ยังไม่มีการเลือกข้อมูลที่ต้องการลบ");

        }else {

            if (isInList(update_ing_name.getText()) != -1) {                    // check if data don't exist
                if (alertBox.alertConfirm("คุณต้องการลบข้อมูลหรือไม่") == 0){

                    if (dbConnect.deleteRecord("delete from Ingredient WHERE Ing_name = ?","str", update_ing_name.getText()) == 0){
                        ObservableList<Ingredient> copyList,selectedRow;
                        copyList = ingredient_table.getItems();
                        selectedRow = ingredient_table.getSelectionModel().getSelectedItems();
                        selectedRow.forEach(copyList::remove);

                        ingredient_table.refresh();
                        clearTextField();

                    }
                }

            } else {

                alertBox.alertERR("err","ไม่พบวัตถุดิบ");

            }

        }

    }

    @FXML
    public void insertRecord() {
            if (add_ing_name.getText().isEmpty() || add_ing_price.getText().isEmpty() || add_ing_amount.getText().isEmpty()){
                alertBox.alertERR("err", "กรุณากรอกข้อมูลให้ครบถ้วน");

            } else{
                if (isInList(add_ing_name.getText()) == -1) {                   // check if data already exist

                    ArrayList<ParaCommand> paraCommands = new ArrayList<>();

                    paraCommands.add(new ParaCommand("str",add_ing_name.getText()));
                    paraCommands.add(new ParaCommand("double",add_ing_price.getText()));
                    paraCommands.add(new ParaCommand("int",add_ing_amount.getText()));

                    if (dbConnect.insertRecord("INSERT  INTO ingredient( ing_name, ing_price, ing_amount) VALUES(?,?,?) ",paraCommands) == 0){   // check insert is not error

                        Ingredient addIngredient = new Ingredient( add_ing_name.getText(), Double.parseDouble(add_ing_price.getText()), Integer.parseInt(add_ing_amount.getText()));
                        ingredientList.add(addIngredient);
                        clearTextField();
                        ingredient_table.refresh();
                    }

                }else {
                    alertBox.alertERR("err","วัตถุดิบนี้มีอยู่แล้ว");
                }
            }

    }

    @FXML
    public void updateRecord() {
        try {

            if (update_ing_name.getText().isEmpty()){
                alertBox.alertERR("err","กรุณากรอกชื่อวัตถุดิบที่ต้องการอัพเดต");

            }else if (update_ing_price.getText().isEmpty() && update_ing_amount.getText().isEmpty()) {
                alertBox.alertERR("err","กรุณากรอกข้อมูลวัตถุดิบให้ครบถ้วน");

            }else if (isInList(update_ing_name.getText()) == -1) {

                alertBox.alertERR("err","ไม่มีข้อมูลดังกล่าวอยู่ในฐานข้อมูล");

            }else if (ingredientList.get(isInList(update_ing_name.getText()))
                    .equals(new Ingredient(update_ing_name.getText(),Double.parseDouble(update_ing_price.getText()),Integer.parseInt(update_ing_amount.getText())))){

                alertBox.alertERR("err","ข้อมูลไม่มีการเปลี่ยนแปลง");

            } else {

                ArrayList<ParaCommand> paraCommands = new ArrayList<>();

                paraCommands.add(new ParaCommand("double",update_ing_price.getText()));
                paraCommands.add(new ParaCommand("int",update_ing_amount.getText()));
                paraCommands.add(new ParaCommand("str",update_ing_name.getText()));  // last parameter must be pk

                if (dbConnect.updateRecord("UPDATE Ingredient set Ing_price = ?, Ing_amount = ? WHERE Ing_name = ? ",paraCommands) == 0) {  // check update is not error

                    Ingredient newIng = new Ingredient(update_ing_name.getText(), Double.parseDouble(update_ing_price.getText()), Integer.parseInt(update_ing_amount.getText()));
                    ingredientList.set(isInList(update_ing_name.getText()),newIng);
                    ingredient_table.refresh();
                    clearTextField();

                }
            }

        } catch (NumberFormatException e) {

            alertBox.alertERR("err","ข้อมูลที่กรอกไม่ถูกต้อง");

        }

    }

}
