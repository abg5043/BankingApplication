package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checks;
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

/**
 * Controller for screen where customer can deposit checks
 */
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
    private TextField memoField;

    /**
     * Button that lets customer deposit check
     */
    @FXML
    void depositClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        String originAcctID = originAccIDField.getText();

        //check that the formatting is good in the text areas
        if(!originAccIDField.getText().isEmpty() &&
            !moneyField.getText().isEmpty() &&
            !checkNumField.getText().isEmpty() &&
            !memoField.getText().isEmpty() &&
            originAcctID.length() == 11
        ) {
            try {
                //check that numeric fields are numbers
                int checkNum = Integer.parseInt(checkNumField.getText());
                double amountCash = Double.parseDouble(moneyField.getText());
                String customerCheckAccID = String.format("%s_c", getLoginController().getCurrentUser().getSSN());
                String memo = memoField.getText();

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedAmtCash = formatter.format(amountCash);

                //Check that customer has a checking account
                if(getLoginController().findCheckingByID(customerCheckAccID) != null){
                    //check if we are trying to send a check to ourselves
                    if(!customerCheckAccID.equals(originAcctID)) {
                        //create new check
                        Checks newCheck = new Checks(
                            originAcctID,
                            amountCash,
                            String.valueOf(checkNum),
                            currentDate,
                            customerCheckAccID,
                            memo
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
                    } else {
                        //we tried to send a check to ourselves
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Deposit Failed");
                        a.setHeaderText("Invalid account");
                        a.setContentText("Please do not send checks to and from the same account.");

                        // show the dialog
                        a.show();
                    }
                }else{
                    //You need to have a checking account to cash checks
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Deposit Failed");
                    a.setHeaderText("No checking account");
                    a.setContentText("Please create a checking account before trying to deposit checks.");

                    // show the dialog
                    a.show();
                }
            } catch (NumberFormatException e) {
                //there were non-numeric characters in numeric fields
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Deposit failed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");

                // show the dialog
                a.show();
            }
        }else {
            //there were null fields
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Deposit failed");
            a.setHeaderText("Null or Misformatted Fields");
            a.setContentText("Please enter information in all fields according to suggested formatting.");

            // show the dialog
            a.show();
        }
    }

    //constructor
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
