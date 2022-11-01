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
import javafx.scene.image.Image;
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

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;
import static drivingschoolfmis.NotificationClass.showNotification;

public class FeesHistoryController {
    Student student;
    ResultSet rs;
    ObservableList<Receipts> data = FXCollections.observableArrayList();
    private FilteredList<Receipts> filteredData = new FilteredList<>(data, e->true);

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Receipts> receiptsTable;

    @FXML
    private Label totalFeesLbl;

    @FXML
    private Label discountLbl;

    @FXML
    private Label govtLbl;

    @FXML
    private Label courseFeeLbl;

    @FXML
    private Label totalPaidLbl;

    @FXML
    private Label balanceLbl;

    @FXML
    private Label titleLbl;

    @FXML
    private Label courseLbl;

    @FXML
    private Label categoryLbl;

    @FXML
    private TextField filterTxt;

    @FXML
    void deleteAction(ActionEvent event) {
        Receipts receipt = receiptsTable.getSelectionModel().getSelectedItem();

        if(receipt != null){
            int deleted = ReceiptQueries.deleteReceipt(receipt.getId());

            if (deleted != 404) {
                loadData();
                showNotification("Receipt Deleted Successfully");
                setLabels();
            }

        }else{
            makeAlert("error", "Nothing to delete \nPlease select a record from the table");
        }
    }

