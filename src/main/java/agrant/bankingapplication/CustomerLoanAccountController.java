package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerLoanAccountController extends Controller{
    @FXML
    private Button longTermButton;

    @FXML
    private Button shortTermButton;

    @FXML
    private Button creditCardButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void longTermClicked(ActionEvent event) {
        CustomerMortgageController customerMortgageController = new CustomerMortgageController(
            getCurrentStage(),
                getLoginController(),
            (CustomerOpeningController) getMainPage()
        );

        customerMortgageController.showStage();
    }

    @FXML
    void shortTermClicked(ActionEvent event) {
        CustomerLoanAccountTermController customerLoanCreditCardController = new CustomerLoanAccountTermController(getCurrentStage(),
                getLoginController(),
            (CustomerOpeningController) getMainPage()
        );

        customerLoanCreditCardController.showStage();
    }

    @FXML
    void creditCardClicked(ActionEvent event) {
        CustomerLoanCreditCardController customerLoanAccountTermController = new CustomerLoanCreditCardController(getCurrentStage(),
                getLoginController(),
            (CustomerOpeningController) getMainPage()
        );

        customerLoanAccountTermController.showStage();
    }

    public CustomerLoanAccountController(Stage currentStage,
                                         LoginController loginController,
                                         CustomerOpeningController customerOpeningController
    ){
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-loan-account.fxml");
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

