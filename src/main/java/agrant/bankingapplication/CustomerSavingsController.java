package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerSavingsController extends Controller{
    @FXML
    private Button withdrawButton;

    @FXML
    private Button depositButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void withdrawClicked(ActionEvent event) {

    }

    @FXML
    void depositClicked(ActionEvent event) {

    }
    public CustomerSavingsController(Stage currentStage,
                                      LoginController loginController,
                                      CustomerPINCheckController customerPINCheckController
    ){
        super(currentStage, loginController, customerPINCheckController);
        setCurrentViewFile("customer-savings.fxml");
        setCurrentViewTitle("Loan Account");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set set up your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
        //FIXME Replace placeholders with pointers to data:
        //this.amtDeposited.setText("Amount Deposited: " + "[PLACEHOLDER]");
        //this.acctBalance.setText("Account Balance: " + "[PLACEHOLDER]");
    }
}

