package sample.Controller;

import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recipe_Controller {

    @FXML
    private Text recipe_name, price_per_bag;

    @FXML
    private TableView<IngRecipe> ing_list;

    @FXML
    private TableColumn<IngRecipe, String> ing_name;

    @FXML
    private TableColumn<IngRecipe, Double> cost_price;

    @FXML
    private ListView<Recipe> listViewRec;

    @FXML
    private Button update_rec_btn, sale_his_btn, ing_page_btn, recipe_page_btn, menu_page_btn, home_btn;

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();

    private ObservableList<IngRecipe> selectRecIng = FXCollections.observableArrayList();

    private AlertBox alertBox;

    @FXML
    public void initialize(){
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
                listViewRec.setItems(recipeList);

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
                    System.out.println(String.format("%.2f is Total cost of %s in %s recipe",temp.getTotalCost(), temp.getIngName(), readRec.getRec_name()));
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }



        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void getSelectMenu(){
        if (listViewRec.getSelectionModel().getSelectedItem() != null){   // check select row not null

            sale_his_btn.setDisable(false);
            sale_his_btn.setVisible(true);

            String priceText = String.format("%.2f",listViewRec.getSelectionModel().getSelectedItem().getRec_price());

            recipe_name.setText("ชื่อสูตรอาหาร: "+ listViewRec.getSelectionModel().getSelectedItem().getRec_name());
            price_per_bag.setText("ราคาต่อถุง: "+ priceText +" บาท");

        }
    }

    //--------------------------------------- normal button method -----------------------------------------------------

    @FXML
    void handle_sale_his(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Recipe_sale_page.fxml"));
        Parent parentRoot = fxmlLoader.load();
        Recipe_sale_Controller controller = fxmlLoader.getController();
        controller.setRec_name(listViewRec.getSelectionModel().getSelectedItem().getRec_name());
        Stage stage = new Stage();
        stage.setTitle("ยอดขายของสูตรอาหาร");
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    @FXML
    void handle_update_rec(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../Fxml/Recipe_update_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("เพิ่ม/แก้ไข สูตรอาหาร");
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    public void go_ing_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Ingredient_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    public void go_menu_page(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Menu_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();

    }

    @FXML
    public void handleHomeBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

}
