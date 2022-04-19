package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;

public class TellerTransferController extends Controller {

    @FXML
    private Button transferButton;

    @FXML
    private TextField transferFromField;

    @FXML
    private TextField transferToField;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField moneyField;

    @FXML
    void transferClicked(ActionEvent event) {

        try {
            double transferredMoney = Double.parseDouble(moneyField.getText());
            String fromID = transferFromField.getText();
            String toID = transferToField.getText();

            //this formats the money amount into currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String formattedTransferredMoney = formatter.format(transferredMoney);



            if( fromID.length() == 11 && toID.length() == 11) {
                //TODO: IMPLEMENT LOGIC. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you transferred " + formattedTransferredMoney +
                        " from account number " + fromID + " to account number " +
                        toID + "."
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Money Not Transferred");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Money Not Transferred");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    public TellerTransferController(
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-transfer.fxml");
        setCurrentViewTitle("Transfer Money");
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
