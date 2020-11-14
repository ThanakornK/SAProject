package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Class.DBConnect;
import sample.Class.Ingredient;

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
    private TableView<?> ingTable;

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    //---------------------------------------------- Button method -------------------------------------------

    @FXML
    void handleAddBtn(ActionEvent event) { // Add Ing. button

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
    void handleRecBtn(ActionEvent event) { // Confirm button

    }

}