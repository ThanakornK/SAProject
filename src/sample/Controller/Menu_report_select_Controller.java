package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Menu_report_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<?> listView;

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    void getSelectItem(ActionEvent event) {

    }

    @FXML
    void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {

    }

}