package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
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
      String accID = String.format("%s_c", this.getLoginController().getCurrentUser().getSSN());
      if (this.getLoginController().hasValidCheckingAccount(accID)) {
          this.isChecking = true;
          CustomerPINCheckController customerPINCheckController = new CustomerPINCheckController(this.getCurrentStage(), this.getLoginController(), this, true);
          customerPINCheckController.showStage();
      } else {
          Alert a = new Alert(AlertType.WARNING);
          a.setTitle("Unable to Proceed.");
          a.setHeaderText("Invalid account.");
          a.setContentText("Account does not exist.");
          a.show();
      }

  }

  @FXML
  void savingsClicked(ActionEvent event) {
      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
      if (this.getLoginController().hasValidSavingsAccount(accID)) {
          this.isChecking = false;
          CustomerPINCheckController customerPINCheckController = new CustomerPINCheckController(this.getCurrentStage(), this.getLoginController(), this, this.isChecking);
          customerPINCheckController.showStage();
      } else {
          Alert a = new Alert(AlertType.WARNING);
          a.setTitle("Unable to Proceed.");
          a.setHeaderText("Invalid account.");
          a.setContentText("Account does not exist.");
          a.show();
      }

  }

  @FXML
  void cdClicked(ActionEvent event) {
      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
      if (this.getLoginController().findCDByID(accID) != null) {
          CustomerCDController customerCDController = new CustomerCDController(this.getCurrentStage(), this.getLoginController(), this);
          customerCDController.showStage();
      } else {
          Alert a = new Alert(AlertType.WARNING);
          a.setTitle("Unable to Proceed.");
          a.setHeaderText("Invalid account.");
          a.setContentText("Account does not exist.");
          a.show();
      }

  }

  @FXML
  void loanClicked(ActionEvent event) {
      CustomerLoanAccountController customerLoanAccountController = new CustomerLoanAccountController(this.getCurrentStage(), this.getLoginController(), this);
    customerLoanAccountController.showStage();
  }

  public CustomerOpeningController(Stage currentStage, LoginController loginController) {
      super(currentStage, loginController, loginController);
      this.setCurrentViewFile("customer-opening-view.fxml");
      this.setCurrentViewTitle("Customer");
      this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
      this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
      this.mainTextBox.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!\n\nWelcome to the Customer Main Screen. \n\nPlease select your next action.");
  }
}

