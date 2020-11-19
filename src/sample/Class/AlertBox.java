package sample.Class;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertBox {

    public void normalAlert(String alertType, String content){
        if (alertType.equals("err")){
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("พบข้อผิดพลาด");
            alertError.setHeaderText(content);
            alertError.showAndWait();

        }else if (alertType.equals("info")){
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("แจ้งข้อมูล");
            alertInfo.setHeaderText(content);
            alertInfo.showAndWait();
            
        }else if (alertType.equals("warning")){
            Alert alertWarn = new Alert(Alert.AlertType.WARNING);
            alertWarn.setTitle("ระวัง");
            alertWarn.setHeaderText(content);
            alertWarn.showAndWait();
        }
    }

    public int alertConfirm(String content){
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);       // confirm delete
        alertConfirm.setTitle("ยืนยัน");
        alertConfirm.setHeaderText(content);

        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK){
            return 0;
        }else{
            alertConfirm.close();
            return 1;
        }
    }

}
