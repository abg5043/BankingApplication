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

public class CustomerLoanCreditCardController extends Controller {
    @FXML
    private Button makePurchaseButton;

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button payInFullButton;

    @FXML
    private Button oneTimePay;

    @FXML
    private TextField moneyField;

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

        //Create and show confirmation and await user input to avoid accidental financial catastrophe
        Alert confirmChoice = new Alert(Alert.AlertType.CONFIRMATION);
        confirmChoice.setTitle("Confirm Choice.");
        confirmChoice.setHeaderText("Are you sure you wish to proceed?");
        confirmChoice.setContentText("Choose OK to pay account in full.");

        confirmChoice.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
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

                        //Ensure account has enough money after potential late fee
                        if((getLoginController().findCheckingByID(accID).getCurrentBalance() >= (payAmt + 75))){

                            //this formats the money amount into currency
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            String formattedPayAmt = formatter.format(payAmt);

                            //Pay loan
                            payCC(loan, payAmt, accID, checkAccID, dueDate, date, currentDate, formattedPayAmt, formatter);
                        }else{
                            Alert a = new Alert(Alert.AlertType.WARNING);
                            a.setTitle("Unable to Make Payment.");
                            a.setHeaderText("Account balance too low");
                            a.setContentText("Unable to locate account.");
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
        });
    }

    @FXML
    void oneTimePayClicked(ActionEvent event) {
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
                double payAmt = Double.parseDouble(moneyField.getText());

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedPayAmt = formatter.format(payAmt);

                //Pay loan
                payCC(loan, payAmt, accID, checkAccID, dueDate, date, currentDate, formattedPayAmt, formatter);

                //Create transaction object
                Transactions newTrans = new Transactions(
                        accID,
                        "payment",
                        "Deposited " + formattedPayAmt + " into account.",
                        currentDate
                );

                //add transaction to log
                getLoginController().getTransactionLog().add(newTrans);
                //write the data
                getLoginController().writeBankData();
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

    private void payCC(Loans loan, double payAmt, String accID, String checkAccID, LocalDate dueDate, LocalDate date, String currentDate, String formattedPayAmt, NumberFormat formatter) {
        //proceed if loan type matches the button the user clicked
        if(loan.getLoanType().equals("Credit")){
            if(getLoginController().hasValidCheckingAccount(checkAccID)) {

                //check if the payment amount is more than the loan amount
                if (loan.getCurrentBalance() >= payAmt) {
                    //Pay specified amount to loan account

                    Checking fromChecking = getLoginController().findCheckingByID(checkAccID);

                    //check that payment isn't late
                    //add late penalty to transaction log if overdue
                    if (dueDate.isBefore(date)) {
                        payAmt += 75;
                    }

                    //Check that the checking has enough money
                    if (fromChecking.getCurrentBalance() - payAmt >= 0) {
                        //We have enough! Withdraw first.
                        fromChecking.oneTimeWithdraw(payAmt);
                        //deposit next
                        if (loan.ccOnetimePay(payAmt)) {
                            //update payment date
                            loan.setLastPaymentMade(currentDate);

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

                            if (loan.getCurrentBalance() == 0) {
                                getLoginController().getLoansData().remove(getLoginController().findLoanByID(accID));

                                //write the data
                                getLoginController().writeBankData();

                                // create a confirmation screen
                                ConfirmationController confirmationController = new ConfirmationController(
                                        getCurrentStage(),
                                        getLoginController(),
                                        getMainPage(),
                                        "Congratulations, you paid " + formattedPayAmt +
                                                " from account number " + checkAccID + " to loan " +
                                                accID + " and you have fully paid off your loan!"
                                );

                                confirmationController.showStage();
                            } else {
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
                        //There wasn't enough money; must try to overdraft
                        double originalCheckingBalance = fromChecking.getCurrentBalance();
                        double overdraftAmount = payAmt - originalCheckingBalance;

                        if (fromChecking.getAccountType().equals("Regular")) {
                            overdraftAmount += 0.5; //includes 50 cent charge
                        }
                        /*
                         * Check if there is a backup savings and that the savings
                         * has enough to cover the rest
                         */
                        if (
                                !fromChecking.getBackupAccountId().equals("n/a") &&
                                        (overdraftAmount <= getLoginController().findSavingsByID(fromChecking.getBackupAccountId()).getAccountBalance())
                        ) {
                            Savings backUpSavings = getLoginController().findSavingsByID(fromChecking.getBackupAccountId());

                            //Withdraw from checking and savings
                            fromChecking.setCurrentBalance(0);
                            fromChecking.setInterest("n/a");
                            fromChecking.setAccountType("Regular");
                            backUpSavings.withdraw(overdraftAmount);
                            fromChecking.setOverdrafts(fromChecking.getOverdrafts() + 1);

                            //deposit into cc
                            loan.ccOnetimePay(payAmt);

                            //update payment date
                            loan.setLastPaymentMade(currentDate);

                            String formattedChecking = formatter.format(originalCheckingBalance);
                            String formattedOverdraft = formatter.format(overdraftAmount);


                            //Create three transaction objects
                            Transactions newTrans1 = new Transactions(
                                    fromChecking.getAccountId(),
                                    "withdraw",
                                    "Withdrew " + formattedChecking + " from account " + fromChecking.getAccountId() + ".",
                                    currentDate
                            );

                            Transactions newTrans2 = new Transactions(
                                    backUpSavings.getAccountId(),
                                    "withdraw",
                                    "Withdrew " + formattedOverdraft + " from account " + backUpSavings.getAccountId() + ".",
                                    currentDate
                            );

                            Transactions newTrans3 = new Transactions(
                                    accID,
                                    "payment",
                                    "Made payment of " + payAmt + ".",
                                    currentDate
                            );

                            //add transaction to log
                            getLoginController().getTransactionLog().add(newTrans1);
                            getLoginController().getTransactionLog().add(newTrans2);
                            getLoginController().getTransactionLog().add(newTrans3);


                            if (loan.getCurrentBalance() == 0) {
                                getLoginController().getLoansData().remove(getLoginController().findLoanByID(accID));

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
                            } else {
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

                        } else {
                            //There wasn't a linked account or there was not enough in that backup account
                            // create an alert
                            Alert a = new Alert(Alert.AlertType.WARNING);
                            a.setTitle("Not enough money.");
                            a.setHeaderText("Payment not processed.");
                            a.setContentText("Not enough money in checking account, even with overdraft.");

                            // show the dialog
                            a.show();
                        }
                    }
                } else {
                    //loan is less than the payment
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Unable to proceed.");
                    a.setHeaderText("Payment amount error.");
                    a.setContentText("Cannot pay more than loan is for.");
                    a.show();
                }
            }else {
                //Customer does not have checking account
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Unable to proceed.");
                a.setHeaderText("Cannot retrieve account.");
                a.setContentText("Checking account not found. Please have a manager create a checking account and try again.");
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

    public CustomerLoanCreditCardController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-loan-credit-card.fxml");
        this.setCurrentViewTitle("Credit Card");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_l");
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}