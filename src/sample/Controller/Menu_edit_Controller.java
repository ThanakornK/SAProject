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
    private Button back_btn;

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
    void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
    }



    @FXML
    void handleAddBoxBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Menu_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

}
