package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;

public class FoodQuan_Controller {

    @FXML
    private Button FoodPlanBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private AnchorPane selectMenu_con;

    @FXML
    private ListView<?> menuListView;

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
    private TableView<?> recPlanQuan_table;

    @FXML
    private TableView<?> ingPlaQuan_table;

    @FXML
    private DatePicker dateSale;

    @FXML
    private TextField menu_name_field;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------



    //----------------------------------------- normal method ----------------------------------------------------------



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
        ChangeScene cs = new ChangeScene("../Fxml/FoodMenu_search_select_page.fxml");
        cs.newWindow();

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
        ChangeScene cs = new ChangeScene("../Fxml/RecommendFoodQuan_page.fxml");
        cs.newWindow();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleMenuConfirm(ActionEvent event) {

    }









}