package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;
import java.util.function.DoubleConsumer;

public class Menu_search_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<?> listView;

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    public void initialize(){
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

        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();

    }



    //-------------------------------------------- database method -----------------------------------------------------


    @FXML
    void handleConfirmBtn(ActionEvent event) throws IOException {
        // do sth with database

        // use below method to go back

//        ChangeScene cs = new ChangeScene("../Fxml/Menu_edit_page.fxml",event);
//        Screen screen = Screen.getPrimary();
//        cs.changeStageAction(screen);
    }


}