    @FXML
    void exploreReceipt(ActionEvent event) {

        try {
            Receipts r = receiptsTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("ReceiptDetails.fxml").openStream());
            ReceiptDetailsController controller = loader.getController();
            controller.initialize(student, r, balanceLbl.getText());

            Scene scene = new Scene(root);
            StageManager.receiptDetails.setScene(scene);
            StageManager.receiptDetails.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.receiptDetails.setTitle("Receipt for " + student.getName());

            StageManager.receiptDetails.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Nothing to display \nPlease select a record");
        }
    }

    @FXML
    void filterTxtOnKeyReleased(KeyEvent event) {
        filterTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Receipts>) receipt -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (receipt.getId().contains(newValue)) {
                    return true;
                } else if (receipt.getMop().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getRef().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getUser().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getDateOfReceipt().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getBpo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (receipt.getAmount().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;

            });
        });
        SortedList<Receipts> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(receiptsTable.comparatorProperty());
        receiptsTable.setItems(sortedData);
    }

    @FXML
    void printAction(ActionEvent event) {
        if(data.size() > 0){
            try {
                Document doc = new Document();
                String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                        "\\" + schoolName + " FMIS\\"+
                        student.getName() +" Fees payment History" + System.currentTimeMillis() + ".pdf";

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
                PdfPCell titleCell = new PdfPCell(new Paragraph("Student Details"));
                titleCell.setColspan(6);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table.addCell(titleCell);
                table.addCell("Student ID:");
                table.addCell(student.getId());
                table.addCell("Student Name:");
                table.addCell(student.getName());
                table.addCell("Course:");
                table.addCell(student.getCourse());
                table.addCell("Course Type:");
                table.addCell(student.getCourseType());
                table.addCell("Phone Number:");
                table.addCell(student.getPhone());
                table.addCell("Location:");
                table.addCell(student.getLocation());

                PdfPTable table0 = new PdfPTable(2);
                PdfPCell titleCell0 = new PdfPCell(new Paragraph("Fees Summary"));
                titleCell0.setColspan(4);
                titleCell0.setBorder(Rectangle.NO_BORDER);
                titleCell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell0.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table0.addCell(titleCell0);
                table0.addCell("Discount:");
                table0.addCell(discountLbl.getText());
                table0.addCell("Government Fee");
                table0.addCell(govtLbl.getText());
                table0.addCell("Course Fee");
                table0.addCell(courseFeeLbl.getText());
                table0.addCell("Total Fees for this Student:");
                table0.addCell(totalFeesLbl.getText());
                table0.addCell("Total Paid:");
                table0.addCell(totalPaidLbl.getText());
                table0.addCell("Your Balance:");
                table0.addCell(balanceLbl.getText());
                table0.addCell("Remarks:");

                if(Double.parseDouble(balanceLbl.getText()) > 0){
                    table0.addCell("Please settle the balance");
                }else{
                    table0.addCell("Thank you for completing your fees");
                }

                PdfPTable table1 = new PdfPTable(6);
                PdfPCell titleCell1 = new PdfPCell(new Paragraph("Payment History"));
                titleCell1.setColspan(12);
                titleCell1.setBorder(Rectangle.NO_BORDER);
                titleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);


                table1.addCell(titleCell1);
                table1.addCell("Receipt No");
                table1.addCell("Date of Payment");
                table1.addCell("Fees for");
                table1.addCell("Mode of Payment");
                table1.addCell("Reference");
                table1.addCell("Amount (MK)");

                for(Receipts r : data){
                    table1.addCell(r.getId());
                    table1.addCell(r.getDateOfReceipt());
                    table1.addCell(r.getBpo());
                    table1.addCell(r.getMop());
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
                        "Location of the report: *"+fileName+"*");
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
        ((Node)event.getSource()).getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("ReceiptForm.fxml").openStream());
            ReceiptFormController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.receiptStage.setScene(scene);
            StageManager.receiptStage.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.receiptStage.setTitle("Record Fees Receipt for " + student.getName());

            StageManager.receiptStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void tableOnMouseClickedAction(MouseEvent event) {

    }

    public void initialize(Student student){
        this.student = student;
        anchor.getStylesheets().add(getClass().getResource("students.css").toExternalForm());
        loadData();
        setTableView();

        setLabels();
    }

    private void setLabels() {
        titleLbl.setText("Name: " + student.getName());
        courseLbl.setText("Course: " + student.getCourse());
        categoryLbl.setText("Category: " + student.getCourseType());

        FeesResults feesResults = new FeesCalculationsClass().FeesCalculations(student);
        
        totalFeesLbl.setText(feesResults.getTotalFee());
        totalPaidLbl.setText(feesResults.getTotalPaid());
        balanceLbl.setText(feesResults.getBalance());
        discountLbl.setText(feesResults.getDiscountFee());
        govtLbl.setText(feesResults.getGovtFee());
        courseFeeLbl.setText("Course Fee: " + feesResults.getCourseFee());
    }

    private void setTableView() {
        receiptsTable.setItems(data);

        TableColumn col1 = new TableColumn("Receipt No.");
        TableColumn col22= new TableColumn("Amount (MK)");
        TableColumn col3 = new TableColumn("Payment Date");
        TableColumn col4 = new TableColumn("Payment For");
        TableColumn col5 = new TableColumn("Payment Mode");
        TableColumn col6 = new TableColumn("Reference");
        TableColumn col7 = new TableColumn("Received By");
        TableColumn col8 = new TableColumn("Created On");

        col1.setMinWidth(100);
        col22.setMinWidth(100);
        col3.setMinWidth(150);
        col4.setMinWidth(120);
        col5.setMinWidth(120);
        col6.setMinWidth(120);
        col7.setMinWidth(100);
        col8.setMinWidth(150);

        receiptsTable.getColumns().addAll(col1, col22, col3, col4, col5, col6, col7,  col8);

        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col22.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col3.setCellValueFactory(new PropertyValueFactory<>("dateOfReceipt"));
        col4.setCellValueFactory(new PropertyValueFactory<>("bpo"));
        col5.setCellValueFactory(new PropertyValueFactory<>("mop"));
        col6.setCellValueFactory(new PropertyValueFactory<>("ref"));
        col7.setCellValueFactory(new PropertyValueFactory<>("user"));
        col8.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadData() {
        data.clear();
        try{
            rs = ReceiptQueries.getReceiptsFor(student.getId());
            while(rs.next()){
                data.add(new Receipts(
                        rs.getString("receiptNo"),
                        rs.getString("date"),
                        rs.getString("dateOfPayment"),
                        rs.getString("amount"),
                        rs.getString("modeOfPayment"),
                        rs.getString("paymentOf"),
                        rs.getString("user"),
                        rs.getString("reference"),
                        ""
                ));
            }

        }catch(SQLException ex){
            System.err.println(ex);
        }

    }

}
