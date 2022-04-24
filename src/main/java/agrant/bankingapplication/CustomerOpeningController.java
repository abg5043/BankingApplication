package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerOpeningController extends Controller {
  //True if Checking clicked, False if Savings clicked
  boolean isChecking;

  @FXML
  private Button checkingButton;

  @FXML
  private Button savingsButton;

  @FXML
  private TextArea mainTextBox;

  @FXML
  private Label welcomeLabel;

  @FXML
  private Button cdButton;

  @FXML
  private Button loanButton;

  @FXML
  void checkingClicked(ActionEvent event) {
    isChecking = true;

    CustomerPINCheckController customerPINCheckController = new CustomerPINCheckController(
        getCurrentStage(), getLoginController(), this, true
    );

    customerPINCheckController.showStage();
  }

  @FXML
  void savingsClicked(ActionEvent event) {
    isChecking = false;

    CustomerPINCheckController customerPINCheckController = new CustomerPINCheckController(
        getCurrentStage(),
        getLoginController(),
        this,
        isChecking
    );

    customerPINCheckController.showStage();
  }

  @FXML
  void cdClicked(ActionEvent event) {
    CustomerCDController customerCDController = new CustomerCDController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    customerCDController.showStage();
  }

  @FXML
  void loanClicked(ActionEvent event) {
    CustomerLoanAccountController customerLoanAccountController = new CustomerLoanAccountController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    customerLoanAccountController.showStage();
  }

  public CustomerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("customer-opening-view.fxml");
    setCurrentViewTitle("Customer");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!"
        + "\n\nWelcome to the Customer Main Screen. "
        + "\n\nPlease select your next action.");
  }
}

