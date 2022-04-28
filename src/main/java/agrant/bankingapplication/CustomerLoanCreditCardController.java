package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
import agrant.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerLoanCreditCardController extends Controller {
    @FXML
    private Button makePurchaseButton;

    @FXML
    private Button payInFullButton;

    @FXML
    private Button payOverTimeButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void makePurchaseClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());

        CustomerCCPurchaseController customerCCPurchaseController = new CustomerCCPurchaseController(
                this.getCurrentStage(),
                this.getLoginController(),
            (CustomerOpeningController) this.getMainPage()
        );

        customerCCPurchaseController.showStage();
    }

    @FXML
    void payInFullClicked(ActionEvent event) {
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
                double payAmt = loan.getCurrentPaymentAmount();

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedPayAmt = formatter.format(payAmt);

                //proceed if loan type matches the button the user clicked
                if((loan.getLoanType().equals("Credit"))){
                    //Pay specified amount to loan account

                    Loans loanPayment = getLoginController().findLoanByID(accID);
                    Checking fromChecking = getLoginController().findCheckingByID(checkAccID);

                    //Check that the checking has enough money
                    if (fromChecking.getCurrentBalance() - payAmt >= 0) {
                        //We have enough! Withdraw first.
                        fromChecking.oneTimeWithdraw(payAmt);
                        //deposit next
                        if(loanPayment.ccOnetimePay(payAmt)) {

                            //Create two transaction objects
                            Transactions newTrans1 = new Transactions(
                                    checkAccID,
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

    @FXML
    void payOverTimeClicked(ActionEvent event) {
    }

    public CustomerLoanCreditCardController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-loan-credit-card.fxml");
        this.setCurrentViewTitle("Credit Card");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}