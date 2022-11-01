package drivingschoolfmis;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.function.Predicate;
import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.BorderPane;

public class StaffController {

    static ObservableList<Employee> employees = FXCollections.observableArrayList();
    ResultSet rs;
    EmployeeQueries employeeQuery = new EmployeeQueries();
    FilteredList<Employee> filteredStudentsData = new FilteredList<>(employees, e->true);

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField filtersTxt;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private Label loadingTxt;

    @FXML
    private Label studentLbl;

    @FXML
    private JFXButton exportBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private TableView<Employee> staffTable;

    @FXML
    private MenuItem refreshBtn2;
    
    @FXML
    private JFXButton loanBtn;

    @FXML
    void addStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffFormAdd.fxml").openStream());

            Scene scene = new Scene(root);
            StageManager.staffAddStage.setScene(scene);
            StageManager.staffAddStage.getIcons().add(new Image("img/newstudenticon_16.png"));
            StageManager.staffAddStage.setTitle("New Staff");

            StageManager.staffAddStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void deleteStudent(ActionEvent event) {
        ObservableList<Employee> selectedStaff = FXCollections.observableArrayList();

        for(Employee data  : staffTable.getItems()){
            if(data.getCheckBox().isSelected()){
                selectedStaff.add(data);
            }
        }

        if(selectedStaff.size() > 0){
            loadingTxt.setVisible(true);
            deleteBtn.setText("Loading...");
            loadingTxt.setText("Deleting Employee(s)... Please Wait");

            Task task = new Task(){

                @Override
                protected Object call() throws Exception {
                    Platform.runLater(new Runnable(){

                        @Override
                        public void run() {
                            int deleted = employeeQuery.deleteEmployees(selectedStaff);

                            if (deleted != 404) {
                                loadEmployees(rs = employeeQuery.getEmployees());
                                showNotification("Employee(s) Deleted Successfully");
                                setCount();
                            }
                            loadingTxt.setVisible(false);
                            deleteBtn.setText("Delete");

                        }
                    });

                    return null;
                }
            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

        }else{
            makeAlert("warning", "Employee(s) not Selected\nPlease tick the checkboxes");
        }

    }

    @FXML
    void editStaff(ActionEvent event) {
        try {
            Employee emp = staffTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffFormEdit.fxml").openStream());
            StaffFormEditController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffEditStage.setScene(scene);
            StageManager.staffEditStage.getIcons().add(new Image("img/editicon_16.png"));
            StageManager.staffEditStage.setTitle("Update Employee");

            StageManager.staffEditStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void exploreStaff(ActionEvent event) {
        try {
            Employee emp = staffTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffDetails.fxml").openStream());
            StaffDetailsController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffDetailsStage.setScene(scene);
            StageManager.staffDetailsStage.getIcons().add(new Image("img/testicon_16.png"));
            StageManager.staffDetailsStage.setTitle("Exploring Staff - " + emp.getFullName());

            StageManager.staffDetailsStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void exportAction(ActionEvent event) {
        loadingTxt.setVisible(true);
        loadingTxt.setText("Exporting Data... Please wait");
        exportBtn.setText("Loading... ");

        ObservableList<Employee> studentData = staffTable.getItems();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        XSSFWorkbook wb = new XSSFWorkbook();
                        XSSFSheet sheet = wb.createSheet("Students Details");
                        XSSFRow header = sheet.createRow(0);
                        header.createCell(0).setCellValue("Employee ID");
                        header.createCell(1).setCellValue("Full Name");
                        header.createCell(2).setCellValue("Gender");
                        header.createCell(3).setCellValue("Phone Number");
                        header.createCell(4).setCellValue("Guardian Contact");
                        header.createCell(5).setCellValue("Postal Address");
                        header.createCell(6).setCellValue("Physical Address");
                        header.createCell(7).setCellValue("Location");
                        header.createCell(8).setCellValue("Department");
                        header.createCell(9).setCellValue("Status");
                        header.createCell(10).setCellValue("Working Hours");
                        header.createCell(11).setCellValue("Salary");
                        header.createCell(12).setCellValue("Date of Birth");
                        header.createCell(13).setCellValue("Date Registered");
                        header.createCell(14).setCellValue("Date Created");

                        sheet.setColumnWidth(0, 256 * 15);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.setColumnWidth(3, 256 * 25);

                        sheet.setZoom(100);

                        int index = 1;

                        try{
                            for(Employee student : studentData){
                                XSSFRow row = sheet.createRow(index);
                                row.createCell(0).setCellValue(student.getId());
                                row.createCell(1).setCellValue(student.getFullName());
                                row.createCell(2).setCellValue(student.getGender());
                                row.createCell(3).setCellValue(student.getPhone());
                                row.createCell(4).setCellValue(student.getGuardianPhone());
                                row.createCell(5).setCellValue(student.getPostalAddress());
                                row.createCell(6).setCellValue(student.getPhysicalAddress());
                                row.createCell(7).setCellValue(student.getLocation());
                                row.createCell(8).setCellValue(student.getDepartment());
                                row.createCell(9).setCellValue(student.getEmployeeStatus());
                                row.createCell(10).setCellValue(student.getWorkingHours());
                                row.createCell(11).setCellValue(student.getSalaryDesired());
                                row.createCell(12).setCellValue(student.getDob());
                                row.createCell(13).setCellValue(student.getDateRegistered());
                                row.createCell(14).setCellValue(student.getDate());

                                index++;
                            }


                            String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                                    "\\" + DrivingSchoolFMIS.schoolName + " FMIS\\Employees" + System.currentTimeMillis() + ".xlsx";

                            FileOutputStream fileOut = new FileOutputStream(fileName);
                            wb.write(fileOut);
                            fileOut.close();

                            loadingTxt.setVisible(false);
                            exportBtn.setText("Export List to Excel");

                            showNotification("Data Exported Successfully\nFile Location: " + fileName);

                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void filterByAction(ActionEvent event) {
        String option = filterCombo.getSelectionModel().getSelectedItem();

        if(option != null){
            switch(option){
                case "Driver":
                    loadEmployees(rs = employeeQuery.getDepartment("Driver"));
                    break;
                case "Instructor":
                    loadEmployees(rs = employeeQuery.getDepartment("Instructor"));
                    break;
                case "Admin":
                    loadEmployees(rs = employeeQuery.getDepartment("Admin"));
                    break;
                case "Receptionist":
                    loadEmployees(rs= employeeQuery.getDepartment("Receptionist"));
                    break;
                case "Guard":
                    loadEmployees(rs = employeeQuery.getDepartment("Guard"));
                    break;
                case "Cleaner":
                    loadEmployees(rs = employeeQuery.getDepartment("Cleaner"));
                    break;
            }

            setCount();
        }
    }

    @FXML
    void recordPayment(ActionEvent event) {
        try {
            Employee emp = staffTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffPaymentForm.fxml").openStream());
            StaffPaymentFormController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffPaymentStage.setScene(scene);
            StageManager.staffPaymentStage.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.staffPaymentStage.setTitle("Payment for - " + emp.getFullName());

            StageManager.staffPaymentStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void refreshAction(ActionEvent event) {
        refresh();
    }

    @FXML
    void refreshActionOnMouseClicked(MouseEvent event) {
        refresh();
    }

    public void refresh(){
        loadEmployees(rs = employeeQuery.getEmployees());
        setCount();
        filterCombo.getSelectionModel().select(null);
    }

    @FXML
    void staffFilterOnKeyReleased(KeyEvent event) {
        filtersTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredStudentsData.setPredicate((Predicate<? super Employee>) student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getId().contains(newValue)) {
                    return true;
                } else if (student.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getDepartment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getEmployeeStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getDateRegistered().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getPhysicalAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getGender().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Employee> sortedData = new SortedList<>(filteredStudentsData);
        sortedData.comparatorProperty().bind(staffTable.comparatorProperty());
        staffTable.setItems(sortedData);
        setCount();
    }

    @FXML
    void studentTableOnMouseClickedAction(MouseEvent event) {

    }

    
    @FXML
    void studentsTableOnKeyReleasedAction(KeyEvent event) {

    }
    
    @FXML
    void loans(ActionEvent event) {
        
        try {
            FXMLLoader loader = new FXMLLoader();
            BorderPane root = loader.load(getClass().getResource("Loans.fxml").openStream());

            Scene scene = new Scene(root);
            StageManager.loanStage.setScene(scene);
            StageManager.loanStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.loanStage.setTitle("Manage Loans");

            StageManager.loanStage.show();
        } catch (IOException ex) {
            Logger.getLogger(StaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    @FXML
    void viewPayment(ActionEvent event) {
        try {
            Employee emp = staffTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffPayments.fxml").openStream());
            StaffPaymentsController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffHistoryStage.setScene(scene);
            StageManager.staffHistoryStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.staffHistoryStage.setTitle("Payment History for " + emp.getFullName());

            StageManager.staffHistoryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    public void initialize(){
        anchor.getStylesheets().add(getClass().getResource("students.css").toExternalForm());
        loadEmployees(rs = employeeQuery.getEmployees());
        setStaffTable();
        setCount();
        new InitSetupForCombosAndToggles().loadIntoDepartments(filterCombo);
        loanBtn.setTooltip(new Tooltip("Manage Loans"));
                
    }

    private void setCount() {
        studentLbl.setText(staffTable.getItems().size() + " Employee(s)");
    }

    static void loadEmployees(ResultSet rs) {
        employees.clear();
        employees = LoadData.loadEmployees(rs);
    }

    private void setStaffTable() {
        staffTable.setItems(employees);

        TableColumn col00 = new TableColumn("Tick");
        TableColumn col1 = new TableColumn("Employee ID");
        TableColumn col2 = new TableColumn("Full Name");
        TableColumn col3 = new TableColumn("Gender");
        TableColumn col4 = new TableColumn("Phone Number");
        TableColumn col04 = new TableColumn("Guardian #");
        TableColumn col5 = new TableColumn("Postal Address");
        TableColumn col6 = new TableColumn("Physical Addr");
        TableColumn col7 = new TableColumn("Location");
        TableColumn col8 = new TableColumn("Department");
        TableColumn col9 = new TableColumn("Status");
        TableColumn col10 = new TableColumn("Working Hours");
        TableColumn col11 = new TableColumn("Salary");
        TableColumn col12 = new TableColumn("Date of Registration");

        col00.setMinWidth(20);
        col1.setMinWidth(50);
        col2.setMinWidth(150);
        col3.setMinWidth(50);
        col4.setMinWidth(100);
        col04.setMinWidth(100);
        col5.setMinWidth(150);
        col6.setMinWidth(50);
        col7.setMinWidth(70);
        col8.setMinWidth(50);
        col9.setMinWidth(50);
        col10.setMinWidth(100);
        col11.setMinWidth(70);
        col12.setMinWidth(100);

        staffTable.getColumns().addAll(col00, col1, col2, col3,col04, col4, col5 , col6, col7, col8,  col9
                , col10, col11, col12);
        staffTable.setEditable(true);

        col00.setCellValueFactory(new PropertyValueFactory<Employee, String>("checkBox"));
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col4.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col04.setCellValueFactory(new PropertyValueFactory<>("guardianPhone"));
        col5.setCellValueFactory(new PropertyValueFactory<>("postalAddress"));
        col6.setCellValueFactory(new PropertyValueFactory<>("physicalAddress"));
        col7.setCellValueFactory(new PropertyValueFactory<>("location"));
        col8.setCellValueFactory(new PropertyValueFactory<>("department"));
        col9.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        col10.setCellValueFactory(new PropertyValueFactory<>("workingHours"));
        col11.setCellValueFactory(new PropertyValueFactory<>("salaryDesired"));
        col12.setCellValueFactory(new PropertyValueFactory<>("dateRegistered"));
    }

}
