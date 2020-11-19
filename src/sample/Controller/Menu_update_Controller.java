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

public class Menu_update_Controller {

    @FXML
    private Button addMenuBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private TextField add_menu_name_field;

    @FXML
    private Button add_Menu_btn;

    @FXML
    private Button back_btn;

    @FXML
    private ListView<String> recipe_list_view;

    @FXML
    private ListView<String> select_rec_lsView;

    @FXML
    private ObservableList<String> allRec = FXCollections.observableArrayList();

    @FXML
    private ObservableList<String> addRec = FXCollections.observableArrayList();

    @FXML
    private Button add_btn;

    @FXML
    private Button deleteBtn;

    private String selectRec;

    private String selectFromSelectRec;

    AlertBox alertBox = new AlertBox();

    DBConnect dbConnect = new DBConnect();

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                readAllRec(allRec);
                recipe_list_view.setCellFactory(param -> new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);

                        if(empty || item == null || item == null) {
                            setText(null);
                        }   else {
                            setText(item);
                        }
                    }
                });
                recipe_list_view.setItems(allRec);

                select_rec_lsView.setCellFactory(param -> new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);

                        if(empty || item == null || item == null) {
                            setText(null);
                        }   else {
                            setText(item);
                        }
                    }
                });
                select_rec_lsView.setItems(addRec);

            }
        });
    }

    //----------------------------------------- normal method ----------------------------------------------------------

    public void readAllRec(ObservableList<String> list){
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Recipe";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                String recName = rs.getString("Rec_name");

                list.add(recName);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.normalAlert("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    public void getSelectRow(){
        if( recipe_list_view.getSelectionModel().getSelectedItem() != null){
            selectRec = recipe_list_view.getSelectionModel().getSelectedItem();
        }
    }

    public void getSelectRec(){
        if( select_rec_lsView.getSelectionModel().getSelectedItem() != null){
            selectFromSelectRec = select_rec_lsView.getSelectionModel().getSelectedItem();
        }
    }

    public int isInAddRec(String name){
        if(addRec.isEmpty()){
            return 0;
        }
        for(String i: addRec){
            if(i.equals(name)){
                return -1;
            }
        }
        return 0;
    }


    //---------------------------------------- normal button method ----------------------------------------------------

    @FXML
    void handleAddRecBtn() {
        if(!(selectRec == null)){
            if (isInAddRec(selectRec) == -1) {
                alertBox.normalAlert("err", "มีสูตรอาหารนี้อยู่แล้ว");
            }
            else {
                addRec.add(selectRec);
                select_rec_lsView.refresh();
            }
        } else {
            alertBox.normalAlert("err", "กรุญาเลือกสูตรอาหาร");
        }
    }

    @FXML
    void handleDeleteRecBtn() {
        addRec.remove(selectFromSelectRec);
        select_rec_lsView.refresh();
    }

    @FXML
    void handleAddMenuBtn(ActionEvent event) {

        if(alertBox.alertConfirm("ยืนยันการเพิ่มเมนูหรือไม่") == 0) {
            if (add_menu_name_field.getText().isEmpty()) {
                alertBox.normalAlert("err", "กรุณากรอกชื่อเมนู");
            } else if (addRec.isEmpty()) {
                alertBox.normalAlert("err", "กรุญาเพิ่มสูตรอาหาร");
            } else {
                ArrayList<ParaCommand> paraCommands = new ArrayList<>();
                for(String s: addRec){
                    paraCommands.add(new ParaCommand("str", add_menu_name_field.getText()));
                    paraCommands.add(new ParaCommand("str", s));
                    paraCommands.add(new ParaCommand("double", "0"));

                    if(dbConnect.insertRecord("INSERT  INTO MenuRecipe(Menu_name, Rec_name, Recommend_fq) VALUES(?,?,?)", paraCommands) == 0){
                        System.out.println("Insert success");
                        paraCommands.clear();
                    }
                }
                add_menu_name_field.clear();
                addRec.clear();
            }
        }


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
