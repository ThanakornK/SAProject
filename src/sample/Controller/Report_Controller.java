package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;

public class Report_Controller {

    @FXML
    private TableView<?> recipe_table;

    @FXML
    private TableColumn<?, ?> recipe_nameCol;

    @FXML
    private TableColumn<?, ?> originalQuanCol;

    @FXML
    private TableColumn<?, ?> recQuanCol;

    @FXML
    private TableColumn<?, ?> planQuanCol;

    @FXML
    private TableView<?> ing_table;

    @FXML
    private TableColumn<?, ?> ing_nameCol;

    @FXML
    private TableColumn<?, ?> ing_amountCol;

    @FXML
    private TableColumn<?, ?> need_quanCol;

    @FXML
    private TableColumn<?, ?> add_quanCol;

    @FXML
    private TableColumn<?, ?> ing_costCol;

    @FXML
    private Button newMenuBtn;

    @FXML
    private Button chooseMenuBtn;

    @FXML
    private Button back_btn;

    @FXML
    private Button pdfBtn;

    @FXML
    private Text menu_name;

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    void handleChooseMenuBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/Menu_report_select_page.fxml");
        cs.newWindow();
    }

    @FXML
    void handleNewMenuBtn(ActionEvent event) {

    }

    @FXML
    void handlePdfBtn(ActionEvent event) {

    }

}
