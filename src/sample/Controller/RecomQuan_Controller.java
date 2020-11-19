package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import sample.Class.ChangeScene;
import sample.Class.RecipeReport;

import java.io.IOException;

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

    private ObservableList<RecipeReport> sendQuan = FXCollections.observableArrayList();

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/FoodQuan_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    @FXML
    public void handleReportBtn(ActionEvent event) throws IOException {

    }

    public void setSendQuan(){
//        for (RecipeReport rp: report) {
//
//        }
    }
}
