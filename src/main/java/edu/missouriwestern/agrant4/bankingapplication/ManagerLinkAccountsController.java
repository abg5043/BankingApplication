package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.ConfirmationController;
import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.LoginController;
import edu.missouriwestern.agrant4.bankingapplication.ManagerOpeningController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
