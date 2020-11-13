package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class Main_page {

    @FXML
    private Button manage_btn;

    @FXML public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @FXML
    public void go_Manage_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Ingredient_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }
}
