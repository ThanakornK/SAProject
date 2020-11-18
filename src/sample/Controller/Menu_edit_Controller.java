package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.*;
import sample.Class.Menu;
import sample.Class.MenuRecipe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu_edit_Controller {

    @FXML
    private Button addMenuBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private TextField add_menu_name_field, update_rec_name;

    @FXML
    private Button add_Menu_btn;

    @FXML
    private Button back_btn, menuSearchBtn;

    @FXML
    private ListView<?> recipe_list_view;

    @FXML
    private ListView<?> select_rec_lsView;

    @FXML
    private Button add_btn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<MenuRecipe, String> menu_name;

    @FXML
    private TableColumn<MenuRecipe, String> menu_quan;

    @FXML
    private TableColumn<Recipe, String> rec_name;

    @FXML
    private TableView<Recipe> recTable;

    private ObservableList<MenuRecipe> menuList = FXCollections.observableArrayList();

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    private ObservableList<Recipe> selectRecList = FXCollections.observableArrayList();

    private ObservableList<IngRecipe> selectRecIngList = FXCollections.observableArrayList();

    private ObservableList<MenuRecipe> selectMenuRecList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> recipeIngList = FXCollections.observableArrayList(); // Used to store local ingredient

    private ObservableList<Recipe> tempList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    MenuRecipe selectMenu;

    Recipe selectRecipe;

    AlertBox alertBox = new AlertBox();

    private DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                readAllMenu(menuList);  // read menuRecipe from database
                readAllRec(recipesList);  // read recipe from database

                rec_name.setCellValueFactory(new PropertyValueFactory<>("recName"));
                menu_name.setCellValueFactory(new PropertyValueFactory<>("menuName"));


                if (selectRecipe != null) {
                    add_menu_name_field.setText(selectRecipe.getRec_name());

                    for (IngRecipe i : selectRecipe.getIngList()) {
                        selectRecIngList.add(i);
                    }
                    recTable.setItems(selectRecList);
                    recTable.refresh();
                } else {
                    System.out.println("null naja");
                }

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
                String MenuName = rs.getString("Menu_name");


                MenuRecipe newMenu = new MenuRecipe(MenuName,recipesList);
                list.add(newMenu);

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
                    readRec.addIngList(temp);
                }

                list.add(readRec);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void setSelectMenu(MenuRecipe menu) {
        this.selectMenu = menu;
    }

    public int isInSelectRecIngList(String ingName) {
        if (selectRecIngList.isEmpty()) {
            return 0;
        }
        for (IngRecipe i : selectRecIngList) {
            if (ingName.equals(i.getIngName())) {
                return -1;
            }
        }
        return 0;
    }


    @FXML
    void handleAddBtn(ActionEvent event) {

    }

    @FXML
    void handleDeleteBtn() {
//        if (!tempList.isEmpty()) {                                         // Remove ingredients that is not currently in database
//            for (Recipe j : tempList) {
//                if (j.getRec_name().equals(update_rec_name.getText())) {
//                    tempList.remove(j);
//                    selectMenuRecList.remove(j);
//                    recTable.refresh();
//                    break;
//                } else {
//                    continue;
//                }
//            }
//        }
//
//        for (MenuRecipe i : selectMenuRecList) {
//            if (i.getRecName().equals(update_rec_name.getText())) {
//                if (dbConnect.deleteRecord("delete from IngRec WHERE Ing_name = ?", "str", update_ing_name.getText()) == 0) {
//                    System.out.println("Delete successful");
//                }
//                selectRecIngList.remove(i);
//                ingTable.refresh();
//                break;
//            } else {
//                continue;
//            }
//
//        }
//        update_ing_name.clear();
//        update_ing_quan.clear();
    }

    @FXML
    void handleAddMenuBtn(ActionEvent event) {

    }

    //---------------------------------------- normal button method ----------------------------------------------------



    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/Menu_page.fxml",event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);
    }

    @FXML
    void handleAddBoxBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Menu_update_page.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    void handleSearchBtn(ActionEvent event) throws IOException {

        ChangeScene cs = new ChangeScene("../Fxml/Menu_search_select.fxml");
        cs.newWindow();

    }

    //-------------------------------------------- database method -----------------------------------------------------



}
