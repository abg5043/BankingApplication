package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;

public class TellerAccountInfoStartController extends Controller {

    @FXML
    private TextField accountField;

    @FXML
    private Button infoButton;

    @FXML
    private Label welcomeLabel;

    private String currentAccountID;

    @FXML
    void infoClicked(ActionEvent event) {

        this.currentAccountID = accountField.getText();

        if( currentAccountID.length() == 11 &&
            (getLoginController().hasValidSavingsAccount(currentAccountID) ||
                getLoginController().hasValidCheckingAccount(currentAccountID) ||
                getLoginController().hasValidCDAccount(currentAccountID) ||
                getLoginController().hasValidLoanAccount(currentAccountID)
            )
        ) {

            // Go to the next screen
            TellerAccountInfoDisplayController controller = new TellerAccountInfoDisplayController(
                getCurrentStage(),
                getLoginController(),
                (TellerOpeningController) getMainPage(),
                currentAccountID
            );

            controller.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Account Chosen");
            a.setHeaderText("Invalid account number");
            a.setContentText("Please ensure you use a valid account.");

            // show the dialog
            a.show();
        }
    }

    public TellerAccountInfoStartController(
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-accountinfo-start.fxml");
        setCurrentViewTitle("Get Account Info");
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
