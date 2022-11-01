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
import javax.xml.transform.Result;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class StudentsController {

    static ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ResultSet rs;
    static ResultSet rs1;
    private FilteredList<Student> filteredStudentsData;
    private String mode;

    @FXML
    private AnchorPane ancho;

    @FXML
    private Label studentLbl;

    @FXML
    private TextField filtersTxt;

    @FXML
    private Label loadingTxt;

    @FXML
    private JFXButton exportBtn;

    @FXML
    private JFXButton addStuButton;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private MenuItem refreshBtn;

    @FXML
    void addNewStudent(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentFormAdd.fxml").openStream());
            Scene scene = new Scene(root);
            StageManager.studentFormAddStage.setScene(scene);
            StageManager.studentFormAddStage.getIcons().add(new Image("img/newstudenticon_16.png"));
            StageManager.studentFormAddStage.setTitle("New Student");

            StageManager.studentFormAddStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void deleteStudent(ActionEvent event) {
        ObservableList<Student> selectedStudents = FXCollections.observableArrayList();

        for(Student data  : studentData){
            if(data.getCheckBox().isSelected()){
                selectedStudents.add(data);
            }
        }

        if(selectedStudents.size() > 0){
            loadingTxt.setVisible(true);
            deleteBtn.setText("Loading...");
            loadingTxt.setText("Deleting Student(s)... Please Wait");

            Task task = new Task(){

                @Override
                protected Object call() throws Exception {
                    Platform.runLater(new Runnable(){

                        @Override
                        public void run() {
                            int deleted = StudentQueries.deleteStudents(selectedStudents);

                            if (deleted != 404) {
                                rs = StudentQueries.getStudents();
                                loadStudentData(rs);
                                mode = "All ";
                                showNotification("Student(s) Deleted Successfully");
                                setTitleLbl();
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
            makeAlert("warning", "No Students Selected\nPlease tick the checkboxes");

        }
    }

    @FXML
    void exportAction(ActionEvent event) {
        loadingTxt.setVisible(true);
        loadingTxt.setText("Exporting Data... Please wait");
        exportBtn.setText("Loading... ");

        ObservableList<Student> studentData = studentTable.getItems();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        XSSFWorkbook wb = new XSSFWorkbook();
                        XSSFSheet sheet = wb.createSheet("Students Details");
                        XSSFRow header = sheet.createRow(0);
                        header.createCell(0).setCellValue("Student ID");
                        header.createCell(1).setCellValue("Full Name");
                        header.createCell(2).setCellValue("Phone Number");
                        header.createCell(3).setCellValue("Postal Address");
                        header.createCell(4).setCellValue("Guardian Contact");
                        header.createCell(5).setCellValue("TRN");
                        header.createCell(6).setCellValue("Course");
                        header.createCell(7).setCellValue("Course Type");
                        header.createCell(8).setCellValue("location");
                        header.createCell(9).setCellValue("dateRegistered");
                        header.createCell(10).setCellValue("Date Created");


                        sheet.setColumnWidth(0, 256 * 15);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.setColumnWidth(3, 256 * 25);

                        sheet.setZoom(150);

                        int index = 1;

                        try{


                            for(Student student : studentData){
                                XSSFRow row = sheet.createRow(index);
                                row.createCell(0).setCellValue(student.getId());
                                row.createCell(1).setCellValue(student.getName());
                                row.createCell(6).setCellValue(student.getCourse());
                                row.createCell(7).setCellValue(student.getCourseType());
                                row.createCell(2).setCellValue(student.getPhone());
                                row.createCell(3).setCellValue(student.getAddress());
                                row.createCell(4).setCellValue(student.getGuardianPhone());
                                row.createCell(5).setCellValue(student.getTrn());
                                row.createCell(8).setCellValue(student.getLocation());
                                row.createCell(9).setCellValue(student.getDateRegistered());
                                row.createCell(10).setCellValue(student.getDate());

                                index++;
                            }


                            String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                                    "\\" + DrivingSchoolFMIS.schoolName + " FMIS\\" +
                                    mode + "Students"+ System.currentTimeMillis() + ".xlsx";

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
    void recordFeesAction(ActionEvent event) {
        try {
            Student student = studentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("ReceiptForm.fxml").openStream());
            ReceiptFormController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.receiptStage.setScene(scene);
            StageManager.receiptStage.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.receiptStage.setTitle("Record Fees Receipt for " + student.getName());

            StageManager.receiptStage.show();
        } catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student from the table");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @FXML
    void refreshAction(ActionEvent event) {
        refresh();
    }

    private void refresh() {
        loadStudentData(rs = StudentQueries.getStudents());
        mode = "All ";
        filterCombo.getSelectionModel().select("All Students");
        setTitleLbl();
    }

    @FXML
    void refreshActionOnMouseClicked(MouseEvent event) {
        refresh();
    }

    @FXML
    void studentTableOnMouseClickedAction(MouseEvent event) {

    }

    @FXML
    void filterByAction(ActionEvent event) {
        String option = filterCombo.getSelectionModel().getSelectedItem();

        if(option != null){
            switch(option){
                case "Having Discount":
                    loadStudentData(rs = StudentQueries.getStudentsWithDiscount());
                    mode = "Discount ";
                    break;
                case "Graduated":
                    loadStudentData(rs = StudentQueries.getStudentsGraduated());
                    mode = "Graduated ";
                    break;
                case "Not Graduated":
                    loadStudentData(rs = StudentQueries.getStudentsNotGraduated());
                    break;
                case "Government Fee":
                    loadStudentData(rs=StudentQueries.getStudentsWithGvtFee());
                    mode = "Government Fee ";
                    break;
                case "All Students":
                    loadStudentData(rs = StudentQueries.getStudents());
                    mode = "All ";
                    break;
            }

            setTitleLbl();
        }

    }

    @FXML
    void studentsFilterOnKeyReleased(KeyEvent event) {
        filtersTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredStudentsData.setPredicate((Predicate<? super Student>) student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getId().contains(newValue)) {
                    return true;
                } else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getCourse().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getCourseType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                return true;
                }else if (student.getDateRegistered().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (student.getGender().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                    return false;
                });
        });
        SortedList<Student> sortedData = new SortedList<>(filteredStudentsData);
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());
        studentTable.setItems(sortedData);
        studentLbl.setText(studentTable.getItems().size() + " Student(s)");
    }

    @FXML
    void studentsTableOnKeyReleasedAction(KeyEvent event) {

    }

    @FXML
    void viewFeesAction(ActionEvent event) {

        try {
            Student student = studentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("FeesHistory.fxml").openStream());
            FeesHistoryController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.historyStage.setScene(scene);
            StageManager.historyStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.historyStage.setTitle("Fees Receipt History For " + student.getName());
            StageManager.historyStage.show();

        } catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student from the table");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @FXML
    void editStudent(ActionEvent event) {
        try {
            Student student = studentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentFormEdit.fxml").openStream());
            StudentFormEditController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.studentFormEditStage.setScene(scene);
            StageManager.studentFormEditStage.getIcons().add(new Image("img/editicon_16.png"));
            StageManager.studentFormEditStage.setTitle("Update Student");

            StageManager.studentFormEditStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student\nUse your mouse buttons");
        }
    }

    @FXML
    void exploreStudent(ActionEvent event) {
        Student student = studentTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentDetails.fxml").openStream());
            StudentDetailsController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.studentDetails.setScene(scene);
            StageManager.studentDetails.getIcons().add(new Image("img/testicon_16.png"));
            StageManager.studentDetails.setTitle("Exploring " + student.getName());

            StageManager.studentDetails.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student\nUse your mouse buttons");
        }
    }

    @FXML
    void exploreTest(ActionEvent event) {
        try {
            Student student = studentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentTest.fxml").openStream());
            StudentTestController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.studentTestStage.setScene(scene);
            StageManager.studentTestStage.getIcons().add(new Image("img/testicon_16.png"));
            StageManager.studentTestStage.setTitle("Exploring tests for " + student.getName());

            StageManager.studentTestStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student\nUse your mouse buttons");
        }
    }

    public void initialize() {
        ancho.getStylesheets().add(getClass().getResource("students.css").toExternalForm());
        loadingTxt.setVisible(false);
        loadStudentData(rs = StudentQueries.getStudents());
        mode = "All ";
        setStudentTable();
        initSearchFilterVars();
        setFilterComboBox();
        setTitleLbl();
        filterCombo.getSelectionModel().select("All Students");
    }

    private void setFilterComboBox() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList();
        filterOptions.add("Having Discount");
        filterOptions.add("Graduated");
        filterOptions.add("Not Graduated");
        filterOptions.add("Government Fee");
        filterOptions.add("All Students");

        filterCombo.setItems(filterOptions);
    }

    private void setTitleLbl() {
        studentLbl.setText(studentTable.getItems().size() + " Student(s)");
    }

    private void initSearchFilterVars() {
        filteredStudentsData = new FilteredList<>(studentData, e->true);
    }

    public static void loadStudentData(ResultSet rs1) {
        studentData.clear();

        try {
            loadStudent(rs1, studentData);
        } catch (SQLException ex) {
            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setStudentTable() {
        studentTable.setItems(studentData);

        TableColumn col00 = new TableColumn("Tick");
        TableColumn col1 = new TableColumn("Student ID");
        TableColumn col2 = new TableColumn("Full Name");
        TableColumn col3 = new TableColumn("Gender");
        TableColumn col4 = new TableColumn("Phone Number");
        TableColumn col5 = new TableColumn("Postal Address");
        TableColumn col6 = new TableColumn("Location");
        TableColumn col7 = new TableColumn("Guardian Contact");
        TableColumn col8 = new TableColumn("Course");
        TableColumn col9 = new TableColumn("Category");
        TableColumn col10 = new TableColumn("Traffic RN");
        TableColumn col11 = new TableColumn("Date Registered");
        TableColumn col12 = new TableColumn("Date Created");

        col00.setMinWidth(20);
        col1.setMinWidth(50);
        col2.setMinWidth(150);
        col3.setMinWidth(70);
        col4.setMinWidth(100);
        col5.setMinWidth(150);
        col6.setMinWidth(50);
        col7.setMinWidth(150);
        col8.setMinWidth(50);
        col9.setMinWidth(50);
        col10.setMinWidth(150);
        col11.setMinWidth(150);
        col11.setMinWidth(150);

        studentTable.getColumns().addAll(col00, col1, col2, col3, col4, col5 , col6, col7, col8,  col9
                , col10, col11, col12);
        studentTable.setEditable(true);

        col00.setCellValueFactory(new PropertyValueFactory<Student, String>("checkBox"));
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col4.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col5.setCellValueFactory(new PropertyValueFactory<>("address"));
        col6.setCellValueFactory(new PropertyValueFactory<>("location"));
        col7.setCellValueFactory(new PropertyValueFactory<>("guardianPhone"));
        col8.setCellValueFactory(new PropertyValueFactory<>("course"));
        col9.setCellValueFactory(new PropertyValueFactory<>("courseType"));
        col10.setCellValueFactory(new PropertyValueFactory<>("trn"));
        col11.setCellValueFactory(new PropertyValueFactory<>("dateRegistered"));
        col12.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    static void loadStudent(ResultSet rs, ObservableList<Student> students) throws SQLException {
        while (rs.next()) {
            students.add(new Student(
                    rs.getString("studentID"),
                    rs.getString("fullname"),
                    rs.getString("phone"),
                    rs.getString("postalAddress"),
                    rs.getString("gender"),
                    rs.getString("course"),
                    rs.getString("duration"),
                    rs.getString("date"),
                    rs.getString("dateRegistered"),
                    rs.getString("guardianPhoneNumber"),
                    rs.getString("location"),
                    rs.getString("trn"),
                    rs.getBoolean("anyDiscount"),
                    rs.getBoolean("anyGovernmentFee"),
                    rs.getBoolean("graduated")
            ));

        }
    }

}
