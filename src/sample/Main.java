package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.function.DoubleConsumer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Fxml/Main_page.fxml"));
        primaryStage.setTitle("Food Plan");
        Screen screen = Screen.getPrimary();
        Rectangle2D sbounds = screen.getBounds();

        double sw = sbounds.getWidth() ;
        double sh = sbounds.getHeight();

        listenToSizeInitialization(primaryStage.widthProperty(),
                w -> primaryStage.setX(( sw - w) /2));
        listenToSizeInitialization(primaryStage.heightProperty(),
                h -> primaryStage.setY(( sh - h) /2));

        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.setResizable(false);
        primaryStage.show();
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


    public static void main(String[] args) {
        launch(args);
    }
}
