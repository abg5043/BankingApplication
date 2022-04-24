package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerCDController extends Controller{

    @FXML
    private Button depositButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void depositClicked(ActionEvent event) {

    }

    @FXML
    void withdrawClicked(ActionEvent event) {

    }

    public CustomerCDController(Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ){
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-cd.fxml");
        setCurrentViewTitle("CD Account");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set set up your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }
}

