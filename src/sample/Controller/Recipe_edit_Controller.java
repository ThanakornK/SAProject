package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;

public class Recipe_edit_Controller {

    @FXML
    private Button back_btn, add_rec_btn, recSearchBtn;

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------



    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

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

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_search_select.fxml");
        cs.newWindow();

    }

    //-------------------------------------------- database method -----------------------------------------------------






}
