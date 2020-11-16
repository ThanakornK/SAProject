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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.AlertBox;
import sample.Class.DBConnect;
import sample.Class.Ingredient;
import sample.Class.Recipe;

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
    private TableView<?> ing_list;

    @FXML
    private ListView<Recipe> listViewRec;

    @FXML
    private Button update_rec_btn, sale_his_btn, ing_page_btn, recipe_page_btn, menu_page_btn, home_btn;

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();

    private AlertBox alertBox;

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                readAll(recipeList);  // read recipe from database
                Recipe ex1 = new Recipe("Egg fried rice",35);
                Recipe ex2 = new Recipe("Gang Green Sweet",45);
                recipeList.add(ex1);       // example recipe
                recipeList.add(ex2);     // example recipe

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

    private void readAll(ObservableList<Recipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String recName = rs.getString("Rec_name");
                double recPrice = rs.getDouble("Rec_price");

                list.add(new Recipe(recName,recPrice));
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
        Parent parentRoot = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("ยอดขายของสูตรอาหาร");
        stage.setScene(new Scene(parentRoot));
        stage.show();
    }

    @FXML
    void handle_update_rec(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Recipe_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
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
