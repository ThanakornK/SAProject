package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import sample.Class.ChangeScene;

import java.io.IOException;

public class Recipe_search_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<?> listView;

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    @FXML
    void getSelectItem(ActionEvent event) {

    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_edit_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleConfirmBtn(ActionEvent event) throws IOException {
        // do sth with database

        // use below method to go back
//        ChangeScene cs = new ChangeScene("../Fxml/Recipe_edit_page.fxml",event);
//        Screen screen = Screen.getPrimary();
//        cs.changeStageAction(screen);

    }


}
