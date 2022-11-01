package drivingschoolfmis;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;
import static drivingschoolfmis.RandomNumbers.randomNumber;

public class PaymentController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private JFXTextField mirageTxt;

    @FXML
    private Label expenseLbl;

    @FXML
    private JFXTextField filterExpenseTxt;

    @FXML
    private JFXTextField expenseNameTxt;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private TableView<Expense> expenseTable;

    @FXML
    private MenuItem refreshMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private Label paymentLbl;

    @FXML
    private JFXButton generateBtn;

    @FXML
    private JFXComboBox<String> expenseCombo;

    @FXML
    private JFXTextField amountTxt;

    @FXML
    private JFXComboBox<String> modeCombo;

    @FXML
    private JFXTextField refTxt;

    @FXML
    private TableView<Payment> paymentsTable;

    @FXML
    private MenuItem refreshMenuItem11;

    @FXML
    private MenuItem deleteMenuItem11;

    @FXML
    private JFXButton printBtn;

    @FXML
    private JFXButton exportBtn;

    @FXML
    private Label totalRecordsLbl;

    @FXML
    private Label totalPaymentsLbl;

    @FXML
    private JFXTextField filterPaymentTxt;

    @FXML
    private JFXButton searchBtns;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private Label loadingTxt;

    private ResultSet rs;
    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private final ObservableList<Payment> payments = FXCollections.observableArrayList();
    private ObservableList<Payment> pdata = FXCollections.observableArrayList();
    private final ObservableList<String> expensesForCombo = FXCollections.observableArrayList();
    private final ObservableList<String> pm = FXCollections.observableArrayList();
    private FilteredList<Expense> filteredExpenseData;
    private FilteredList<Payment> filteredPaymentData;

    @FXML
    void printAction(ActionEvent event) {
        pdata = paymentsTable.getItems();

        if(Integer.parseInt(totalRecordsLbl.getText()) > 0){
            try {
                Document doc = new Document();
                String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                        "\\"+ DrivingSchoolFMIS.schoolName + " FMIS\\Payments" + System.currentTimeMillis() + ".pdf";

                PdfWriter.getInstance(doc, new FileOutputStream(fileName));
                doc.open();
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("img/driving_32.png");
                image.scaleToFit(120, 90);
                doc.add(image);
                doc.add(new Paragraph(DrivingSchoolFMIS.schoolName + " Driving School - Payments Report",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20, Font.BOLD, BaseColor.DARK_GRAY))
                );
                doc.add(new Paragraph("Printed by "
                        + LoginDocumentController.firstname + " " + LoginDocumentController.lastname +
                        " on "+ new Date().toString()
                ));
                doc.add(Chunk.NEWLINE);
                doc.add(new LineSeparator());
                doc.add(Chunk.NEWLINE);

                PdfPTable table0 = new PdfPTable(2);
                PdfPCell titleCell0 = new PdfPCell(new Paragraph("Payments Summary"));
                titleCell0.setColspan(4);
                titleCell0.setBorder(Rectangle.NO_BORDER);
                titleCell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell0.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table0.addCell(titleCell0);
                table0.addCell("Total Records:");
                table0.addCell(totalRecordsLbl.getText());
                table0.addCell("Sum of Payments:");
                table0.addCell(totalPaymentsLbl.getText());

                PdfPTable table1 = new PdfPTable(6);
                PdfPCell titleCell1 = new PdfPCell(new Paragraph("Payments Details"));
                titleCell1.setColspan(12);
                titleCell1.setBorder(Rectangle.NO_BORDER);
                titleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);


                table1.addCell(titleCell1);
                table1.addCell("Payment For");
                table1.addCell("Date of Payment");
                table1.addCell("Mode of Payment");
                table1.addCell("Reference");
                table1.addCell("Mirage/Description");
                table1.addCell("Amount (MK)");

                for(Payment r : pdata){
                    System.out.println("hey");
                    table1.addCell(r.getExpense());
                    table1.addCell(r.getDateOfPayment());
                    table1.addCell(r.getMode());
                    table1.addCell(r.getRef());
                    table1.addCell(r.getMirage());
                    table1.addCell(r.getAmount());
                }

                ReceiptsController.report(doc, fileName, table0, table1);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            makeAlert("warning" , "Nothing to report");
        }

    }

    @FXML
    void filterComboAction(ActionEvent event) {
        try{
            ResultSet rs = PaymentQueries.getPaymentsFor(filterCombo.getSelectionModel().getSelectedItem());
            loadPaymentTableData(rs);
            setLabels();
        }catch (NullPointerException n){
            makeAlert("warning", "No expense selected");
        }
    }

    @FXML
    void exportAction(ActionEvent event) {
        loadingTxt.setVisible(true);
        loadingTxt.setText("Exporting Data...");

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ObservableList<Payment> p = paymentsTable.getItems();

                        XSSFWorkbook wb = new XSSFWorkbook();
                        XSSFSheet sheet = wb.createSheet("Payment Details");
                        XSSFRow header = sheet.createRow(0);
                        header.createCell(0).setCellValue("Payment ID");
                        header.createCell(1).setCellValue("Payment For");
                        header.createCell(2).setCellValue("Cost (MK)");
                        header.createCell(3).setCellValue("Mode of Payment");
                        header.createCell(4).setCellValue("Reference");
                        header.createCell(5).setCellValue("Mirage/Description");
                        header.createCell(6).setCellValue("Date Of Payment");
                        header.createCell(7).setCellValue("Date Created");
                        header.createCell(8).setCellValue("Recorded By");



                        sheet.setColumnWidth(0, 256 * 15);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.setColumnWidth(3, 256 * 25);

                        sheet.setZoom(120);

                        int index = 1;

                        if(p.size() > 0){

                            try{
                                for(Payment data : p) {
                                    XSSFRow row = sheet.createRow(index);
                                    row.createCell(0).setCellValue(data.getId());
                                    row.createCell(1).setCellValue(data.getExpense());
                                    row.createCell(2).setCellValue(data.getAmount());
                                    row.createCell(3).setCellValue(data.getMode());
                                    row.createCell(4).setCellValue(data.getRef());
                                    row.createCell(5).setCellValue(data.getMirage());
                                    row.createCell(6).setCellValue( data.getDateOfPayment());
                                    row.createCell(7).setCellValue( data.getDate());
                                    row.createCell(8).setCellValue( data.getUser());

                                    index++;

                                }

                                String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                                        "\\"+ DrivingSchoolFMIS.schoolName + " FMIS\\Payments"+ System.currentTimeMillis() + ".xlsx";

                                FileOutputStream fileOut = new FileOutputStream(fileName);
                                wb.write(fileOut);
                                fileOut.close();

                                loadingTxt.setVisible(false);

                                showNotification("Data Exported Successfully\nFile Location is  " + fileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }else{
                            loadingTxt.setVisible(false);
                            makeAlert("warning", "Nothing to export");
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
    void ccOnKeyReleasedAction(KeyEvent event) {
        filterPaymentTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredPaymentData.setPredicate((Predicate<? super Payment>) p -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (p.getId().contains(newValue)) {
                    return true;
                } else if (p.getExpense().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getAmount().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getDateOfPayment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getRef().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getUser().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (p.getMode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;

            });
        });
        SortedList<Payment> sortedData = new SortedList<>(filteredPaymentData);
        sortedData.comparatorProperty().bind(paymentsTable.comparatorProperty());
        paymentsTable.setItems(sortedData);
        setLabels();
    }

    @FXML
    void coursesOnKeyReleasedAction(KeyEvent event) {
        filterExpenseTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredExpenseData.setPredicate((Predicate<? super Expense>) e -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (e.getName().contains(newValue)) {
                    return true;
                } else if (e.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;

            });
        });
        SortedList<Expense> sortedData = new SortedList<>(filteredExpenseData);
        sortedData.comparatorProperty().bind(expenseTable.comparatorProperty());
        expenseTable.setItems(sortedData);
    }

    @FXML
    void deleteExpenseAction(ActionEvent event) {
        Expense e = expenseTable.getSelectionModel().getSelectedItem();
        if(e != null){
            int response = PaymentQueries.deleteExpense(e.getName());

            if (response != 404) {
                loadExpenseTableData();
                setExpenseCombo();
                loadPaymentTableData(PaymentQueries.getAllPayments());
                showNotification("Expense Deleted Successfully");
            }
        }else{
            makeAlert("warning" , "Please Select an Expense From the table");
        }
    }

    @FXML
    void deletePaymentInfoAction(ActionEvent event) {
        Payment p = paymentsTable.getSelectionModel().getSelectedItem();
        if(p != null){
            int response = PaymentQueries.deletePayment(p.getId());

            if (response != 404) {
                loadPaymentTableData(PaymentQueries.getAllPayments());
                clearFields();
                setLabels();
                showNotification("Payment Deleted Successfully");
            }
        }else{
            makeAlert("warning" , "Nothing to delete\nPlease Select a Payment from the table");
        }
    }

    @FXML
    void expenseTableClickedAction(MouseEvent event) {

    }

    @FXML
    void expenseTableOnKeyReleasedAction(KeyEvent event) {

    }

    @FXML
    void paymentsOnKeyRelAction(KeyEvent event) {
        if (event.getCode() == KeyCode.UP | event.getCode() == KeyCode.DOWN) {
            autoFillPaymentFields();
        }
    }

    @FXML
    void paymentsTableMouseClicked(MouseEvent event) {
        autoFillPaymentFields();
    }

    @FXML
    void refreshCourseAction(ActionEvent event) {
        loadPaymentTableData(PaymentQueries.getAllPayments());
        setLabels();
    }

    @FXML
    void refreshCourseInfoAction(ActionEvent event) {
        filterCombo.getSelectionModel().select(null);
        loadPaymentTableData(PaymentQueries.getAllPayments());
        setLabels();
    }

    @FXML
    void saveExpenseAction(ActionEvent event) {
        Expense e = new Expense(expenseNameTxt.getText(), "date");
        boolean added = new AddExpense().add(e);

        if (added == false) {
            showNotification("Expense Has been added Succesfully");
            loadExpenseTableData();
            setExpenseCombo();
            clearField();
        }
    }

    private void clearField() {
        expenseNameTxt.setText("");
    }

    @FXML
    void generateID(ActionEvent event) {
        int randomID = randomNumber();

        while (!checkPayment(Integer.toString(randomID))) {
            randomID = randomNumber();
        }

       // idTxt.setText(Integer.toString(randomID));
    }


    @FXML
    void savePaymentAction(ActionEvent event) {

        Payment p = setPayment();

        boolean added = new AddPayment().add(p);

        if (added == false) {
            showNotification("Payment Has been added Succesfully");
            loadPaymentTableData(PaymentQueries.getAllPayments());
            setLabels();
            clearFields();
        }
    }

    private void clearFields() {
        expenseCombo.getSelectionModel().select(null);
        amountTxt.setText("");
        modeCombo.getSelectionModel().select(null);
        refTxt.setText("");
        mirageTxt.setText("");
        dateTxt.setValue(null);

    }

    private boolean checkPayment(String id){
        try {
            rs = PaymentQueries.getPayment(id);
            while (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @FXML
    void searchBtnsAction(ActionEvent event) {
        if (filterExpenseTxt.isVisible()) {
            searchBtns.setText("Search");
            hideSearchTxts();
        } else {
            searchBtns.setText("Hide Search Fields");
            showSearchTxts();
        }
    }

    public void showSearchTxts() {
        filterExpenseTxt.setVisible(true);
        filterPaymentTxt.setVisible(true);
    }


    public void hideSearchTxts() {
        filterExpenseTxt.setVisible(false);
        filterPaymentTxt.setVisible(false);
    }

    @FXML
    void updatePaymentInfoAction(ActionEvent event) {
        Payment pForID = paymentsTable.getSelectionModel().getSelectedItem();
        if(pForID != null){
            Payment p = setPayment();

            if (new ValidateFieldsClass().validatePaymentFields(p)) {
                boolean edited = new PaymentQueries().editPayment(p, pForID.getId());

                if (edited == false) {
                    showNotification("Payment Has been Edited Succesfully");
                    clearFields();
                    loadPaymentTableData(PaymentQueries.getAllPayments());
                    setLabels();
                }
            }

        }else{
            makeAlert("Warning", "Nothing to Update!\nPlease select the payment from the table");
        }

    }

    private Payment setPayment() {
        String date;
        try {
            date = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            date = "";
        }

        Payment p = new Payment(
                expenseCombo.getSelectionModel().getSelectedItem(), "date", date,amountTxt.getText(),
                modeCombo.getSelectionModel().getSelectedItem(), refTxt.getText(), mirageTxt.getText(),
                LoginDocumentController.userName
        );

        return p;
    }

    @FXML
    void modeOnAction(ActionEvent event) {
        String selectedItem = modeCombo.getSelectionModel().getSelectedItem();

        if(selectedItem != "Cash" & selectedItem != null){
            refTxt.setVisible(true);
        }else{
            refTxt.setVisible(false);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.getStylesheets().add(getClass().getResource("students.css").toExternalForm());

        loadExpenseTableData();
        setExpenseTable();

        loadPaymentTableData(PaymentQueries.getAllPayments());
        setPaymentTable();

        setExpenseCombo();
        setModeCombo();

        setReferenceTxt();

        hideSearchTxts();
        initSearchFilterVars();

        setLabels();

        loadingTxt.setVisible(false);

    }

    public void initSearchFilterVars() {
        filteredExpenseData = new FilteredList<>(expenses, e -> true);
        filteredPaymentData = new FilteredList<>(payments, e -> true);
    }

    private void setReferenceTxt() {
        refTxt.setVisible(false);
    }

    private void setExpenseCombo() {
        expensesForCombo.clear();
        for(Expense data: expenses){
            expensesForCombo.add(data.getName());
        }

        expenseCombo.setItems(expensesForCombo);
        filterCombo.setItems(expensesForCombo);
    }

    private void setModeCombo(){
       new InitSetupForCombosAndToggles().loadPaymentsMode(modeCombo);
    }

    private void loadExpenseTableData() {
        expenses.clear();

        try {
            rs = PaymentQueries.getAllExpenses();
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("name"), rs.getString("date")));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void   setExpenseTable(){
        expenseTable.setItems(expenses);
        expenseTable.setEditable(true);

        TableColumn col1 = new TableColumn("Description");
        TableColumn col2 = new TableColumn("Date Added");

        col1.setMinWidth(250);
        col2.setMinWidth(200);

        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("date"));

        expenseTable.getColumns().addAll(col1, col2);
    }

    private void loadPaymentTableData(ResultSet rs){
        payments.clear();
        try {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getString("id"),
                        rs.getString("expense"),
                        rs.getString("date"),
                        rs.getString("dateOfPayment"),
                        rs.getString("amount"),
                        rs.getString("mode"),
                        rs.getString("ref"),
                        rs.getString("mirage"),
                        rs.getString("user")
                        )
                );
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setPaymentTable(){
        paymentsTable.setItems(payments);
        paymentsTable.setEditable(true);

        TableColumn col10 = new TableColumn("Payment Date");
        TableColumn col2 = new TableColumn("Payment For");
        TableColumn col3 = new TableColumn("Cost/Amount (MK)");
        TableColumn col4 = new TableColumn("Mode Of Payment");
        TableColumn col5 = new TableColumn("Reference");
        TableColumn col6 = new TableColumn("Date Added");
        TableColumn col7 = new TableColumn("Recorded By");
        TableColumn col8 = new TableColumn("Mirage/Description");

        col10.setMinWidth(70);
        col2.setMinWidth(150);
        col3.setMinWidth(130);
        col4.setMinWidth(130);
        col5.setMinWidth(70);
        col6.setMinWidth(150);
        col7.setMinWidth(70);
        col8.setMinWidth(120);

        col10.setCellValueFactory(new PropertyValueFactory<>("dateOfPayment"));
        col2.setCellValueFactory(new PropertyValueFactory<>("expense"));
        col3.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col4.setCellValueFactory(new PropertyValueFactory<>("mode"));
        col5.setCellValueFactory(new PropertyValueFactory<>("ref"));
        col6.setCellValueFactory(new PropertyValueFactory<>("date"));
        col7.setCellValueFactory(new PropertyValueFactory<>("user"));
        col8.setCellValueFactory(new PropertyValueFactory<>("mirage"));

        paymentsTable.getColumns().addAll(col2, col3, col4, col8, col5,  col10, col6, col7);
    }

    private void autoFillPaymentFields() {
        Payment p = paymentsTable.getSelectionModel().getSelectedItem();

        if(p != null){
            expenseCombo.getSelectionModel().select(p.getExpense());
            amountTxt.setText(p.getAmount());
            modeCombo.getSelectionModel().select(p.getMode());
            refTxt.setText(p.getRef());
            mirageTxt.setText(p.getMirage());
            dateTxt.setValue(LocalDate.parse(p.getDateOfPayment()));
        }
    }

    private void setLabels() {
        ObservableList<Payment> data = paymentsTable.getItems();
        totalRecordsLbl.setText(""+data.size());

        double total = 0.0;

        if(data.size() > 0){
            for(int i = 0; i < data.size() ; i++){
                total += Double.parseDouble(data.get(i).getAmount());
            }
        }

        totalPaymentsLbl.setText("K"+total);
    }


}
