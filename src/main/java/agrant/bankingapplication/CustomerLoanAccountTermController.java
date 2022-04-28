package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerLoanAccountTermController extends Controller {
    private boolean isMortgage;

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button makePaymentButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private TextField paymentAmountField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void makePaymentClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());
        String checkAccID = String.format("%s_c", getLoginController().getCurrentUser().getSSN());

        //Check if user has a valid loan already
        if(getLoginController().hasValidLoanAccount(accID)) {
            try{
                //get current date
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String currentDate = date.format(formatters);
                //parse next payment due date to compare to current date
                LocalDate dueDate = LocalDate.parse(
                        getLoginController().findLoanByID(accID).getNextPaymentDueDate(),
                        formatters
                );

                Loans loan = getLoginController().findLoanByID(accID);
                double payAmt = Double.parseDouble(paymentAmountField.getText());

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedPayAmt = formatter.format(payAmt);

                //proceed if loan type matches the button the user clicked
                if((loan.getLoanType().equals("Mortgage") && isMortgage) ||
                        (loan.getLoanType().equals("Short-Term") && !isMortgage)){
                    //Pay specified amount to loan account

                    Loans loanPayment = getLoginController().findLoanByID(accID);
                    Checking fromChecking = getLoginController().findCheckingByID(checkAccID);

                    //Check that the checking has enough money
                    if (fromChecking.getCurrentBalance() >= (payAmt + 75)) {
                        //We have enough! Withdraw first.
                        fromChecking.oneTimeWithdraw(payAmt);
                        //deposit next
                        if(loanPayment.makeLoanPayment(payAmt)) {

                            //Create two transaction objects
                            Transactions newTrans1 = new Transactions(
                                    accID,
                                    "transfer",
                                    "Made payment of  " + payAmt + " to account " + accID,
                                    currentDate
                            );

                            Transactions newTrans2 = new Transactions(
                                    accID,
                                    "payment",
                                    "Made payment of " + payAmt + " from account " + checkAccID,
                                    currentDate
                            );

                            //add transactions to log
                            getLoginController().getTransactionLog().add(newTrans1);
                            getLoginController().getTransactionLog().add(newTrans2);

                            //add late penalty to transaction log if overdue
                            if (dueDate.isBefore(date)) {
                                //pay late fee
                                if (date.isAfter(dueDate)) {
                                    double lFee = 75.00;
                                    fromChecking.oneTimeWithdraw(lFee);
                                }

                                //create
                                Transactions pastDueTrans = new Transactions(
                                        checkAccID,
                                        "penalty",
                                        "Penalized  $75.00 from account " + checkAccID,
                                        currentDate
                                );
                                getLoginController().getTransactionLog().add(pastDueTrans);
                            }

                            if(loanPayment.getCurrentBalance() == 0) {
                                getLoginController().getLoanApplications().remove(getLoginController().findLoanByID(accID));

                                //write the data
                                getLoginController().writeBankData();

                                // create a confirmation screen
                                ConfirmationController confirmationController = new ConfirmationController(
                                        getCurrentStage(),
                                        getLoginController(),
                                        getMainPage(),
                                        "Congratulations, you paid " + formattedPayAmt +
                                                " from account number " + checkAccID + " loan " +
                                                accID + " and you have fully paid off your loan! Congrats!"
                                );

                                confirmationController.showStage();
                            }else{
                                //write the data
                                getLoginController().writeBankData();

                                // create a confirmation screen
                                ConfirmationController confirmationController = new ConfirmationController(
                                    getCurrentStage(),
                                    getLoginController(),
                                    getMainPage(),
                                    "Congratulations, you paid " + formattedPayAmt +
                                            " from account number " + checkAccID + " loan " +
                                            accID + "."
                                );

                                confirmationController.showStage();
                            }
                        }
                    } else {
                        //we do not have enough money
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Money Not Transferred");
                        a.setHeaderText("Not enough money in checking.");
                        a.setContentText("Please ensure you have enough money in checking.");

                        // show the dialog
                        a.show();
                    }
                }else{
                    //User chose wrong account
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Unable to proceed.");
                    a.setHeaderText("Account mismatch.");
                    a.setContentText("Account type differed from one chosen. Please go back and try again.");
                    a.show();
                }
            }catch(NumberFormatException nfe){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Payment Not Processed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");
                a.show();
            }
        }else{
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Unable to proceed.");
            a.setHeaderText("Account not found");
            a.setContentText("Unable to locate account.");
            a.show();
        }
    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    private String findAccountInfo(String text) {
        String returnString = "Error: No Account found";

        for(Loans loans : getLoginController().getLoansData()) {
            if(loans.getAccountId().equals(text)) {
                returnString = loans.toString();
                break;
            }
        }
        return returnString;
    }

    public CustomerLoanAccountTermController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController,
        boolean isMortgage
    ) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-loan-term.fxml");
        this.setCurrentViewTitle("Loan Account Actions");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        this.isMortgage = isMortgage;
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_l");
    }



    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}