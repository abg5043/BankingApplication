package agrant.bankingapplication;

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

/**
 * Controller for screen where customers can withdraw from their CDs
 */
public class CustomerCDController extends Controller {
    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Button withdrawButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField moneyField;

    /**
     * Button that lets customer withdraw from their CD
     */
    @FXML
    void withdrawClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(dateFormatter);

        try {
            //check that numeric fields are numbers
            double withdrawAmount = Double.parseDouble(this.moneyField.getText());

            //format currency as such
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            String formattedWDAmount = numberFormatter.format(withdrawAmount);

            //get account ID and savings account object
            String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
            Savings targetedAccount = this.getLoginController().findCDByID(accID);

            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

            //check if we are before or after the due date
            if (LocalDate.parse(targetedAccount.getDueDate(), newFormatter).isAfter(LocalDate.now())) {
                //we are withdrawing early. We can do so, but with a fee
                this.withdrawFromSavings(currentDate, withdrawAmount, accID, formattedWDAmount, targetedAccount, "\n\nPlease note we also debited a 20% penalty for early withdrawal.");
            } else {
                //we are withdrawing from savings after the due date. So no fee
                this.withdrawFromSavings(currentDate, withdrawAmount, accID, formattedWDAmount, targetedAccount, "");
            }
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Money Not Withdrawn");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");
            a.show();
        }

    }

    /**
     * Method for withdrawing from CD
     *
     * @param currentDate - String of current date
     * @param outgoingMoney - how much to withdraw
     * @param accID - CD account ID
     * @param formattedOutgoingMoney - how much to withdraw
     * @param targetedAccount - Savings account object
     * @param appendedMessage - message if we have fee
     */
    private void withdrawFromSavings(String currentDate, double outgoingMoney, String accID, String formattedOutgoingMoney, Savings targetedAccount, String appendedMessage) {
        //try to withdraw
        if (targetedAccount.withdraw(outgoingMoney)) {
            //we withdrew! Create transaction object
            Transactions newTrans = new Transactions(
                accID,
                "withdraw",
                "Withdrew "
                    + formattedOutgoingMoney
                    + " from account "
                    + targetedAccount.getAccountId(),
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
                "Congratulations, you withdrew "
                    + formattedOutgoingMoney
                    + " from account number "
                    + accID
                    + ". " + appendedMessage
            );
            confirmationController.showStage();
        }

    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    /**
     * Method for finding account info
     *
     * @param accID - account id for current account
     * @return - toString for current account
     */
    private String findAccountInfo(String accID) {
        String returnString = "Error: No Account found";
            for(Savings savings : getLoginController().getSavingsData()) {
                if(savings.getAccountId().equals(accID)) {
                    returnString = savings.toString();
                    break;
                }
            }
        return returnString;
    }

    //constructor
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