/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author ANOYMASS
 */
public class FxmlLoader {
    private Pane view;
    
    public Pane getDocument(String name){
        try {
            URL fileUrl = DrivingSchoolFMIS.class.getResource("/drivingschoolfmis/" + name + ".fxml");
            if(fileUrl == null){
                throw new java.io.FileNotFoundException("FXML file not found");
            }
            
            view = new FXMLLoader().load(fileUrl);
            
        } catch (IOException ex) {
            System.err.print("No page Found " + name + "\n"+ ex);
        }
        
        return view;
    }
    
}
