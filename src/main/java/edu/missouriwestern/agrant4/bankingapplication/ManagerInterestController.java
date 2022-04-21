package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                getLoginController().findSavingsByID(accountID).setInterestRate(newInterest);

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


