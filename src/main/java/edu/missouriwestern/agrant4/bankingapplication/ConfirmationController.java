package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ConfirmationController extends Controller {

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    private String confirmationMessage;

    public ConfirmationController(
        Stage currentStage,
        LoginController loginController,
        Controller mainPage,
        String confirmationMessage
    ) {
        super(currentStage, loginController, mainPage);
        setCurrentViewFile("confirmation-view.fxml");
        setCurrentViewTitle("Confirmation");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
        this.confirmationMessage = confirmationMessage;
        this.mainTextBox.setText(confirmationMessage);
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }

}
