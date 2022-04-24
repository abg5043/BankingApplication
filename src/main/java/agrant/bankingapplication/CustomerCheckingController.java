package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerCheckingController extends Controller{
    @FXML
    private Button basicWithdrawButton;

    @FXML
    private Button basicDepositButton;

    @FXML
    private Button monthlyWithdrawButton;

    @FXML
    private Button monthlyDepositButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void basicWithdrawClicked(ActionEvent event) {

    }

    @FXML
    void basicDepositClicked(ActionEvent event) {

    }

    @FXML
    void monthlyWithdrawClicked(ActionEvent event) {

    }

    @FXML
    void monthlyDepositClicked(ActionEvent event) {

    }

    public CustomerCheckingController(Stage currentStage,
                                            LoginController loginController,
                                            CustomerPINCheckController customerPINCheckController
    ){
        super(currentStage, loginController, customerPINCheckController);
        setCurrentViewFile("customer-checking.fxml");
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

