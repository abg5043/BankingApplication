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
    private final boolean isMortgage;

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button makePaymentButton;

    @FXML
    private Button monthlyPaymentButton;

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
            //check if user has a valid checking account to pay from
            if(getLoginController().hasValidCheckingAccount(accID.substring(0,9) + "_c")) {
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
                    if((loan.getLoanType().equals("Mortgage-15") && isMortgage) ||
                        (loan.getLoanType().equals("Mortgage-30") && isMortgage) ||
                        (loan.getLoanType().equals("Short-Term") && !isMortgage)){
                        //Pay specified amount to loan account

                        Checking fromChecking = getLoginController().findCheckingByID(checkAccID);

                        //check that payment isn't late
                        //add late penalty to transaction log if overdue
                        if (dueDate.isBefore(date)) {
                            payAmt += 75;
                        }

                        //Check that the checking has enough money
                        if (fromChecking.getCurrentBalance() - payAmt >= 0) {
                            //We have enough!
                            //deposit first if you don't overpay
                            if(loan.makeLoanPayment(payAmt)) {
                                //Withdraw next
                                fromChecking.oneTimeWithdraw(payAmt);

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

                                if(loan.getCurrentBalance() == 0) {
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

                                //deposit into loan acc if we aren't overpaying
                                if(loan.makeLoanPayment(payAmt)) {
                                    //Withdraw from checking and savings
                                    fromChecking.setCurrentBalance(0);
                                    fromChecking.setInterest("n/a");
                                    fromChecking.setAccountType("Regular");
                                    backUpSavings.withdraw(overdraftAmount);
                                    fromChecking.setOverdrafts(fromChecking.getOverdrafts() + 1);

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
            } else {
                //does not have valid checking
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Payment Not Processed");
                a.setHeaderText("No checking account");
                a.setContentText("Please ensure you have a valid checking account to pay with.");
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
    void monthlyPaymentClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());
        String checkAccID = String.format("%s_c", getLoginController().getCurrentUser().getSSN());

        //Check if user has a valid loan already
        if(getLoginController().hasValidLoanAccount(accID)) {
            //check if there is a valid checking account
            if (getLoginController().hasValidCheckingAccount(accID.substring(0,9) + "_c")) {
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

                    double payAmt = Double.parseDouble(String.valueOf(getLoginController().findLoanByID(accID).getCurrentPaymentAmount()));

                    //this formats the money amount into currency
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    String formattedPayAmt = formatter.format(payAmt);

                    //proceed if loan type matches the button the user clicked
                    if((loan.getLoanType().equals("Mortgage-15") && isMortgage) ||
                        (loan.getLoanType().equals("Mortgage-30") && isMortgage) ||
                        (loan.getLoanType().equals("Short-Term") && !isMortgage)){
                        //Pay specified amount to loan account

                        Checking fromChecking = getLoginController().findCheckingByID(checkAccID);

                        //check that payment isn't late
                        //add late penalty to transaction log if overdue
                        if (dueDate.isBefore(date)) {
                            payAmt += 75;
                        }

                        //Check that the checking has enough money
                        if (fromChecking.getCurrentBalance() - payAmt >= 0) {
                            //we have enough!

                            //deposit first if you are not overpaying loan
                            if(loan.makeMonthlyPayment()) {
                                //We have enough! Withdraw next.
                                fromChecking.oneTimeWithdraw(payAmt);


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

                                if(loan.getCurrentBalance() == 0) {
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
                                //write the data
                                getLoginController().writeBankData();
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

                                //deposit into loan acc if we aren't overpaying
                                if(loan.makeMonthlyPayment()) {
                                    //Withdraw from checking and savings
                                    fromChecking.setCurrentBalance(0);
                                    fromChecking.setInterest("n/a");
                                    fromChecking.setAccountType("Regular");
                                    backUpSavings.withdraw(overdraftAmount);
                                    fromChecking.setOverdrafts(fromChecking.getOverdrafts() + 1);

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
            } else {
                //does not have valid checking
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Payment Not Processed");
                a.setHeaderText("No checking account");
                a.setContentText("Please ensure you have a valid checking account to pay with.");
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