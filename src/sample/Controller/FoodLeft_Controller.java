package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FoodLeft_Controller {

    @FXML
    private Button FoodPlanBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private AnchorPane showMenu_con;

    @FXML
    private TableView<?> recPlanQuan_table;

    @FXML
    private TableView<?> ingPlaQuan_table;

    @FXML
    private Button calculateFoodQuan_btn;

    @FXML
    private Button reportBtn;

    @FXML
    private DatePicker dateSale;

    @FXML
    private TextField menu_name_field;

    //----------------------------------------- normal method ----------------------------------------------------------

    @FXML
    void handleCalculate(ActionEvent event) {

    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleFoodPlanBox(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/FoodQuan_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void handleReport(ActionEvent event) {

    }

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    public void handleSaveLeftBtn(ActionEvent event) {

    }


}
