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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class TestsController {
    ResultSet rs;
    TestQueries query = new TestQueries();
    private ObservableList<Test> testData = FXCollections.observableArrayList();
    private FilteredList<Test> filteredData;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Test> testTable;

    @FXML
    private TextField filtersTxt;

    @FXML
    private Label loadingTxt;

    @FXML
    private Label countLbl;

    @FXML
    private JFXButton exportBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    void deleteAction(ActionEvent event) {
        ObservableList<Test> selectedTests = FXCollections.observableArrayList();

        for(Test data  : testTable.getItems()){
            if(data.getCheckBox().isSelected()){
                selectedTests.add(data);
            }
        }

        if(selectedTests.size() > 0){
            loadingTxt.setVisible(true);
            deleteBtn.setText("Loading...");
            loadingTxt.setText("Deleting Test(s)...");

            Task task = new Task(){

                @Override
                protected Object call() throws Exception {
                    Platform.runLater(new Runnable(){

                        @Override
                        public void run() {
                            int deleted = query.deleteMany(selectedTests);

                            if (deleted != 404) {
                                rs = StudentQueries.getStudents();
                                loadTestData();
                                showNotification("Test(s) Deleted Successfully");
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
            makeAlert("warning", "Nothing to delete!\nPlease select the test(s) " +
                    "by ticking the checkboxes");

        }
    }

    @FXML
    void exportAction(ActionEvent event) {
        loadingTxt.setVisible(true);
        loadingTxt.setText("Exporting Data...");
        exportBtn.setText("Loading... ");

        ObservableList<Test> testData = testTable.getItems();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        XSSFWorkbook wb = new XSSFWorkbook();
                        XSSFSheet sheet = wb.createSheet("Test History");
                        XSSFRow header = sheet.createRow(0);
                        header.createCell(0).setCellValue("Student Name");
                        header.createCell(1).setCellValue("Test Taken");
                        header.createCell(2).setCellValue("Result");
                        header.createCell(3).setCellValue("Date of Test");
                        header.createCell(4).setCellValue("Date added");


                        sheet.setColumnWidth(0, 256 * 15);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.setColumnWidth(3, 256 * 25);

                        sheet.setZoom(100);

                        int index = 1;

                        try{


                            for(Test test : testData){
                                XSSFRow row = sheet.createRow(index);
                                row.createCell(0).setCellValue(test.getStudent());
                                row.createCell(1).setCellValue(test.getTestName());
                                row.createCell(2).setCellValue(test.getPassOrFail());
                                row.createCell(3).setCellValue(test.getDateOfTest());
                                row.createCell(4).setCellValue(test.getDate());

                                index++;
                            }


                            String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                                    "\\" + DrivingSchoolFMIS.schoolName + " FMIS\\Test History" +
                                    System.currentTimeMillis() + ".xlsx";

                            FileOutputStream fileOut = new FileOutputStream(fileName);
                            wb.write(fileOut);
                            fileOut.close();

                            loadingTxt.setVisible(false);
                            exportBtn.setText("Export to Excel");

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
    void filterOnKeyReleased(KeyEvent event) {
        filtersTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Test>) test -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (test.getStudent().contains(newValue)) {
                    return true;
                } else if (test.getTestName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (test.getPassOrFail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (test.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (test.getDateOfTest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Test> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(testTable.comparatorProperty());
        testTable.setItems(sortedData);

        setCount();
    }

    public void initialize(){
        anchor.getStylesheets().add(getClass().getResource("students.css").toExternalForm());

        loadTestData();
        setTestTable();

        filteredData = new FilteredList<>(testData, e->true);
    }

    private void setCount() {
        countLbl.setText("#"+testTable.getItems().size());
    }

    private void loadTestData() {
        testData.clear();

        try{
            rs = query.getTests();
            while(rs.next()){
                testData.add(
                        new Test(
                                rs.getString("testID"),
                                rs.getString("fullname"),
                                rs.getString("testName"),
                                rs.getString("testDate"),
                                rs.getString("dateOfTest"),
                                rs.getString("passOrFail")
                        )
                );
            }
        }catch(SQLException ex){

        }
    }

    private void setTestTable() {
        testTable.setItems(testData);
        setCount();

        TableColumn col0 = new TableColumn("Tick");
        TableColumn col = new TableColumn("Student Name");
        TableColumn col1 = new TableColumn("Test Taken");
        TableColumn col2 = new TableColumn("Result");
        TableColumn col3 = new TableColumn("Test Taken on");
        TableColumn col4 = new TableColumn("Date Added");

        col0.setMinWidth(70);
        col.setMinWidth(300);
        col1.setMinWidth(200);
        col2.setMinWidth(120);
        col3.setMinWidth(150);
        col4.setMinWidth(200);

        testTable.getColumns().addAll(col0, col, col1, col2, col3, col4);
        testTable.setEditable(true);


        col0.setCellValueFactory(new PropertyValueFactory<Test, String>("checkBox"));
        col.setCellValueFactory(new PropertyValueFactory<>("student"));
        col1.setCellValueFactory(new PropertyValueFactory<>("testName"));
        col2.setCellValueFactory(new PropertyValueFactory<>("passOrFail"));
        col3.setCellValueFactory(new PropertyValueFactory<>("dateOfTest"));
        col4.setCellValueFactory(new PropertyValueFactory<>("date"));
    }


}
