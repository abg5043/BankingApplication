package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.ConfirmationController;
import edu.missouriwestern.agrant4.bankingapplication.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;

public class TellerCreditController extends Controller {

    @FXML
    private TextField accountField;

    @FXML
    private Button creditButton;

    @FXML
    private Button debitButton;

    @FXML
    private TextField moneyField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void creditClicked(ActionEvent event) {
        try {
            double money = Double.parseDouble(moneyField.getText());
            String accID = accountField.getText();

            //this formats the money amount into currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String formattedMoney = formatter.format(money);



            if( accID.length() == 11 ) {
                //TODO: IMPLEMENT LOGIC. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you credited " + formattedMoney +
                        " to account number " + accID + "."
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Money Not Credited");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    @FXML
    void debitClicked(ActionEvent event) {
        try {
            double money = Double.parseDouble(moneyField.getText());
            String accID = accountField.getText();

            //this formats the money amount into currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String formattedMoney = formatter.format(money);



            if( accID.length() == 11 ) {
                //TODO: IMPLEMENT LOGIC. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you debited " + formattedMoney +
                        " from account number " + accID + "."
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Money Not Debited");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Money Not Debited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }


    public TellerCreditController(
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-credit.fxml");
        setCurrentViewTitle("Credit/Debit Money");
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
