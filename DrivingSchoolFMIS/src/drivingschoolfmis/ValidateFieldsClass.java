package drivingschoolfmis;

import java.util.regex.Pattern;

import static drivingschoolfmis.AlertClass.makeAlert;

public class ValidateFieldsClass {
    public boolean validateStudent(Student student, String discount) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        StringBuilder errors = new StringBuilder("");
        if (student.getName().isEmpty()) errors.append("-> Full Name\n");
        if (student.getPhone().isEmpty()) errors.append("-> Student Phone Number\n");
        if (student.getGuardianPhone().isEmpty()) errors.append("-> Guardian Phone Number\n");
        if (student.getAddress().isEmpty()) errors.append("-> Address/ Residential Area\n");
        if (student.getGender().isEmpty()) errors.append("-> Gender\n");
        if (student.getCourse() == null || student.getCourse().isEmpty()) errors.append("-> Course\n");
        if (student.getDateRegistered() == null || student.getDateRegistered().isEmpty()) errors.append("-> Date of Registration\n");
        if (student.getCourseType() == null) errors.append("-> Category\n");
        if (student.getLocation() == null) errors.append("-> Location\n");


        if (errors.length() == 0) {
            if (pattern.matcher(student.getPhone()).matches() & pattern.matcher(student.getGuardianPhone()).matches() ) {

                if(student.isAnyDiscount()){

                    //validating discount fee
                    if (discount.length() > 0) {
                        if (pattern.matcher(discount).matches()) {

                            Double fees = new CategoryFees().getCategoryFees(student.getCourse(), student.getCourseType());


                            if(Double.parseDouble(discount) > fees){
                                makeAlert("warning", "Invalid discount -> Discount(MK" +discount+ ") " +
                                        "is greater than fees(MK"+fees+")\n" +
                                        "Solution: Enter Discount less than MK" + fees);
                            }else{
                                return true;
                            }

                        }
                        else{
                            makeAlert("warning", "Discount Should be numeric");
                        }
                    } else {
                        makeAlert("warning", "Please Enter Discount");
                    }
                }else{
                    return true;
                }


            } else {
                makeAlert("warning", "Phone numbers should be numeric");
            }
        } else {
            makeAlert("warning", "Ops! You forgot to fill the following \n" + errors);
        }

        return false;
    }

    public boolean validateTest(Test test) {
        StringBuilder errors = new StringBuilder("");

        if (test.getTestName() == null || test.getTestName().isEmpty()) errors.append("-> Test taken\n");
        if (test.getPassOrFail().isEmpty()) errors.append("-> Result (Pass Or Fail)\n");
        if (test.getDateOfTest() == null || test.getDateOfTest().isEmpty()) errors.append("-> Date of test\n");


        if (errors.length() == 0) {
           return true;
        } else {
            makeAlert("warning", "Ops! You forgot to fill the following \n" + errors);
        }

        return false;
    }

    public boolean validatePaymentFields(Payment p) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        StringBuilder errors = new StringBuilder("");

        if (p.getExpense() == null || p.getExpense().isEmpty()) errors.append("-> Expense\n");
        if (p.getAmount().isEmpty()) errors.append("-> Amount\n");
        if (p.getDateOfPayment() == null || p.getDateOfPayment().isEmpty()) errors.append("-> Date of payment\n");
        if (p.getMirage() == null || p.getMirage().isEmpty()) errors.append("-> Description (Payment Type)\n");

        if (errors.length() == 0) {
            if (pattern.matcher(p.getAmount()).matches()) {
                if(Double.parseDouble(p.getAmount()) > 0){
                    return true;
                }else{
                    makeAlert("warning", "Amount should not be less than zero");
                }
            } else {
                makeAlert("warning", "Amount should be numeric");
            }
        } else {
            makeAlert("warning", "Ops! You forgot to fill the following \n" + errors);
        }

        return false;
    }

    public boolean validateReceipt(Receipts receipt, Double balance) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        StringBuilder errors = new StringBuilder("");

        if (receipt.getAmount().isEmpty()) errors.append("-> Amount\n");
        if (receipt.getBpo() == null || receipt.getBpo().isEmpty()) errors.append("-> Payment for\n");
        if (receipt.getMop() == null || receipt.getMop().isEmpty()) errors.append("-> Mode of payment\n");
        if (receipt.getDateOfReceipt() == null || receipt.getDateOfReceipt().isEmpty())
            errors.append("-> Date of receipt\n");

        if (errors.length() == 0 ) {

            if (pattern.matcher(receipt.getAmount()).matches() ) {
                if(Double.parseDouble(receipt.getAmount()) > 0){
                    if(Double.parseDouble(receipt.getAmount()) <= balance){
                        return true;
                    }else {
                        makeAlert("warning", "The amount you entered (K" + receipt.getAmount() +
                                ") is greater than balance (K"+ balance +")\n" +
                                "Please enter amount less or equal to the balance");
                    }
                }else{
                    makeAlert("warning", "Amount should not be less than zero");
                }

            } else {
                makeAlert("warning", "Amount should be numeric and not less than zero");
            }
        } else {
            makeAlert("warning", "Ops! You forgot to fill the following \n" + errors);
        }

        return false;
    }

    public boolean validateStaff(Employee emp, NextOfKin nextOfKin) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        StringBuilder errors = new StringBuilder("");
        if (emp.getFullName().isEmpty()) errors.append("-> Employee Name\n");
        if (emp.getDob() == null || emp.getDob().isEmpty())
            errors.append("-> Date of Birth\n");
        if (emp.getGender().isEmpty()) errors.append("-> Gender\n");
        if (emp.getPhone().isEmpty()) errors.append("-> Employee Phone Number\n");
        if (emp.getPhysicalAddress().isEmpty()) errors.append("-> Physical/ Residential Area\n");
        if (emp.getDepartment() == null || emp.getDepartment().isEmpty())
            errors.append("-> Department\n");
        if (emp.getEmployeeStatus() == null || emp.getEmployeeStatus().isEmpty())
            errors.append("-> Employment Status\n");
        if (emp.getWorkingHours() == null || emp.getWorkingHours().isEmpty())
            errors.append("-> Working Hours\n");
        if (emp.getSalaryDesired().isEmpty()) errors.append("-> Salary Desired\n");
        if (emp.getLocation() == null) errors.append("-> Location\n");
        if (emp.getDateRegistered() == null || emp.getDateRegistered().isEmpty())
            errors.append("-> Date of Registration\n");
        if (nextOfKin.getName().isEmpty()) errors.append("-> Guardian Name\n");
        if (nextOfKin.getPhone().isEmpty()) errors.append("-> Guardian Phone Number\n");
        if (nextOfKin.getPhysicalAddress().isEmpty()) errors.append("-> Guardian Address/ Residential Area\n");

        if (errors.length() == 0) {
            if (pattern.matcher(emp.getPhone()).matches() &
                    pattern.matcher(nextOfKin.getPhone()).matches() &
                    pattern.matcher(emp.getSalaryDesired()).matches()
            ) {
                return true;
            } else {
                makeAlert("warning", "Phone numbers and salary should be numeric");
            }
        } else {
            makeAlert("warning", "Ops! You forgot to fill the following \n" + errors);
        }

        return false;
    }
}
