package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    void getSelectItem(ActionEvent event) {

    }

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_edit_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {

    }

}
