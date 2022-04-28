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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CustomerCDController extends Controller {
    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button depositButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private TextArea mainTextBox;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField moneyField;

    @FXML
    void depositClicked(ActionEvent event) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            double depAmount = Double.parseDouble(this.moneyField.getText());
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedDepAmount = numberFormatter.format(depAmount);
            String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
            if (this.getLoginController().hasValidCDAccount(accID)) {
                this.getLoginController().findCDByID(accID).deposit(depAmount);
                this.confirmDeposit(currentDate, accID, formattedDepAmount);
            } else {
                Alert a = new Alert(AlertType.WARNING);
                a.setTitle("Invalid Account Type");
                a.setHeaderText("Deposit not processed.");
                a.setContentText("Account is not CD account.");
                a.show();
            }
        } catch (NumberFormatException var11) {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");
            a.show();
        }

    }

    private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
        Transactions newTrans = new Transactions(accID, "deposit", "Deposited " + formattedIncomingMoney + " into account.", currentDate);
        this.getLoginController().getTransactionLog().add(newTrans);
        this.getLoginController().writeBankData();
        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you deposited " + formattedIncomingMoney + " to account number " + accID + ".");
        confirmationController.showStage();
    }

    @FXML
    void withdrawClicked(ActionEvent event) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            double withdrawAmount = Double.parseDouble(this.moneyField.getText());
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedWDAmount = numberFormatter.format(withdrawAmount);
            String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
            Savings targetedAccounted = this.getLoginController().findCDByID(accID);
            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            if (LocalDate.parse(targetedAccounted.getDueDate(), newFormatter).isAfter(LocalDate.now())) {
                this.withdrawFromSavings(currentDate, withdrawAmount, accID, formattedWDAmount, targetedAccounted, "\n\nPlease note we also debited a 20% penalty for early withdrawal.");
            } else {
                this.withdrawFromSavings(currentDate, withdrawAmount, accID, formattedWDAmount, targetedAccounted, "");
            }
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");
            a.show();
        }

    }

    private void withdrawFromSavings(String currentDate, double outgoingMoney, String accID, String formattedOutgoingMoney, Savings targetedAccounted, String appendedMessage) {
        if (targetedAccounted.withdraw(outgoingMoney)) {
            Transactions newTrans = new Transactions(accID, "withdraw", "Withdrew " + formattedOutgoingMoney + " from account " + targetedAccounted.getAccountId(), currentDate);
            this.getLoginController().getTransactionLog().add(newTrans);
            this.getLoginController().writeBankData();
            ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you credited " + formattedOutgoingMoney + " to account number " + accID + ". " + appendedMessage);
            confirmationController.showStage();
        }

    }

    private void confirmWithdraw(String currentDate, String accID, String formattedIncomingMoney) {
        Transactions newTrans = new Transactions(accID, "withdraw", "Withdrew " + formattedIncomingMoney + " from account.", currentDate);
        this.getLoginController().getTransactionLog().add(newTrans);
        this.getLoginController().writeBankData();
        ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you withdrew " + formattedIncomingMoney + " from account number " + accID + ".");
        confirmationController.showStage();
    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    private String findAccountInfo(String text) {
        String returnString = "Error: No Account found";
            for(Savings savings : getLoginController().getSavingsData()) {
                if(savings.getAccountId().equals(text)) {
                    returnString = savings.toString();
                    break;
                }
            }
        return returnString;
    }

    public CustomerCDController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-cd.fxml");
        this.setCurrentViewTitle("CD Account");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_s");
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}