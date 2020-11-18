package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import sample.Class.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.DoubleConsumer;

public class RecomQuan_Controller {

    @FXML
    private Button backBtn;

    @FXML
    private TableView<RecipeReport> recipeList;

    @FXML
    private TableColumn<RecipeReport, String> recipe_nameCol;

    @FXML
    private TableColumn<RecipeReport, Double> originalQuanCol;

    @FXML
    private TableColumn<RecipeReport, Double> recomendQuanCol;

    @FXML
    private TableColumn<RecipeReport, String> additionCol;

    @FXML
    private Button reportBtn;

    private String menuSelect = "";

    private AlertBox alertBox;

    private ObservableList<RecipeReport> recList = FXCollections.observableArrayList();

    RecipeReport selectRecipe;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (menuSelect != ""){
                    readSetRecipe_tb(recList);

                    recipe_nameCol.setCellValueFactory(new PropertyValueFactory<RecipeReport,String>("recNameReport"));
                    recomendQuanCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, Double>("recommend_fqReport"));
                    originalQuanCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, Double>("total_fqReport"));
                    setRecipeReportColumnDouble(originalQuanCol);
                }
            }
        });
    }

    public void readSetRecipe_tb(ObservableList<RecipeReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MenuRecipe.Rec_name, MenuRecipe.Recommend_fq " +
                    "FROM MenuRecipe INNER JOIN FoodQuan " +
                    "ON MenuRecipe.MenuRec_ID = FoodQuan.MenuRec_ID " +
                    "WHERE MenuRecipe.Menu_name = ? AND FoodQuan.Food_date = (SELECT max(FoodQuan.Food_date) " +
                    "FROM FoodQuan WHERE FoodQuan.MenuRec_ID = MenuRecipe.MenuRec_ID)";
            ps = con.prepareStatement(sql);
            ps.setString(1, menuSelect);
            rs = ps.executeQuery();

            while(rs.next()) {
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");

                RecipeReport rp = new RecipeReport(regName, recommendFq, 0.0);
                System.out.println(String.format("%s", rp.getRecNameReport()));
                list.add(rp);

            }
            recipeList.setItems(list);

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void setMenuSelect(String menu) {
        this.menuSelect = menu;
    }

    public void getSelectItem(){

        RecipeReport select = recipeList.getSelectionModel().getSelectedItem();
        System.out.println(select.getRecNameReport() + " has been selected.");
        if( select != null){
            selectRecipe = select;
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


    public void setRecipeReportColumnDouble(TableColumn<RecipeReport, Double> tableCol) {
        tableCol.setCellFactory(tc -> new TableCell<RecipeReport, Double>() {
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

    @FXML
    public void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleReportBtn(ActionEvent event) {

    }


}
