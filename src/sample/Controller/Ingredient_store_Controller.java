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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Class.DBConnect;
import sample.Class.IngReport;
import sample.Class.Ingredient;
import sample.Class.RecipeReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.DoubleConsumer;

public class Ingredient_store_Controller {

    @FXML
    private Button backBtn;

    @FXML
    private TableView<IngReport> ing_table;

    @FXML
    private TextField ing_name_field;

    @FXML
    private TableColumn<IngReport, String> ing_nameCol;

    @FXML
    private TableColumn<IngReport, Double> ing_amoutCol;

    @FXML
    private TableColumn<IngReport, Double> ing_usageCol;

    @FXML
    private TableColumn<IngReport, Double> ing_addCol;

    @FXML
    private TableColumn<IngReport, Double> ing_costCol;

    @FXML
    private Button reportBtn;

    private String menuSelect = "";

    private ObservableList<IngReport> ingList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readSetIng_tb(ingList);

                ing_nameCol.setCellValueFactory(new PropertyValueFactory<IngReport,String>("Ing_name"));
                ing_amoutCol.setCellValueFactory(new PropertyValueFactory<IngReport, Double>("Ing_amount"));
                ing_usageCol.setCellValueFactory(new PropertyValueFactory<IngReport,Double>("Ing_quan"));
                ing_addCol.setCellValueFactory(new PropertyValueFactory<IngReport, Double>("Ing_add"));
                ing_costCol.setCellValueFactory(new PropertyValueFactory<IngReport, Double>("Ing_originCost"));
                setIngReportColumnDouble(ing_amoutCol);
                setIngReportColumnDouble(ing_usageCol);
                setIngReportColumnDouble(ing_addCol);
                setIngReportColumnDouble(ing_costCol);

                FilteredList<IngReport> ingFilterList = new FilteredList<>(ingList, p -> true);
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
                SortedList<IngReport> ingSortedList = new SortedList<>(ingFilterList);

                ing_table.setItems(ingSortedList);

            }
        });
    }


    //----------------------------------------- normal method ----------------------------------------------------------

    public void setMenuSelect(String menu) {
        this.menuSelect = menu;
    }

    public void setIngReportColumnDouble(TableColumn<IngReport, Double> tableCol) {
        tableCol.setCellFactory(tc -> new TableCell<IngReport, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f",item));
                }
            }
        });
    }

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

    public void readSetIng_tb(ObservableList<IngReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT MenuRecipe.Rec_name, Ingredient.Ing_name, SUM(IngRec.Ing_quan), Ingredient.Ing_price, Ingredient.Ing_amount " +
                    "FROM (( IngRec " +
                    "INNER JOIN MenuRecipe ON MenuRecipe.Rec_name = IngRec.Rec_name) INNER JOIN Ingredient ON Ingredient.Ing_name = IngRec.Ing_name) " +
                    "WHERE MenuRecipe.Menu_name = ? " +
                    "GROUP BY Ingredient.Ing_name;";
            ps = con.prepareStatement(sql);
            ps.setString(1, menuSelect);
            rs = ps.executeQuery();
            while (rs.next()) {

                String ing_name = rs.getString("Ing_name");
                double ing_amount = rs.getDouble("Ing_amount");
                double ing_quan = rs.getDouble("SUM(IngRec.Ing_quan)");
                double ing_price = rs.getDouble("Ing_price");

                Ingredient newIng = new Ingredient(ing_name, ing_price, ing_amount);
                IngReport newIngReport = new IngReport(newIng, ing_quan);
                list.add(newIngReport);

            }
            ing_table.setItems(list);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
}
