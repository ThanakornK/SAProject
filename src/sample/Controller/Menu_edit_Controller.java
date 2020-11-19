package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Class.AlertBox;
import sample.Class.ChangeScene;
import sample.Class.DBConnect;
import sample.Class.ParaCommand;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu_edit_Controller {

    @FXML
    private Button addMenuBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private TextField update_menu_name_field;

    @FXML
    private Button add_Menu_btn;

    @FXML
    private Button back_btn, menuSearchBtn;

    @FXML
    private ListView<String> recipe_list_view;

    @FXML
    private ListView<String> select_rec_lsView;

    @FXML
    private Button add_btn;

    @FXML
    private Button deleteBtn;

    @FXML
    private ObservableList<String> allRec = FXCollections.observableArrayList();

    @FXML
    private ObservableList<String> selectRec = FXCollections.observableArrayList();

    private ObservableList<String> tempSelectRec = FXCollections.observableArrayList();

    String curMenuName;

    String selectFromAllRec;

    String selectFromSelectRec;

    int selectRecID;

    DBConnect dbConnect = new DBConnect();

    AlertBox alertBox = new AlertBox();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (curMenuName != null) {
                    readAllRec(allRec);
                    readAllSelectRec(selectRec);
                    update_menu_name_field.setText(curMenuName);
                    recipe_list_view.setCellFactory(param -> new ListCell<String>() {
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

                    select_rec_lsView.setCellFactory(param -> new ListCell<String>() {
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


                    recipe_list_view.setItems(allRec);
                    select_rec_lsView.setItems(selectRec);
                }

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    public void readAllRec(ObservableList<String> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String recName = rs.getString("Rec_name");

                list.add(recName);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void readAllSelectRec(ObservableList<String> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = String.format("SELECT Rec_name FROM MenuRecipe WHERE Menu_name = '%s'", curMenuName);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String recName = rs.getString("Rec_name");

                list.add(recName);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void readID(int id){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = String.format("SELECT * FROM MenuRecipe WHERE Menu_name = '%s' AND Rec_name = '%s'", curMenuName, selectFromSelectRec);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                int recID = rs.getInt("MenuRec_ID");
                selectRecID = recID;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void setCurMenuName(String name) {
        this.curMenuName = name;
    }

    public void getSelectionAllRec() {
        if (recipe_list_view.getSelectionModel().getSelectedItem() != null) {
            selectFromAllRec = recipe_list_view.getSelectionModel().getSelectedItem();
        }
    }

    public void getSelectionSelectRec() {
        if (select_rec_lsView.getSelectionModel().getSelectedItem() != null) {
            selectFromSelectRec = select_rec_lsView.getSelectionModel().getSelectedItem();
            readID(selectRecID);

        }
    }

    public int isInSelectRec(String name) {
        if (selectRec.isEmpty()) {
            return 0;
        }
        for (String recName : selectRec) {
            if (recName.equals(name)) {
                return -1;
            }
        }
        return 0;
    }

    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    void handleAddBtn() {
        if (isInSelectRec(selectFromAllRec) == -1) {
            alertBox.alertERR("err", "มีสูตรอาหารนี้อยู่แล้ว");
        } else {
            tempSelectRec.add(selectFromAllRec);
            selectRec.add(selectFromAllRec);
            select_rec_lsView.refresh();
        }
    }

    @FXML
    void handleDeleteBtn() {
        if (!tempSelectRec.isEmpty()) {
            for (String rec : tempSelectRec) {
                if (rec.equals(selectFromSelectRec)) {
                    tempSelectRec.remove(rec);
                    selectRec.remove(rec);
                    select_rec_lsView.refresh();
                    break;
                } else {
                    continue;
                }
            }
        }

        for (String rec : selectRec) {                      // For delete rec that is in database
            if (rec.equals(selectFromSelectRec)) {
                if(dbConnect.deleteRecord("DELETE FROM MenuRecipe WHERE MenuRec_ID = ?", "int", String.valueOf(selectRecID)) == 0) {
                    selectRec.remove(selectFromSelectRec);
                    select_rec_lsView.refresh();
                    break;
                }
            }
        }
    }

    @FXML
    void handleAddMenuBtn(ActionEvent event) {
        if (alertBox.alertConfirm("ยืนยันการแก้ไขเมนูหรือไม่") == 0) {
            if (selectRec.isEmpty()) {
                alertBox.alertERR("err", "กรุณาเพิ่มสูตรอาหาร");
            } else {
                ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                if (!tempSelectRec.isEmpty()) {
                    for (String s : tempSelectRec) {
                        paraCommands.add(new ParaCommand("str", curMenuName));
                        paraCommands.add(new ParaCommand("str", s));
                        paraCommands.add(new ParaCommand("double", "0"));

                        if (dbConnect.insertRecord("INSERT  INTO MenuRecipe(Menu_name, Rec_name, Recommend_fq) VALUES(?,?,?)", paraCommands) == 0) {
                            paraCommands.clear();
                        } else {
                            System.out.println("Insert fail");
                        }
                    }

                }
                tempSelectRec.clear();
                update_menu_name_field.clear();
                allRec.clear();
                selectRec.clear();
                recipe_list_view.refresh();
                select_rec_lsView.refresh();
            }
        }
    }

    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        ChangeScene cs = new ChangeScene("../Fxml/Menu_page.fxml", event);
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

        ChangeScene cs = new ChangeScene("../Fxml/Menu_search_select.fxml", event);
        Screen screen = Screen.getPrimary();
        cs.changeStageAction(screen);

    }

    //-------------------------------------------- database method -----------------------------------------------------


}
