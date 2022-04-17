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
            double checkingInterest = Double.parseDouble(interestRateField.getText());
            String ID = accountField.getText();

            if( ID.length() == 9) {
                //TODO: IMPLEMENT LOGIC. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you updated your interest rate for account " + ID + "!" +
                        "\n\nChecking rates are now " + checkingInterest
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Rates not updated");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

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


