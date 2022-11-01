/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.jfoenix.controls.JFXTextField;
import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;
import static drivingschoolfmis.ReceiptsController.report;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javax.swing.filechooser.FileSystemView;

/**
 * FXML Controller class
 *
 * @author ANOYMASS
 */
public class SettlementController {

    @FXML
    private HBox formHbox;
    @FXML
    private TextField amountTxt;

    @FXML
    private JFXTextField loanFor;
    @FXML
    private JFXTextField acquiredOn;
    @FXML
    private JFXTextField amount;
    @FXML
    private JFXTextField balanceTxt;

    static ObservableList<LoanSettlement> settlements = FXCollections.observableArrayList();
    @FXML
    private TableView<LoanSettlement> table;

    private int loan_id;
    /**
     * Initializes the controller class.
     */
    public void initialize(String employee, Loan loan) {
        loan_id = loan.getLoan_id();
        loanFor.setText(employee.split("~")[1]);
        setFields(loan);
        setTable();
        loadSettlements(loan_id);

    }

    public void setFields(Loan loan) {
        acquiredOn.setText(loan.getDate());
        amount.setText(String.valueOf(loan.getAmount()));
        balanceTxt.setText(String.valueOf(loan.getBalance()));

        if (loan.getBalance() == 0) {
            formHbox.setVisible(false);
        } else if (!formHbox.isVisible()) {
            formHbox.setVisible(true);
        }
    }

    @FXML
    private void pay(ActionEvent event) {
        if (!amountTxt.getText().isEmpty()) {
            try {
                double d = Double.parseDouble(amountTxt.getText());
                if (d > Double.parseDouble(balanceTxt.getText()) || d < 0) {
                    makeAlert("error", "Amount entered is negative or more than the balance (MK" + balanceTxt.getText() + ")");
                } else if (!LoanQueries.addSettlement(new LoanSettlement(loan_id, d))) {
                    NotificationClass.showNotification("Loan Settlement added successfully!");
                    loadSettlements(loan_id);
                    setFields(new LoanQueries().getLoans(loan_id));
                }

            } catch (NumberFormatException nfe) {
                makeAlert("error", "Invalid amount - " + nfe);
            }
        } else {
            makeAlert("error", "Please enter amount to payback");
        }
    }

    public void setTable() {
        table.setItems(settlements);

        TableColumn col1 = new TableColumn("No.");
        TableColumn col2 = new TableColumn("Payment Date");
        TableColumn col3 = new TableColumn("Amount (MK)");

        col1.setMinWidth(50);
        col2.setMinWidth(150);
        col3.setMinWidth(100);

        table.getColumns().addAll(col1, col2, col3);
        //table.setEditable(true);

        col1.setCellValueFactory(new PropertyValueFactory<>("settlement_id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("date"));
        col3.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    public static void loadSettlements(int loan_id) {
        settlements.clear();
        settlements.addAll(new LoanQueries().getSettlements(loan_id));
    }

    @FXML
    private void print(ActionEvent event) {
        ObservableList<LoanSettlement> pdata = table.getItems();

        if(pdata.size() > 0){
            try {
                double totalPaid = Double.parseDouble(amount.getText()) - Double.parseDouble(balanceTxt.getText());
                Document doc = new Document();
                String fileName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                        "\\" + schoolName +
                        " FMIS\\" + loanFor.getText() +  "_LoanSettlement" + System.currentTimeMillis() + ".pdf";

                PdfWriter.getInstance(doc, new FileOutputStream(fileName));
                doc.open();
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("img/driving_32.png");
                image.scaleToFit(120, 90);
                doc.add(image);
                doc.add(new Paragraph(schoolName + " Driving School - Loan Settlement Report",
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
                PdfPCell titleCell0 = new PdfPCell(new Paragraph("Loan Details"));
                titleCell0.setColspan(4);
                titleCell0.setBorder(Rectangle.NO_BORDER);
                titleCell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell0.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table0.addCell(titleCell0);
                table0.addCell("Loan for:");
                table0.addCell(loanFor.getText());
                table0.addCell("Loan Acquired on:");
                table0.addCell(acquiredOn.getText());
                table0.addCell("Loan Amounting to (MK):");
                table0.addCell(amount.getText());
                table0.addCell("Total Paid (MK):");
                table0.addCell(String.valueOf(totalPaid));
                table0.addCell("Balance (MK):");
                table0.addCell(balanceTxt.getText());
                

                PdfPTable table1 = new PdfPTable(3);
                PdfPCell titleCell1 = new PdfPCell(new Paragraph("Payment History"));
                titleCell1.setColspan(6);
                titleCell1.setBorder(Rectangle.NO_BORDER);
                titleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);


                table1.addCell(titleCell1);
                table1.addCell("Settlement ID");
                table1.addCell("Payment Date");
                table1.addCell("Amount");
            

                for(LoanSettlement r : pdata){
                    table1.addCell(String.valueOf(r.getSettlement_id()));
                    table1.addCell(r.getDate());
                    table1.addCell(String.valueOf(r.getAmount()));
                }

                report(doc, fileName, table0, table1);
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
    private void delete(ActionEvent event) {
        try{
            LoanSettlement ls = table.getSelectionModel().getSelectedItem();
            if(new LoanQueries().delete(ls.getSettlement_id()) != 404){
                NotificationClass.showNotification("Loan has been deleted succesfully");
                 loadSettlements(loan_id);
                 setFields(new LoanQueries().getLoans(loan_id));   
            }
        }catch(NullPointerException ne){
            makeAlert("error", "Nothing to delete! Please select a record");
        }
        
    }

}
