package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
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
    void basicWithdrawClicked(ActionEvent event) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            double withdrawAmount = Double.parseDouble(this.amountField.getText());
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedWDAmount = numberFormatter.format(withdrawAmount);
            String accID = String.format("%s_c", this.getLoginController().getCurrentUser().getSSN());
            Checking targetedChecking = this.getLoginController().findCheckingByID(accID);
            if (this.getLoginController().hasValidCheckingAccount(accID)) {
                if (this.getLoginController().findCheckingByID(accID).oneTimeWithdraw(withdrawAmount)) {
                    this.confirmWithdraw(currentDate, accID, formattedWDAmount);
                } else {
                    double originalCheckingBalance = targetedChecking.getCurrentBalance();
                    double overdraftAmount = withdrawAmount - originalCheckingBalance;
                    if (targetedChecking.getAccountType().equals("Regular")) {
                        overdraftAmount += 0.5;
                    }

                    if (!targetedChecking.getBackupAccountId().equals("n/a") && overdraftAmount <= this.getLoginController().findSavingsByID(targetedChecking.getBackupAccountId()).getAccountBalance()) {
                        Savings backUpSavings = this.getLoginController().findSavingsByID(targetedChecking.getBackupAccountId());
                        targetedChecking.setCurrentBalance(0.0);
                        backUpSavings.withdraw(overdraftAmount);
                        String formattedChecking = numberFormatter.format(originalCheckingBalance);
                        String formattedOverdraft = numberFormatter.format(overdraftAmount);
                        Transactions newTrans1 = new Transactions(targetedChecking.getAccountId(), "withdraw", "Withdrew " + formattedChecking + " from account " + targetedChecking.getAccountId() + ".", currentDate);
                        Transactions newTrans2 = new Transactions(targetedChecking.getAccountId(), "withdraw", "Withdrew " + formattedOverdraft + " from account " + backUpSavings.getAccountId() + ".", currentDate);
                        this.getLoginController().getTransactionLog().add(newTrans1);
                        this.getLoginController().getTransactionLog().add(newTrans2);
                        this.getLoginController().writeBankData();
                        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you withdrew " + formattedWDAmount + " from account number " + accID + ".");
                        confirmationController.showStage();
                    } else {
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

    @FXML
    void depositCheckClicked(ActionEvent event) {
        CustomerDepositCheckController controller = new CustomerDepositCheckController(
            this.getCurrentStage(),
            this.getLoginController(),
            (CustomerOpeningController) this.getMainPage()
        );
        controller.showStage();

    }

    private void confirmWithdraw(String currentDate, String accID, String formattedIncomingMoney) {
        Transactions newTrans = new Transactions(accID, "withdraw", "Withdrew " + formattedIncomingMoney + " from account.", currentDate);
        this.getLoginController().getTransactionLog().add(newTrans);
        this.getLoginController().writeBankData();
        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you withdrew " + formattedIncomingMoney + " from account number " + accID + ".");
        confirmationController.showStage();
    }

    @FXML
    void basicDepositClicked(ActionEvent event) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            double depAmount = Double.parseDouble(this.amountField.getText());
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedDepAmount = numberFormatter.format(depAmount);
            String accID = String.format("%s_c", this.getLoginController().getCurrentUser().getSSN());
            if (this.getLoginController().hasValidCheckingAccount(accID)) {
                if (this.getLoginController().findCheckingByID(accID).oneTimeDeposit(depAmount)) {
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

    private String findAccountInfo(String text) {
        String returnString = "Error: No Account found";


        for(Checking checking : getLoginController().getCheckingData()) {
            if(checking.getAccountId().equals(text)) {
                returnString = checking.toString();
                break;
            }
        }

        return returnString;
    }

    private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
        Transactions newTrans = new Transactions(accID, "deposit", "Deposited " + formattedIncomingMoney + " into account.", currentDate);
        this.getLoginController().getTransactionLog().add(newTrans);
        this.getLoginController().writeBankData();
        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you deposited " + formattedIncomingMoney + " to account number " + accID + ".");
        confirmationController.showStage();
    }

    public CustomerCheckingController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-checking.fxml");
        this.setCurrentViewTitle("Loan Account");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_c");

    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
