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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.ChangeScene;

import java.io.IOException;
import java.util.function.DoubleConsumer;

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

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_edit_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

//        Button button = (Button) event.getSource();
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_edit_page.fxml"));
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
    void handleConfirmBtn(ActionEvent event) {
        // do sth with database

        // use below method to go back

//        Button button = (Button) event.getSource();
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/Menu_edit_page.fxml"));
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

    private void listenToSizeInitialization(ObservableDoubleValue size,             // method for change position of window
                                            DoubleConsumer handler) {

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs,
                                Number oldSize, Number newSize) {
                if (newSize.doubleValue() != Double.NaN) {
                    handler.accept(newSize.doubleValue());
                    size.removeListener(this);
                }
            }
        };
        size.addListener(listener);
    }

}
