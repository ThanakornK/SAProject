package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.Menu;
import sample.Class.Recipe;

import java.util.ArrayList;
import java.util.List;

public class Menu_sale_Controller {

    @FXML
    private Button back_btn;

    @FXML
    private Text menu_name;

    @FXML
    private TableView<?> salesTable;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TableColumn<?, ?> recCol;

    @FXML
    private TableColumn<?, ?> salesCol;

    @FXML
    private TableColumn<?, ?> foodQuanCol;

    @FXML
    private TableColumn<?, ?> leftCol;

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Recipe ex1 = new Recipe("Egg fried rice",35);
                Recipe ex2 = new Recipe("Gang Green Sweet",45);
                List<Recipe> recipeList = new ArrayList<>();
                recipeList.add(ex1); recipeList.add(ex2);

                Menu menu = new Menu("example", recipeList);
                menu_name.setText(menu.getMenu_name());
            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------



    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
    }

    //-------------------------------------------- database method -----------------------------------------------------



}
