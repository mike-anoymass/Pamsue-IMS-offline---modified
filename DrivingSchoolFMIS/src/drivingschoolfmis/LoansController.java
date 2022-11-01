/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import com.jfoenix.controls.JFXComboBox;
import static drivingschoolfmis.AlertClass.makeAlert;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author ANOYMASS
 */
public class LoansController implements Initializable {
    
    @FXML
    private TableView<Loan> tableView;
    @FXML
    private JFXComboBox<String> employeeCombo;
    
    ObservableList<String> employees = FXCollections.observableArrayList();
    ObservableList<Employee> employees1 = FXCollections.observableArrayList();
    static ObservableList<Loan> loans = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setEmployeeCombo();
        setTable();
        loans.clear();
    }
    
    @FXML
    void employeeSelected(ActionEvent event) {
        
        try {
            String[] extract = employeeCombo.getSelectionModel().getSelectedItem().split("~");
            String employee_id = extract[0];
            
            loadLoans(Integer.parseInt(employee_id));
        } catch (NullPointerException ex) {
            makeAlert("error", "Please select an Employee");
        }
        
    }
    
    @FXML
    private void payBack(ActionEvent event) {
        
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            StageManager.loanStage.hide();
            try {
                
                FXMLLoader loader = new FXMLLoader();
                BorderPane root = loader.load(getClass().getResource("Settlement.fxml").openStream());
                SettlementController controller = (SettlementController) loader.getController();
                controller.initialize(employeeCombo.getSelectionModel().getSelectedItem(), tableView.getSelectionModel().getSelectedItem());
                
                Scene scene = new Scene(root);
                StageManager.settlementStage.setTitle("Loan Settlement for " + employeeCombo.getSelectionModel().getSelectedItem());
                StageManager.settlementStage.setScene(scene);
                
                StageManager.settlementStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            makeAlert("warning", "Please a record");
        }
        
    }
    
    
    private void setEmployeeCombo() {
        employees1 = new LoadData().loadEmployees();
        
        for (Employee e : employees1) {
            employees.add(e.getId() + "~" + e.getFullName());
        }
        
        employeeCombo.setItems(employees);
        
        TextFields.bindAutoCompletion(employeeCombo.getEditor(), employeeCombo.getItems());
    }
    
    private void setTable() {
        tableView.setItems(loans);
        
        TableColumn col1 = new TableColumn("No.");
        TableColumn col2 = new TableColumn("Date");
        TableColumn col3 = new TableColumn("Loan Amount");
        TableColumn col4 = new TableColumn("Balance");
        
        col1.setMinWidth(50);
        col2.setMinWidth(150);
        col3.setMinWidth(100);
        col4.setMinWidth(100);
        
        tableView.getColumns().addAll(col1, col2, col3, col4);
        tableView.setEditable(true);
        
        col1.setCellValueFactory(new PropertyValueFactory<>("loan_id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("date"));
        col3.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col4.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }
    
    public static void loadLoans(int employee_id) {
        loans.clear();
        loans.addAll(new LoanQueries().getLoansFor(employee_id));
        
        if (loans.isEmpty()) {
            makeAlert("warning", "This Employee does not have loans");
        }
    }
    
}
