package agrant.bankingapplication;

import agrant.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for screen where manager can create a savings account
 */
public class ManagerCreateSavingsController extends Controller {

    @FXML
    private TextField ssnField;

    @FXML
    private Button savingsButton;

    @FXML
    private TextField interestRateField;

    @FXML
    private TextField startingBalanceField;

    @FXML
    private Label welcomeLabel;

    /**
     * Button that lets manager create savings account
     */
    @FXML
    void savingsClicked(ActionEvent event) {
        String customerSSN = ssnField.getText();

        try {
            //checks that all user-entered data is properly formatted
            double balance = Double.parseDouble(startingBalanceField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());

            //get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String openDate = date.format(formatters);

            //Check that the text is not blank
            if( customerSSN.length() == 9) {
                //check that the SSN matches a user
                if (getLoginController().isValidUser(customerSSN)) {
                    //check that the user doesn't have a savings or CD
                    if(
                        !getLoginController().hasValidSavingsAccount(customerSSN + "_s") ||
                        !getLoginController().hasValidCDAccount(customerSSN + "_s")
                    ) {
                        //Create new savings account
                        Savings newSavings = new Savings(
                            customerSSN + "_s",
                            balance,
                            interestRate,
                            openDate,
                            "n/a"
                        );

                        //update data and write to csv
                        getLoginController().getSavingsData().add(newSavings);
                        getLoginController().writeBankData();

                        // create a confirmation screen
                        ConfirmationController confirmationController = new ConfirmationController(
                            getCurrentStage(),
                            getLoginController(),
                            getMainPage(),
                            "Congratulations, you created a savings account!"
                        );

                        confirmationController.showStage();
                    } else {
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Savings Not Created");
                        a.setHeaderText("User already has account");
                        a.setContentText("Sorry. Users are only allowed one account.");

                        // show the dialog
                        a.show();
                    }

                } else {
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Savings Not Created");
                    a.setHeaderText("Invalid user SSN");
                    a.setContentText("Please ensure you enter in a valid SSN for a valid customer");

                    // show the dialog
                    a.show();
                }
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Savings Not Created");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }
        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Savings Not Created");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    //Constructor for class
    public ManagerCreateSavingsController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-create-savings.fxml");
        setCurrentViewTitle("Create a Savings Account");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
