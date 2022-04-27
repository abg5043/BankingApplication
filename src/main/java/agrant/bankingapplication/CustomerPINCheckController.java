//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

public class CustomerPINCheckController extends Controller {
    boolean isChecking;
    @FXML
    private Button enterButton;
    @FXML
    private TextArea mainTextBox;
    @FXML
    private Label welcomeLabel;
    @FXML
    private PasswordField pinPasswordField;

    @FXML
    void enterClicked(ActionEvent event) {
        Alert a;
        try {
            String inputPIN = String.valueOf(Integer.parseInt(this.pinPasswordField.getText()));
            if (inputPIN.length() != 4) {
                a = new Alert(AlertType.WARNING);
                a.setTitle("PIN Unsuccessful");
                a.setHeaderText("Invalid PIN Length");
                a.setContentText("Please enter 4-Digit Numeric PIN.");
                a.show();
            } else if (inputPIN.equals(this.getLoginController().getCurrentUser().getPin())) {
                if (this.isChecking) {
                    CustomerCheckingController customerCheckingController = new CustomerCheckingController(this.getCurrentStage(), this.getLoginController(), this);
                    customerCheckingController.showStage();
                } else if (!this.isChecking) {
                    CustomerSavingsController customerSavingsController = new CustomerSavingsController(this.getCurrentStage(), this.getLoginController(), this);
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

    public CustomerPINCheckController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController, Boolean accType) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-pin-check.fxml");
        this.setCurrentViewTitle("Enter PIN");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
        this.isChecking = accType;
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}