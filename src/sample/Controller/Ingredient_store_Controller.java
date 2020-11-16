package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Class.Ingredient;

import java.util.ArrayList;

public class Ingredient_store_Controller {

    @FXML
    private Button backBtn;

    @FXML
    private TableView<Ingredient> ing_table;

    @FXML
    private TextField ing_name_field;

    @FXML
    private TableColumn<?, ?> ing_nameCol;

    @FXML
    private TableColumn<?, ?> ing_amoutCol;

    @FXML
    private TableColumn<?, ?> ing_usageCol;

    @FXML
    private TableColumn<?, ?> ing_addCol;

    @FXML
    private TableColumn<?, ?> ing_costCol;

    @FXML
    private Button reportBtn;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                FilteredList<Ingredient> ingFilterList = new FilteredList<>(ingredientList, p -> true);
                ing_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
                    ingFilterList.setPredicate(menu -> {
                        if (newValue == null || newValue.isEmpty()){
                            return true;
                        }

                        if (menu.getIng_name().indexOf(newValue) != -1){
                            return true;
                        } else{
                            return false;
                        }
                    });

                });

                // wrap filter list to sorted list
                SortedList<Ingredient> ingSortedList = new SortedList<>(ingFilterList);

                ing_table.setItems(ingSortedList);

            }
        });
    }


    //----------------------------------------- normal method ----------------------------------------------------------



    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    void handleReportBtn(ActionEvent event) {

    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    //-------------------------------------------- database method -----------------------------------------------------

}
