package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checks;
import agrant.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerDepositCheckController extends Controller {

    @FXML
    private TextField checkNumField;

    @FXML
    private Button depositButton;

    @FXML
    private TextField originAccIDField;

    @FXML
    private TextField moneyField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void depositClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        String originAcctID = originAccIDField.getText();

        if(!originAccIDField.getText().isEmpty() &&
            !moneyField.getText().isEmpty() &&
            !checkNumField.getText().isEmpty() &&
            originAcctID.length() == 11
        ) {
            try {
                int checkNum = Integer.parseInt(checkNumField.getText());
                double amountCash = Double.parseDouble(moneyField.getText());
                String customerCheckAccID = String.format("%s_c", getLoginController().getCurrentUser().getSSN());

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedAmtCash = formatter.format(amountCash);

                //Check that customer has a checking account
                if(getLoginController().findCheckingByID(customerCheckAccID) != null){
                    //create new check
                    Checks newCheck = new Checks(
                        originAcctID,
                        amountCash,
                        String.valueOf(checkNum),
                        date.format(formatters),
                        customerCheckAccID
                    );

                    //add check to pending
                    getLoginController().getPendingChecks().add(newCheck);

                    //write the data
                    getLoginController().writeBankData();

                    // create a confirmation screen
                    ConfirmationController confirmationController = new ConfirmationController(
                        getCurrentStage(),
                        getLoginController(),
                        getMainPage(),
                        "Congratulations, you deposited check " + checkNum +
                        " to acct " + customerCheckAccID + "."
                    );

                    confirmationController.showStage();
                }else{
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Deposit Failed");
                    a.setHeaderText("No checking account");
                    a.setContentText("Please create a checking account before trying to deposit checks.");

                    // show the dialog
                    a.show();
                }
            } catch (NumberFormatException e) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Deposit failed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");

                // show the dialog
                a.show();
            }
        }else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Deposit failed");
            a.setHeaderText("Null or Misformatted Fields");
            a.setContentText("Please enter information in all fields according to suggested formatting.");

            // show the dialog
            a.show();
        }
    }

    public CustomerDepositCheckController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-deposit-check.fxml");
        this.setCurrentViewTitle("Deposit Check");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }

}
