package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;
import sample.Class.Menu;

import java.io.IOException;

public class FoodMenu_search_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<Menu> listView;

    @FXML
    private Button backBtn, confirmBtn;


    @FXML
    void getSelectItem(ActionEvent event) {

    }

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {

    }

}
