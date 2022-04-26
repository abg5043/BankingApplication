package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerSavingsController extends Controller {
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

    public CustomerSavingsController(Stage currentStage, LoginController loginController, CustomerPINCheckController customerPINCheckController) {
        super(currentStage, loginController, new CustomerOpeningController(currentStage, loginController));
        this.setCurrentViewFile("customer-savings.fxml");
        this.setCurrentViewTitle("Loan Account");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
