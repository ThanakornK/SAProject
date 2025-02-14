package sample.Controller;

import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;
import sample.Class.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recipe_Controller {

    @FXML
    private TableView<IngRecipe> ing_list;

    @FXML
    private TableColumn<IngRecipe, String> ing_name;

    @FXML
    private TableColumn<IngRecipe, Double> cost_price;

    @FXML
    private ListView<Recipe> listViewRec;

    @FXML
    private TextField recipe_name_field;

    @FXML
    private Button update_rec_btn, delete_btn, ing_page_btn, recipe_page_btn, menu_page_btn, home_btn, reBtn;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();

    private ObservableList<IngRecipe> selectIngRec = FXCollections.observableArrayList();

    private Recipe selectRec;

    private AlertBox alertBox = new AlertBox();
    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllIng(ingredientList);  // read ingredient from database
                readAllRec(recipeList);  // read recipe from database

                listViewRec.setCellFactory(param -> new ListCell<Recipe>() {            // use this method to show recipe name
                    @Override
                    protected void updateItem(Recipe item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null || item.getRec_name() == null) {
                            setText(null);
                        } else {
                            setText(item.getRec_name());
                        }
                    }
                });

                FilteredList<Recipe> recipeFilterList = new FilteredList<>(recipeList, p -> true);
                recipe_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
                    recipeFilterList.setPredicate(recipe -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        if (recipe.getRec_name().indexOf(newValue) != -1) {
                            return true;
                        } else {
                            return false;
                        }
                    });

                });

                // wrap filter list to sorted list
                SortedList<Recipe> RecipeSortedList = new SortedList<>(recipeFilterList);

                listViewRec.setItems(RecipeSortedList);

                ing_name.setCellValueFactory(new PropertyValueFactory<>("ingName"));
                cost_price.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

            }
        });
    }

    //----------------------------------------- Normal method ----------------------------------------------------------

    private void readAllIng(ObservableList<Ingredient> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Ingredient";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ingName = rs.getString("Ing_name");
                double ingPrice = rs.getDouble("Ing_price");
                int ingAmount = rs.getInt("Ing_amount");

                Ingredient newIngredient = new Ingredient(ingName, ingPrice, ingAmount);
                list.add(newIngredient);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllRec(ObservableList<Recipe> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String recName = rs.getString("Rec_name");

                Recipe readRec = new Recipe(recName);

                String sql2 = String.format("SELECT * FROM IngRec WHERE Rec_name = '%s'", readRec.getRec_name());
                ps2 = con.prepareStatement(sql2);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    String ingName = rs2.getString("Ing_name");
                    String rec_Name = rs2.getString("Rec_name");
                    double ingQuan = rs2.getDouble("Ing_quan");
                    System.out.println(String.format("Added %s into %s", ingName, rec_Name));
                    IngRecipe temp = new IngRecipe(ingName, recName, ingQuan);
                    temp.calculateTotalCost(ingredientList);
                    System.out.println(String.format("%.2f is Total cost of %s in %s recipe", temp.getTotalCost(), temp.getIngName(), readRec.getRec_name()));
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void getSelectMenu() {
        selectIngRec.clear();

        if (listViewRec.getSelectionModel().getSelectedItem() != null) {   // check select row not null

            System.out.println(listViewRec.getSelectionModel().getSelectedItem().getRec_name() + " has been selected");
            delete_btn.setDisable(false);
            delete_btn.setVisible(true);

            for (IngRecipe i : listViewRec.getSelectionModel().getSelectedItem().getIngList()) {
                selectIngRec.add(i);
            }
            ing_list.setItems(selectIngRec);
            ing_list.refresh();

            selectRec = listViewRec.getSelectionModel().getSelectedItem();

            recipe_name_field.setText(selectRec.getRec_name());
        }
    }

    //--------------------------------------- Button method -----------------------------------------------------

    @FXML
    void handle_update_rec(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleReBtn(){
        if( !(recipe_name_field.getText().isEmpty() && selectIngRec.isEmpty()) ){
            recipe_name_field.clear();
            selectIngRec.clear();
            listViewRec.refresh();
        }
    }

    //--------------------------------------- Change page method -------------------------------------------------------

    @FXML
    public void go_ing_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Ingredient_page.fxml"));
        stage.setScene(new Scene(loader.load(), 1280, 800));
        stage.show();
    }

    @FXML
    public void go_menu_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_page.fxml"));
        stage.setScene(new Scene(loader.load(), 1280, 800));
        stage.show();

    }

    @FXML
    public void handleHomeBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(), 1280, 800));
        stage.show();
    }

    //----------------------------------------------- Database method --------------------------------------------------

    @FXML
    public void handleDelBtn() {

        if (alertBox.alertConfirm("คุณต้องการจะลบสูตรอาหารนี้ใช่หรือไม่") == 0) {

            for (Recipe r : recipeList) {
                if (r.getRec_name().equals(selectRec.getRec_name())) {
                    if (dbConnect.deleteRecord("delete from IngRec WHERE Rec_name = ?", "str", selectRec.getRec_name()) == 0) {
                        System.out.println("Delete data from Recipe Successful");
                        if (dbConnect.deleteRecord("delete from Recipe Where Rec_name = ?", "str", selectRec.getRec_name()) == 0) {
                            System.out.println("Delete data from Recipe Successful");
                            listViewRec.refresh();
                            ing_list.refresh();
                            break;
                        }
                    }

                }
            }
//        }

        }

    }
}
