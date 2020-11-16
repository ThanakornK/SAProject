package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Recipe_search_select_Controller {

    @FXML
    private TextField nameField;

    @FXML
    private ListView<Recipe> listView;

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmBtn;

    AlertBox alertBox = new AlertBox();

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    Recipe selectRecipe;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllIng(ingredientList);
                readAllRec(recipesList);

                listView.setCellFactory(param -> new ListCell<Recipe>(){
                    @Override
                    protected void updateItem(Recipe item, boolean empty){
                        super.updateItem(item, empty);

                        if(empty || item == null || item.getRec_name() == null) {
                            setText(null);
                        }   else {
                            setText(item.getRec_name());
                        }
                    }
                });
                listView.setItems(recipesList);

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    private void readAllIng(ObservableList<Ingredient> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Ingredient";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String ingName = rs.getString("Ing_name");
                double ingPrice = rs.getDouble("Ing_price");
                int ingAmount = rs.getInt("Ing_amount");

                Ingredient newIngredient = new Ingredient(ingName,ingPrice,ingAmount);
                list.add(newIngredient);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    private void readAllRec(ObservableList<Recipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");
                double recPrice = rs.getDouble("Rec_price");

                Recipe readRec = new Recipe(recName, recPrice);

                String sql2 = String.format("SELECT * FROM IngRecipe WHERE Rec_name = '%s'",readRec.getRec_name());
                ps2 = con.prepareStatement(sql2);
                rs2 = ps2.executeQuery();
                while (rs2.next()){
                    String ingName = rs2.getString("Ing_name");
                    String rec_Name = rs2.getString("Rec_name");
                    double ingQuan = rs2.getDouble("Ing_quan");
                    System.out.println(String.format("Added %s into %s", ingName, rec_Name));
                    IngRecipe temp = new IngRecipe(ingName, recName, ingQuan);
                    temp.calculateTotalCost(ingredientList);
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }


    public void getSelectItem(){

        Recipe select = listView.getSelectionModel().getSelectedItem();
        System.out.println(select.getRec_name() + " has been selected.");
        if( select != null){
            selectRecipe = select;
        }


    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Recipe_edit_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    void handleConfirmBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Recipe_edit_page.fxml"));
        Parent parentRoot = fxmlLoader.load();
        Recipe_edit_Controller controller = fxmlLoader.getController();
        controller.setSelectRecipe(selectRecipe);
        Stage stage = new Stage();
        stage.setTitle("ยอดขายของสูตรอาหาร");
        stage.setScene(new Scene(parentRoot));
        stage.show();
        // do sth with database

        // use below method to go back
//        ChangeScene cs = new ChangeScene("../Fxml/Recipe_edit_page.fxml",event);
//        Screen screen = Screen.getPrimary();
//        cs.changeStageAction(screen);

    }


}
