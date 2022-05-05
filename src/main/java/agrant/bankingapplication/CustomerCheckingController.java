package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for screen where customer can manage their checking account.
 */
public class CustomerCheckingController extends Controller {
    @FXML
    private Button basicWithdrawButton;

    @FXML
    private Button basicDepositButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button depositCheckButton;

    @FXML
    private Button sendCheckButton;

    /**
     * Button that lets customer withdraw from their checking account
     */
    @FXML
    void basicWithdrawClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            //check that numeric fields are numbers
            double withdrawAmount = Double.parseDouble(this.amountField.getText());

            //format currency as $__.__
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedWDAmount = numberFormatter.format(withdrawAmount);

            //get account ID and checking object
            String accID = String.format("%s_c", this.getLoginController().getCurrentUser().getSSN());
            Checking targetedChecking = this.getLoginController().findCheckingByID(accID);

            //check that the user has valid checking account
            if (this.getLoginController().hasValidCheckingAccount(accID)) {
                //try to withdraw
                if (this.getLoginController().findCheckingByID(accID).oneTimeWithdraw(withdrawAmount)) {
                    //we can withdraw! Now confirm
                    this.confirmWithdraw(currentDate, accID, formattedWDAmount);
                } else {
                    //we have to try to overdraft
                    double originalCheckingBalance = targetedChecking.getCurrentBalance();
                    double overdraftAmount = withdrawAmount - originalCheckingBalance;

                    //we have to account for any fees
                    if (targetedChecking.getAccountType().equals("Regular")) {
                        overdraftAmount += 0.5;
                    }

                    //check if we have a backup account and that it has enough money
                    if (
                        !targetedChecking.getBackupAccountId().equals("n/a") &&
                        overdraftAmount <= this.getLoginController().findSavingsByID(targetedChecking.getBackupAccountId()).getAccountBalance()
                    ) {
                        //We have both; get backup savings object
                        Savings backUpSavings = this.getLoginController().findSavingsByID(targetedChecking.getBackupAccountId());

                        //Withdraw from checking and savings
                        targetedChecking.setCurrentBalance(0.0);
                        targetedChecking.setInterest("n/a");
                        targetedChecking.setAccountType("Regular");
                        backUpSavings.withdraw(overdraftAmount);
                        targetedChecking.setOverdrafts(targetedChecking.getOverdrafts() + 1);

                        //format numbers for confirmation
                        String formattedChecking = numberFormatter.format(originalCheckingBalance);
                        String formattedOverdraft = numberFormatter.format(overdraftAmount);

                        //Create 2 transaction objects
                        Transactions newTrans1 = new Transactions(
                            targetedChecking.getAccountId(),
                            "withdraw", "Withdrew "
                            + formattedChecking
                            + " from account "
                            + targetedChecking.getAccountId()
                            + ".",
                            currentDate
                        );

                        Transactions newTrans2 = new Transactions(
                            backUpSavings.getAccountId(),
                            "withdraw",
                            "Withdrew "
                                + formattedOverdraft
                                + " from account "
                                + backUpSavings.getAccountId()
                                + ".",
                            currentDate
                        );

                        //add transactions to log
                        this.getLoginController().getTransactionLog().add(newTrans1);
                        this.getLoginController().getTransactionLog().add(newTrans2);

                        //write data to CSV
                        this.getLoginController().writeBankData();

                        //create confirmation controller
                        ConfirmationController confirmationController = new ConfirmationController(
                            this.getCurrentStage(),
                            this.getLoginController(),
                            this.getMainPage(),
                            "Congratulations, you withdrew "
                                + formattedWDAmount
                                + " from account number "
                                + accID
                                + "."
                        );
                        confirmationController.showStage();
                    } else {
                        //we either don't have a linked savings or we don't have enough with the overdraft
                        Alert a = new Alert(AlertType.WARNING);
                        a.setTitle("Not enough money.");
                        a.setHeaderText("Withdraw not processed.");
                        a.setContentText("Not enough money in account, even with overdraft.");
                        a.show();
                    }
                }
            }
        } catch (NumberFormatException var21) {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");
            a.show();
        }

    }

    /**
     * Button that brings customer to the deposit check screen
     */
    @FXML
    void depositCheckClicked(ActionEvent event) {
        CustomerDepositCheckController controller = new CustomerDepositCheckController(
            this.getCurrentStage(),
            this.getLoginController(),
            (CustomerOpeningController) this.getMainPage()
        );
        controller.showStage();
    }

    /**
     * Button that brings customer to the send check screen
     */
    @FXML
    void sendCheckClicked(ActionEvent event) {
        CustomerSendCheckController controller = new CustomerSendCheckController(
            this.getCurrentStage(),
            this.getLoginController(),
            (CustomerOpeningController) this.getMainPage()
        );
        controller.showStage();
    }

    /**
     * Method that confirms withdrawals
     *
     * @param currentDate - String of current date
     * @param accID - account ID for checking account
     * @param formattedIncomingMoney - formatted string of amount money $__.__
     */
    private void confirmWithdraw(String currentDate, String accID, String formattedIncomingMoney) {
        Transactions newTrans = new Transactions(accID, "withdraw", "Withdrew " + formattedIncomingMoney + " from account.", currentDate);
        this.getLoginController().getTransactionLog().add(newTrans);
        this.getLoginController().writeBankData();
        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you withdrew " + formattedIncomingMoney + " from account number " + accID + ".");
        confirmationController.showStage();
    }

    /**
     * Button that lets customer deposit money into their account
     */
    @FXML
    void basicDepositClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            //check that numeric fields are numeric
            double depAmount = Double.parseDouble(this.amountField.getText());

            //format money as currency
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedDepAmount = numberFormatter.format(depAmount);

            //get account ID
            String accID = String.format("%s_c", this.getLoginController().getCurrentUser().getSSN());

            //check that user has valid checking account
            if (this.getLoginController().hasValidCheckingAccount(accID)) {
                //try to deposit money
                if (this.getLoginController().findCheckingByID(accID).oneTimeDeposit(depAmount, getLoginController())) {
                    //if we can deposit, confirm it!
                    this.confirmDeposit(currentDate, accID, formattedDepAmount);
                }
            } else {
                Alert a = new Alert(AlertType.WARNING);
                a.setTitle("Too small a deposit.");
                a.setHeaderText("Transfer not processed.");
                a.setContentText("Transfer less than fee.");
                a.show();
            }
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");
            a.show();
        }

    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    /**
     * Method for finding account info
     *
     * @param accID - String of account ID
     * @return - returns toString of given checking account
     */
    private String findAccountInfo(String accID) {
        String returnString = "Error: No Account found";


        for(Checking checking : getLoginController().getCheckingData()) {
            if(checking.getAccountId().equals(accID)) {
                returnString = checking.toString();
                break;
            }
        }

        return returnString;
    }

    /**
     * Method for confirming deposits
     *
     * @param currentDate - string of current date
     * @param accID - account id for checking account
     * @param formattedIncomingMoney - formatted deposited money as $__.__
     */
    private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
        //create transaction object
        Transactions newTrans = new Transactions(
            accID,
            "deposit",
            "Deposited " + formattedIncomingMoney + " into account.",
            currentDate
        );
        //add transaction to log
        this.getLoginController().getTransactionLog().add(newTrans);
        //write log to CSV
        this.getLoginController().writeBankData();
        //create confirmation screen
        ConfirmationController confirmationController = new ConfirmationController(
            this.getCurrentStage(),
            this.getLoginController(),
            this.getMainPage(),
            "Congratulations, you deposited "
                + formattedIncomingMoney
                + " to account number "
                + accID
                + "."
        );
        confirmationController.showStage();
    }

    //constructor
    public CustomerCheckingController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-checking.fxml");
        this.setCurrentViewTitle("Checking Account");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_c");

    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
