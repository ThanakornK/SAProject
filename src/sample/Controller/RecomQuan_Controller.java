package sample.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

public class RecomQuan_Controller {

    @FXML
    private Button backBtn;

    @FXML
    private TableView<?> recipeList;

    @FXML
    private TableColumn<?, ?> recipe_nameCol;

    @FXML
    private TableColumn<?, ?> originalQuanCol;

    @FXML
    private TableColumn<?, ?> recomendQuanCol;

    @FXML
    private TableColumn<?, ?> additionCol;

    @FXML
    private Button reportBtn;

    @FXML
    public void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleReportBtn(ActionEvent event) {

    }
}
