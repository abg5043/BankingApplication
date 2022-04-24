package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerLoanCreditCardController extends Controller{
    @FXML
    private Button chargeCardButton;

    @FXML
    private Button payInFullButton;

    @FXML
    private Button payOverTimeButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void chargeCardClicked(ActionEvent event) {

    }

    @FXML
    void payInFullClicked(ActionEvent event) {

    }

    @FXML
    void payOverTimeClicked(ActionEvent event) {

    }

    public CustomerLoanCreditCardController(Stage currentStage,
                                         LoginController loginController,
                                            CustomerLoanAccountController customerLoanAccountController
    ){
        super(currentStage, loginController, customerLoanAccountController);
        setCurrentViewFile("customer-loan-credit-card.fxml");
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

