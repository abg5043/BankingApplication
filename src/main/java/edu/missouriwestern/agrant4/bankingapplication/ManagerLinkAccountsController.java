package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.ConfirmationController;
import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.LoginController;
import edu.missouriwestern.agrant4.bankingapplication.ManagerOpeningController;
import edu.missouriwestern.agrant4.bankingapplication.classes.Checking;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerLinkAccountsController extends Controller {

    @FXML
    private TextField checkingField;

    @FXML
    private Button linkButton;

    @FXML
    private TextField savingsField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void linkClicked(ActionEvent event) {
        String checkingID = checkingField.getText();
        String savingsID = savingsField.getText();

        //Check that the text is not blank and matches an account
        if(
            checkingID.length() == 11 &&
            savingsID.length() == 11 &&
            getLoginController().hasValidCheckingAccount(checkingID) &&
            getLoginController().hasValidSavingsAccount(savingsID)
        ) {

            //find the checking account
            Checking modifiedChecking = getLoginController().findCheckingByID(checkingID);

            //check if there is already a linked account
            if (modifiedChecking.getBackupAccountId().equals("n/a")) {
                //modify the checking account accountID
                modifiedChecking.setBackupAccountId(savingsID);

                //if account is gold, also modify the savings rate
                if(modifiedChecking.getAccountType().equals("Gold")) {
                    Savings linkedSavings;
                    //find the savings account
                    if(savingsID.substring(9,11).equals("_s")) {
                        //this is a normal savings
                        linkedSavings = getLoginController().findSavingsByID(savingsID);
                    } else {
                        //this is a CD
                        linkedSavings = getLoginController().findCDByID(savingsID);
                    }
                    //use the savings account to set the interest rate of the checking
                    modifiedChecking.setInterest(String.valueOf(0.5 * linkedSavings.getInterestRate()));
                }

                //write to CSV
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you linked account " +checkingID + " with account " + savingsID + "."
                );

                confirmationController.showStage();
            } else {
                //there is a linked account
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Accounts Not Linked");
                a.setHeaderText("Already linked account");
                a.setContentText("This checking account already has a linked savings");

                // show the dialog
                a.show();
            }
        } else {
            //text is blank or does not match an account
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Accounts Not Linked");
            a.setHeaderText("Invalid account ID");
            a.setContentText("Please ensure you enter valid account IDs. " +
                "\n\nRemember that CDs can't be backup savings!");

            // show the dialog
            a.show();
        }
    }

    public ManagerLinkAccountsController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-link-accounts.fxml");
        setCurrentViewTitle("Link Two Accounts");
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
