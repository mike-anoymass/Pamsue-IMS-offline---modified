package drivingschoolfmis;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class ProceedConfirmation {
    public static boolean confirm(String name) {
        
        try{
               StageManager.ParentStage.getIcons().add(new Image("img/driving_16.png"));
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                       "Do want to add another " + name + "?");

               Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
               yesButton.setText("Yes Please");

               alert.setTitle("Confirmation");
               alert.setHeaderText(name + " added successfully!\n");
               alert.initModality(Modality.APPLICATION_MODAL);
               alert.initOwner(StageManager.ParentStage);
               //alert.setX(StageManager.ParentStage.getX());
               //alert.setY(StageManager.ParentStage.getY() + StageManager.ParentStage.getHeight());

               //setting an icon to alert Dialog- first thing, get he window of the alert dialog
               Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
               stage.getIcons().add(new Image("img/warning_32.png"));

               Optional<ButtonType> closeOptional = alert.showAndWait();
               if (ButtonType.OK.equals(closeOptional.get())) {
                   return true;
               }
               return false;
        }catch(NullPointerException n){
            AlertClass.makeAlert("error", "null: " + n);
        }catch(RuntimeException r){
            AlertClass.makeAlert("error", "runtime :" + r);
        }
       return false;
    }
}
