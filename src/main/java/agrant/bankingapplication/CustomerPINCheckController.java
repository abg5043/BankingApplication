package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for screen where customer enters pin number
 */
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
    private PasswordField pinPasswordField;

    /**
     * Button that checks if pin is correct
     */
    @FXML
    void enterClicked(ActionEvent event) {
        Alert a;
        try {
            //checks that pin is number
            String inputPIN = String.valueOf(Integer.parseInt(this.pinPasswordField.getText()));
            //checks if pin is 4 digits long
            if (inputPIN.length() != 4) {
                a = new Alert(AlertType.WARNING);
                a.setTitle("PIN Unsuccessful");
                a.setHeaderText("Invalid PIN Length");
                a.setContentText("Please enter 4-Digit Numeric PIN.");
                a.show();
            } else if (inputPIN.equals(this.getLoginController().getCurrentUser().getPin())) {
                //pin is 4 digits and matches the current user's pin
                if (this.isChecking) {
                    //if we are going to a checking account, we start that screen
                    CustomerCheckingController customerCheckingController = new CustomerCheckingController(
                            this.getCurrentStage(),
                            this.getLoginController(),
                        (CustomerOpeningController) this.getMainPage()
                    );
                    customerCheckingController.showStage();
                } else if (!this.isChecking) {
                    //if we aren't going to checking, we are going to savings account; start that screen
                    CustomerSavingsController customerSavingsController = new CustomerSavingsController(
                            this.getCurrentStage(),
                            this.getLoginController(),
                        (CustomerOpeningController) this.getMainPage()
                    );
                    customerSavingsController.showStage();
                }
            } else {
                a = new Alert(AlertType.WARNING);
                a.setTitle("PIN Unsuccessful");
                a.setHeaderText("Invalid PIN");
                a.setContentText("Please enter correct PIN.");
                a.show();
            }
        } catch (NumberFormatException var4) {
            a = new Alert(AlertType.WARNING);
            a.setTitle("PIN Unsuccessful");
            a.setHeaderText("Invalid Input Type");
            a.setContentText("Please enter 4-digit number-only PIN.");
            a.show();
        }

    }

    //constructor
    public CustomerPINCheckController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController, Boolean accType) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-pin-check.fxml");
        this.setCurrentViewTitle("Enter PIN");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        this.isChecking = accType;
    }


    /**
     * The initialize() method allows you set set up your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}