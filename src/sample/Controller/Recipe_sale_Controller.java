package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.DBConnect;
import sample.Class.Recipe;

public class Recipe_sale_Controller {

    @FXML
    private TableView<?> salesTable;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TableColumn<?, ?> menuCol;

    @FXML
    private TableColumn<?, ?> salesCol;

    @FXML
    private TableColumn<?, ?> salesBagCol;

    @FXML
    private TableColumn<?, ?> foodQuanCol;

    @FXML
    private TableColumn<?, ?> leftCol;

    @FXML
    private Button back_btn;

    @FXML
    private Text rec_name;

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //Recipe rec = new Recipe("example", 10);
                //rec_name.setText(rec.getRec_name());

            }
        });
    }

    @FXML
    void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
    }

    public void setRec_name(String recname){
        this.rec_name.setText(recname);
    }
}
