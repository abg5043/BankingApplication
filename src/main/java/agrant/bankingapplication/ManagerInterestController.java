package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerInterestController extends Controller {


    @FXML
    private TextField interestRateField;

    @FXML
    private Button interestButton;

    @FXML
    private TextField accountField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void interestClicked(ActionEvent event) {

        try {
            double newInterest = Double.parseDouble(interestRateField.getText());
            String accountID = accountField.getText();

            if( accountID.length() == 11 && getLoginController().hasValidSavingsAccount(accountID)) {

                //set the new interest rate
                Savings currentSavings = getLoginController().findSavingsByID(accountID);
                currentSavings.setInterestRate(newInterest);

                //check if user has checking account, that it is linked to this account, and that it is gold
                if(
                    getLoginController().findCheckingByID(accountID.substring(0,9) + "_c") != null &&
                    getLoginController().findCheckingByID(accountID.substring(0,9) + "_c").getBackupAccountId().equals(accountID) &&
                    getLoginController().findCheckingByID(accountID.substring(0,9) + "_c").getAccountType().equals("Gold")
                ) {
                    //update that interest too
                    Checking currentChecking = getLoginController().findCheckingByID(accountID.substring(0,9) + "_c");
                    currentChecking.setInterest(String.valueOf(0.5 * currentSavings.getInterestRate()));
                }

                //write data to csv
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you updated your savings account interest rate for account " +
                        accountID + "!" +
                        "\n\nSavings rate is now " +
                        newInterest
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Rates not updated");
                a.setHeaderText("Invalid account number");
                a.setContentText("Please enter a valid savings account number.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Rates Not Updated");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    public ManagerInterestController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-interest.fxml");
        setCurrentViewTitle("Set Interest Rates");
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


