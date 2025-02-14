package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.AlertBox;
import sample.Class.ChangeScene;
import sample.Class.DBConnect;
import sample.Class.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.DoubleConsumer;

public class FoodMenu_search_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button backBtn, confirmBtn;

    private ObservableList<Menu> menuList = FXCollections.observableArrayList();

    private ObservableList<String> menuStrList = FXCollections.observableArrayList();

    private AlertBox alertBox = new AlertBox();


    //------------------------------------------------------------------------------------------------------------------

    @FXML
    private void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readMenu(menuStrList);


                listView.setCellFactory(param -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item == null) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                });

                FilteredList<String> menuFilterList = new FilteredList<>(menuStrList, p -> true);
                nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                    menuFilterList.setPredicate(menu -> {
                        if (newValue == null || newValue.isEmpty()){
                            return true;
                        }

                        if (menu.indexOf(newValue) != -1){
                            return true;
                        } else{
                            return false;
                        }
                    });

                });

                // wrap filter list to sorted list
                SortedList<String> sortedList = new SortedList<>(menuFilterList);

                listView.setItems(sortedList);
            }
        });
    }
    //----------------------------------------- normal method ----------------------------------------------------------

    public void getSelectMenu(MouseEvent mouseEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null){   // check select row not null

            nameField.setText(listView.getSelectionModel().getSelectedItem());

        }
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

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------



    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/FoodQuan_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) throws IOException {
        Screen screen = Screen.getPrimary();
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/FoodQuan_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        FoodQuan_Controller controller = fxmlLoader.getController();
        String name = nameField.getText();
        controller.setMenuSelect(name);
        Rectangle2D sbounds = screen.getBounds();
        double sw = sbounds.getWidth() ;
        double sh = sbounds.getHeight();

        listenToSizeInitialization(stage.widthProperty(),
                w -> stage.setX(( sw - w) /2));
        listenToSizeInitialization(stage.heightProperty(),
                h -> stage.setY(( sh - h) /2));

        stage.setTitle("Food Plan");
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    public void readMenu(ObservableList<String> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM MenuRecipe GROUP BY Menu_name";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String MenuName = rs.getString("Menu_name");
                System.out.println(MenuName);
                list.add(MenuName);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

}
