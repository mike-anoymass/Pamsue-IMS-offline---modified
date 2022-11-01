package drivingschoolfmis;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;
import static drivingschoolfmis.NotificationClass.showNotification;

public class StaffPaymentsController {
    
    Employee emp;
    ObservableList<Payment> payments = FXCollections.observableArrayList();
    ResultSet rs;
    FilteredList<Payment> filteredPaymentData = new FilteredList<>(payments, e -> true);

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Payment> receiptsTable;

    @FXML
    private Label totalLbl;

    @FXML
    private Label titleLbl;

    @FXML
    private Label departmentLbl;

    @FXML
    private Label phoneLbl;

    @FXML
    private Label salaryLbl;

    @FXML
    private TextField filterTxt;

    @FXML
    void deleteAction(ActionEvent event) {
        Payment p = receiptsTable.getSelectionModel().getSelectedItem();
        if(p != null){
            int response = PaymentQueries.deletePayment(p.getId());

            if (response != 404) {
                loadPaymentTableData();
                setLabels();
                showNotification("Payment Deleted Successfully");
            }
        }else{
            makeAlert("warning" , "Nothing to delete\nPlease Select a Payment from the table");
        }
    }

    @FXML
    void filterTxtOnKeyReleased(KeyEvent event) {
        filterTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredPaymentData.setPredicate((Predicate<? super Payment>) p -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (p.getAmount().toLowerCase().contains(lowerCaseFilter)) {
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
                }else if (p.getMirage().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;

            });
        });
        SortedList<Payment> sortedData = new SortedList<>(filteredPaymentData);
        sortedData.comparatorProperty().bind(receiptsTable.comparatorProperty());
        receiptsTable.setItems(sortedData);
        setLabels();
    }

    @FXML
    void printAction(ActionEvent event) {
        ObservableList<Payment> data = receiptsTable.getItems();
        if(data.size() > 0){
            try {
                Document doc = new Document();
                String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                        "\\" + schoolName + " FMIS\\"+
                        emp.getFullName() +" payment History" + System.currentTimeMillis() + ".pdf";

                PdfWriter.getInstance(doc, new FileOutputStream(fileName));
                doc.open();
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("img/driving_32.png");
                image.scaleToFit(120, 90);
                doc.add(image);
                doc.add(new Paragraph(schoolName + " Driving School - Fees Payment Report",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20, Font.BOLD, BaseColor.DARK_GRAY))
                );
                doc.add(new Paragraph("Printed by "
                        + LoginDocumentController.firstname + " " + LoginDocumentController.lastname +
                        " on "+ new Date().toString()
                ));
                doc.add(Chunk.NEWLINE);
                doc.add(new LineSeparator());
                doc.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(2);
                PdfPCell titleCell = new PdfPCell(new Paragraph("Employee Details"));
                titleCell.setColspan(6);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table.addCell(titleCell);
                table.addCell("Employee ID:");
                table.addCell(emp.getId());
                table.addCell("Employee Name:");
                table.addCell(emp.getFullName());
                table.addCell("Department:");
                table.addCell(emp.getDepartment());
                table.addCell("Phone Number:");
                table.addCell(emp.getPhone());
                table.addCell("Location:");
                table.addCell(emp.getLocation());

                PdfPTable table0 = new PdfPTable(2);
                PdfPCell titleCell0 = new PdfPCell(new Paragraph("Payment Summary"));
                titleCell0.setColspan(4);
                titleCell0.setBorder(Rectangle.NO_BORDER);
                titleCell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell0.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table0.addCell(titleCell0);
                table0.addCell("Salary (MK): ");
                table0.addCell(emp.getSalaryDesired());
                table0.addCell("Sum of Payments: ");
                table0.addCell(totalLbl.getText());

                PdfPTable table1 = new PdfPTable(6);
                PdfPCell titleCell1 = new PdfPCell(new Paragraph("Payment History"));
                titleCell1.setColspan(12);
                titleCell1.setBorder(Rectangle.NO_BORDER);
                titleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);


                table1.addCell(titleCell1);
                table1.addCell("Payment No");
                table1.addCell("Date of Payment");
                table1.addCell("Description");
                table1.addCell("Payment Mode");
                table1.addCell("Reference");
                table1.addCell("Amount (MK)");

                for(Payment r : data){
                    table1.addCell(r.getId());
                    table1.addCell(r.getDateOfPayment());
                    table1.addCell(r.getMirage());
                    table1.addCell(r.getMode());
                    table1.addCell(r.getRef());
                    table1.addCell(r.getAmount());
                }

                doc.add(table);
                doc.add(Chunk.NEWLINE);
                doc.add(table0);
                doc.add(Chunk.NEWLINE);
                doc.add(table1);
                doc.add(Chunk.NEWLINE);

                doc.add(new Paragraph("Pamseu FMIS @ " + LocalDate.now().getYear(),
                        FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.NORMAL, BaseColor.DARK_GRAY))
                );

                doc.add(new Paragraph("*Anoymass Programs",
                        FontFactory.getFont(FontFactory.TIMES_ITALIC, 9, Font.NORMAL, BaseColor.DARK_GRAY))
                );

                doc.close();

                makeAlert("information","Report Saved Successfully !\n\n" +
                        "Location: *"+fileName+"*");
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
    void recordPaymentAction(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffPaymentForm.fxml").openStream());
            StaffPaymentFormController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffPaymentStage.setScene(scene);
            StageManager.staffPaymentStage.getIcons().add(new javafx.scene.image.Image("img/receipt_16.png"));
            StageManager.staffPaymentStage.setTitle("Payment for - " + emp.getFullName());

            StageManager.staffPaymentStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void tableOnMouseClickedAction(MouseEvent event) {

    }

    public void initialize(Employee emp) {
        anchor.getStylesheets().addAll(getClass().getResource("students.css").toExternalForm());
        this.emp = emp;
        loadPaymentTableData();
        setTable();
        setLabels();
    }

    private void setTable() {
        receiptsTable.setItems(payments);
        receiptsTable.setEditable(true);

        TableColumn col2 = new TableColumn("Description");
        TableColumn col3 = new TableColumn("Payment Date");
        TableColumn col4 = new TableColumn("Payment Mode");
        TableColumn col5 = new TableColumn("Reference");
        TableColumn col6 = new TableColumn("Date Added");
        TableColumn col7 = new TableColumn("Amount (MK)");
        TableColumn col8 = new TableColumn("Added By");

        col2.setMinWidth(100);
        col3.setMinWidth(120);
        col4.setMinWidth(110);
        col5.setMinWidth(60);
        col6.setMinWidth(150);
        col7.setMinWidth(70);
        col8.setMinWidth(70);

        col2.setCellValueFactory(new PropertyValueFactory<>("mirage"));
        col3.setCellValueFactory(new PropertyValueFactory<>("dateOfPayment"));
        col4.setCellValueFactory(new PropertyValueFactory<>("mode"));
        col5.setCellValueFactory(new PropertyValueFactory<>("ref"));
        col6.setCellValueFactory(new PropertyValueFactory<>("date"));
        col7.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col8.setCellValueFactory(new PropertyValueFactory<>("user"));

        receiptsTable.getColumns().addAll(col2, col3, col4, col5, col6,  col7, col8);
    }

    private void setLabels() {
        double total = 0.0;
        titleLbl.setText("Name: " + emp.getFullName());
        departmentLbl.setText(emp.getDepartment());
        phoneLbl.setText(emp.getPhone());
        salaryLbl.setText("Salary: MK" + emp.getSalaryDesired());

        ObservableList<Payment> payments = receiptsTable.getItems();
        if(payments.size() > 0){
            for(Payment payment : payments){
                total += Double.parseDouble(payment.getAmount());
            }
        }

        totalLbl.setText("MK" + total);
    }

    private void loadPaymentTableData(){
        payments.clear();
        try {
            rs = PaymentQueries.getPaymentsFor(emp.getFullName());
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
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
