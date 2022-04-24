package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerPINCheckController extends Controller {
    //True if Checking clicked, False if Savings clicked
    boolean isChecking;

    @FXML
    private Button enterButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void enterClicked(ActionEvent event) {
        if(isChecking) {
            CustomerCheckingController customerCheckingController = new CustomerCheckingController(getCurrentStage(),
                    getLoginController(),
                    this
            );

            customerCheckingController.showStage();
        }else if(!isChecking){
            CustomerSavingsController customerSavingsController = new CustomerSavingsController(getCurrentStage(),
                    getLoginController(),
                    this
            );

            customerSavingsController.showStage();
        }
    }

    public CustomerPINCheckController(Stage currentStage,
                                      LoginController loginController,
                                      CustomerOpeningController customerOpeningController,
                                      Boolean accType
    ){
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-pin-check.fxml");
        setCurrentViewTitle("Checking account");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
        isChecking = accType;
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

