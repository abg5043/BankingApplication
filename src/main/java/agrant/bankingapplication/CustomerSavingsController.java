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
    public CustomerSavingsController(Stage currentStage,
                                      LoginController loginController,
                                      CustomerOpeningController customerOpeningController
    ){
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-savings.fxml");
        setCurrentViewTitle("Savings Account");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set set up your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
