package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerMortgageController extends Controller {

    @FXML
    private Label amtOwedLabel;

    @FXML
    private Button makePaymentButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void makePaymentClicked(ActionEvent event) {

    }

    public CustomerMortgageController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-mortgage.fxml");
        setCurrentViewTitle("Mortgage");
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
