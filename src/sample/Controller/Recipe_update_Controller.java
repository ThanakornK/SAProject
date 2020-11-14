package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;
import sample.Class.Recipe;

import java.io.IOException;

public class Recipe_update_Controller {

    @FXML
    private TextField add_ing_name, add_ing_quan;

    @FXML
    private Button add_btn, clear_btn, add_rec_btn, back_btn, addRecBox, editRecBtn;

    @FXML
    private TextField add_rec_name_field, add_rec_price_field;

    @FXML
    private ListView<Recipe> recipe_list_view, select_rec_lsView;

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @FXML
    void handleAddBtn(ActionEvent event) {

    }

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    @FXML
    void handleClearBtn(ActionEvent event) {

    }

    @FXML
    void handleEditBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_edit_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleRecBtn(ActionEvent event) {

    }

    // method double click try at home

//    @FXML
//    void recipelistGetClick(MouseEvent mouseEvent) {
//        recipe_list_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//
//                if (event.getClickCount() == 2) {
//                    // add AllrecipeList selectRow to select_rec_list
//                }
//            }
//        });
//    }

}