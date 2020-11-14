package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;
import java.util.function.DoubleConsumer;

public class Recipe_edit_Controller {

    @FXML
    private Button back_btn, add_rec_btn, recSearchBtn;


    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

//        Button button = (Button) event.getSource();
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_page.fxml"));
//        Parent parentRoot = (Parent) fxmlLoader.load();
//        Screen screen = Screen.getPrimary();
//        Rectangle2D sbounds = screen.getBounds();
//
//        double sw = sbounds.getWidth() ;
//        double sh = sbounds.getHeight();
//
//        listenToSizeInitialization(stage.widthProperty(),
//                w -> stage.setX(( sw - w) /2));
//        listenToSizeInitialization(stage.heightProperty(),
//                h -> stage.setY(( sh - h) /2));
//
//        stage.setScene(new Scene(parentRoot));
//        stage.show();
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

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_search_select.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

}
