package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;

public class Menu_edit_Controller {

    @FXML
    private Button addMenuBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private TextField add_menu_name_field;

    @FXML
    private Button add_Menu_btn;

    @FXML
    private Button back_btn, menuSearchBtn;

    @FXML
    private ListView<?> recipe_list_view;

    @FXML
    private ListView<?> select_rec_lsView;

    @FXML
    private Button add_btn;

    @FXML
    private Button deleteBtn;

    @FXML
    void handleAddBtn(ActionEvent event) {

    }

    @FXML
    void handleDeleteBtn(ActionEvent event) {

    }

    @FXML
    void handleAddMenuBtn(ActionEvent event) {

    }

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
    void handleBackBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/Menu_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    @FXML
    void handleAddBoxBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Menu_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleSearchBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Menu_search_select.fxml");
        cs.newWindow();

    }

    //-------------------------------------------- database method -----------------------------------------------------



}
