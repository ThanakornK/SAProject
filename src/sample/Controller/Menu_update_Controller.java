package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.DoubleConsumer;

public class Menu_update_Controller {

    @FXML
    private Button addMenuBox, update_btn;

    @FXML
    private Button editMenuBtn;

    @FXML
    private TextField add_menu_name_field, update_rec_name;

    @FXML
    private Button add_Menu_btn;

    @FXML
    private Button back_btn;

    @FXML
    private ListView<?> recipe_list_view;

    @FXML
    private ListView<?> select_rec_lsView;

    @FXML
    private Button add_btn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<Recipe> recTable;

    @FXML
    private TableColumn<MenuRecipe, String> menu_name;

    @FXML
    private ListView<MenuRecipe> listViewMenu;

    @FXML
    private TextField menu_name_field;

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList(); // Used to store all recipe in database

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private ObservableList<MenuRecipe> menuList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    private ObservableList<Recipe> addRecipeInfo = FXCollections.observableArrayList();


    Recipe selectRecipe;

    AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();


    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllMenu(menuList);
                readAllRec(recipeList);

                menu_name.setCellValueFactory(new PropertyValueFactory<>("menuName"));

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    private void readAllMenu(ObservableList<MenuRecipe> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM MenuRecipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String menuName = rs.getString("Menu_name");

                MenuRecipe newMenuRecipe = new MenuRecipe(menuName,recipesList);
                list.add(newMenuRecipe);

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

                Recipe readRec = new Recipe(recName);

                String sql2 = String.format("SELECT * FROM IngRec WHERE Rec_name = '%s'",readRec.getRec_name());
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

    public int isInRecList(String rec_name) {

        for (Recipe rec : recipeList) {
            if (rec_name.equals(rec.getRec_name())) {
                return recipeList.indexOf(rec);
            }
        }
        return -1;
    }

    public int isInMenuList(String menu_name) {

        for (MenuRecipe menu : menuList) {
            if (menu_name.equals(menu.getMenu_name())) {
                return menuList.indexOf(menu);
            }
        }
        return -1;
    }

    public int isInAddList(String rec_Name) {
        if (addRecipeInfo.isEmpty()) {
            return 0;
        }
        for (Recipe i : addRecipeInfo) {
            if (rec_Name.equals(i.getRec_name())) {
                return -1;
            }
        }
        return 0;
    }

    public int isDouble(String input) {
        try {
            Double.parseDouble(input);
            return 0;
        } catch (NumberFormatException e) {
            return 1;
//            alertBox.alertERR("err", "กรอกข้อมูลไม่ถูกต้อง");
        }
    }

//    public void clearTextField() {
//
//        add_menu_name_field.clear();
//    }
//
//    @FXML
//    void handleClearBtn(ActionEvent event) {
//
//        add_menu_name_field.clear();
//        addRecipeInfo.clear();
//    }

    public void getSelectedRow() {

        if (update_btn.isVisible() && (recTable.getSelectionModel().getSelectedItem() != null)) {
            System.out.println(recTable.getSelectionModel().getSelectedItem().getRec_name() + " is selected");
            update_menu_name_field.setText(recTable.getSelectionModel().getSelectedItem().getIngName());
        }

    }

    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    void handleAddRecBtn(ActionEvent event) {

    }

    @FXML
    void handleAddMenuBtn(ActionEvent event) {

    }

    @FXML
    void handleDeleteRecBtn() {

        for (Recipe i : addRecipeInfo) {
            if (i.getRec_name().equals(update_rec_name.getText())) {
                addRecipeInfo.remove(i);
                break;
            } else {
                continue;
            }
        }

//        update_ing_name.clear();
//        update_ing_quan.clear();

    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Menu_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    @FXML
    void handleEditBoxBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Menu_edit_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();

    }

    //-------------------------------------------- database method -----------------------------------------------------


}
