package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerLoanAccountTermController extends Controller{
    @FXML
    private Button newLoanButton;

    @FXML
    private Button makePaymentButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void newLoanClicked(ActionEvent event) {

    }

    @FXML
    void makePaymentClicked(ActionEvent event) {

    }

    public CustomerLoanAccountTermController(Stage currentStage,
                                            LoginController loginController,
                                             CustomerLoanAccountController customerLoanAccountController
    ){
        super(currentStage, loginController, customerLoanAccountController);
        setCurrentViewFile("customer-loan-term.fxml");
        setCurrentViewTitle("Loan Term");
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

