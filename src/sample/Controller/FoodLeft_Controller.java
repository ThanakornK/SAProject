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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import sample.Class.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.function.DoubleConsumer;

public class FoodLeft_Controller {

    @FXML
    private Button FoodPlanBox;

    @FXML
    private Button editMenuBtn;

    @FXML
    private AnchorPane showMenu_con;

    @FXML
    private TableView<RecipeReport> recLeftQuan_table;

    @FXML
    private TableView<RecipeReport> recRecomQuan_table;

    @FXML
    private TableColumn<RecipeReport, String> rec_leftNameCol;

    @FXML
    private TableColumn<RecipeReport, Double> rec_leftQuanCol;

    @FXML
    private TableColumn<RecipeReport, String> rec_recomNameCol;

    @FXML
    private TableColumn<RecipeReport, Double> rec_recomQuanCol;

    @FXML
    private Button calculateFoodQuan_btn;

    @FXML
    private Button reportBtn;

    @FXML
    private DatePicker dateSale;

    @FXML
    private TextField menu_name_field;

    private String menuSelect = "";

    private AlertBox alertBox;
    private DBConnect dbConnect = new DBConnect();
    private String foodDate;

    private ObservableList<RecipeReport> recList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dateSale.setValue(LocalDate.now());
                if (menuSelect != ""){
                    menu_name_field.setText(menuSelect);
                    readSetRecipe(recList);

                    rec_leftNameCol.setCellValueFactory(new PropertyValueFactory<RecipeReport,String>("recNameReport"));
                    rec_leftQuanCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, Double>("leftOver_fqReport"));
                    setRecipeReportColumnDouble(rec_leftQuanCol);

                    rec_recomNameCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, String>("recNameReport"));
                    rec_recomQuanCol.setCellValueFactory(new PropertyValueFactory<RecipeReport, Double>("recommend_fqReport"));

                    setRecipeReportColumnDouble(rec_recomQuanCol);
                }
            }
        });
    }

    @FXML
    private void getDateAction(ActionEvent event) {
        if (dateSale.getValue() != null) {
            Locale lc = new Locale("en","EN");
            String currentDate = new SimpleDateFormat("yyyy-MM-dd",lc).format(new Date());
            String selectDate = dateSale.getValue().toString();
            if (isDateOfInterestValid("yyyy-MM-dd", currentDate, selectDate)){
                foodDate = currentDate;
            }else{
                AlertBox alertBox = new AlertBox();
                alertBox.alertERR("err","กรุณาเลือกเวลาปัจจุบันหรืออนาคต");
                dateSale.setValue(LocalDate.now());
            }

        } else {
            AlertBox alertBox = new AlertBox();
            alertBox.alertERR("err","กรุณาเลือกวันที่ขาย");
        }
        updateFoodLeft();
    }

    public static boolean isDateOfInterestValid(String dateformat, String currentDate, String dateOfInterest) {

        String format = dateformat;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date cd = null;
        Date doi = null;

        try {
            cd = sdf.parse(currentDate);
            doi = sdf.parse(dateOfInterest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = cd.getTime() - doi.getTime();
        int diffDays = (int) (diff / (24 * 1000 * 60 * 60));

        if (diffDays > 0) {
            return false;
        } else {
            return true;
        }
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

    //----------------------------------------- normal method ----------------------------------------------------------

    public void setMenuSelect(String menu) {
        this.menuSelect = menu;
    }

    public void readSetRecipe(ObservableList<RecipeReport> list) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * " +
                    "FROM MenuRecipe " +
                    "WHERE MenuRecipe.Menu_name = ? ;";
            ps = con.prepareStatement(sql);
            ps.setString(1,menuSelect);
            rs = ps.executeQuery();

            while(rs.next()) {
                String regName = rs.getString("Rec_name");
                double recommendFq = rs.getDouble("Recommend_fq");
                int recId = rs.getInt("MenuRec_ID");
                RecipeReport rp = new RecipeReport(regName,recommendFq,0.0, 0.0);
                rp.setMenuRecId(recId);
                list.add(rp);
            }
            recLeftQuan_table.setItems(list);
            recRecomQuan_table.setItems(list);

        } catch (SQLException e) {
            System.out.println(e.toString());
            alertBox.alertERR("err", "การอ่านข้อมูลผิดพลาด");
        }
    }

    @FXML
    void handleCalculate(ActionEvent event) {

    }

    //---------------------------------------- calculate method --------------------------------------------------------

    //example
    public double calculateFood(){

        Double foodQuan = 200.00; // x bar from foodQuanList (keep record 5 - 30 record)
        Double foodLeft = 50.00;  // x bar from foodLeftList (keep record 5 - 30 record)
        String additional; // for tell เพิ่ม ลด เท่าเดิม
        Double howMuch;

        Double foodLeftPer = ( foodLeft/foodQuan ) * 100;

        if (foodLeftPer > 70.00) {
            additional = "เพิ่มปริมาณ";
            if ( foodLeftPer <= 85.00 ) {
                howMuch = 25.00;
            }else {
                howMuch = 50.00;
            }
        }else if (foodLeftPer > 40.00) {
            additional = "ไม่เปลี่ยนแปลง";
            howMuch = 0.0;
        }else{
            additional = "ลดปริมาณ";
            if ( foodLeftPer >= 25.00 ){
                howMuch = -25.00;
            }else {
                howMuch = -50.00;
            }
        }

        // if record less than 5 alert box say record too low
        // use howMuch to find change quantity and add to original quantity to show user

        Double changeQuan = (foodQuan*howMuch)/100;
        Double newQuan = foodQuan+changeQuan;

        return newQuan;
    }


    //---------------------------------------- normal button method ----------------------------------------------------


    //--------------------------------------- change page method -------------------------------------------------------

    @FXML
    void handleFoodPlanBox(ActionEvent event) throws IOException {
        Screen screen = Screen.getPrimary();
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/FoodQuan_page.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        FoodQuan_Controller controller = fxmlLoader.getController();
        controller.setMenuSelect(menuSelect);
        Rectangle2D sbounds = screen.getBounds();
        double sw = sbounds.getWidth() ;
        double sh = sbounds.getHeight();

        listenToSizeInitialization(stage.widthProperty(),
                w -> stage.setX(( sw - w) /2));
        listenToSizeInitialization(stage.heightProperty(),
                h -> stage.setY(( sh - h) /2));

        stage.setTitle("Food Plan");
        stage.setScene(new Scene(parentRoot));
        stage.show();

    }

    @FXML
    void handleReport(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/Report_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../Fxml/Main_page.fxml"));
        stage.setScene(new Scene(loader.load(),1280,800));
        stage.show();
    }

    //-------------------------------------------- database method -----------------------------------------------------

    @FXML
    public void handleSaveLeftBtn(ActionEvent event) {

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

//    @FXML
//    void update(ActionEvent event) {
//        Connection con = DBConnect.connect();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//
//            String sql = "UPDATE FoodQuan " +
//                    "SET FoodQuan.Total_fq = ?" +
//                    "FROM FoodQuan, MenuRecipe" +
//                    "WHERE FoodQuan.MenuRec_ID = MenuRecipe.MenuRec_ID;";
//            ps = con.prepareStatement(sql);
//
//            for(int i = 0 ; i < recPlanQuan_table.getItems().size(); i++){
//
//                ps.setString(1, recPlanQuan_table.getItems().get(i).getTotal_fqReport().toString());
//                ps.execute();
//
//            }
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }

    public void updateFoodLeft(){
        for (RecipeReport rp: recList) {
            System.out.println(rp.getRecNameReport());
            ArrayList<ParaCommand> paraCommands = new ArrayList<>();
            ArrayList<ParaCommand> paraCommands2 = new ArrayList<>();
            paraCommands.add(new ParaCommand("double", String.valueOf(rp.getLeftOver_fqReport())));
            paraCommands.add(new ParaCommand("int", String.valueOf(rp.getMenuRecId())));
            paraCommands2.add(new ParaCommand("double", String.valueOf(rp.getRecommend_fqReport())));
            paraCommands2.add(new ParaCommand("int", String.valueOf(rp.getMenuRecId())));

            if(dbConnect.updateRecord("UPDATE FoodQuan " +
                    "SET LeftOver_fq = ?" +
                    "WHERE (MenuRec_ID = ?)", paraCommands) == 0){
                System.out.println("Update Left over food success");
                System.out.println(rp.getLeftOver_fqReport() + " " + rp.getMenuRecId());
                paraCommands.clear();
            }

            if(dbConnect.updateRecord("UPDATE MenuRecipe " +
                    "SET Recommend_fq = ?" +
                    "WHERE (MenuRec_ID = ?)", paraCommands2) == 0){
                System.out.println("Update Recommend food success");
                System.out.println(rp.getRecommend_fqReport() + " " + rp.getMenuRecId());
                paraCommands.clear();
            }
        }
    }

}
