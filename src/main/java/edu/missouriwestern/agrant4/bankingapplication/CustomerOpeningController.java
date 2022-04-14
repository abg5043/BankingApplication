package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerOpeningController extends Controller {

  @FXML
  private Button balanceButton;

  @FXML
  private Button depositButton;

  @FXML
  private TextArea mainTextBox;

  @FXML
  private Label welcomeLabel;

  @FXML
  private Button withdrawButton;

  @FXML
  void balanceClicked(ActionEvent event) {

  }

  @FXML
  void depositClicked(ActionEvent event) {

  }

  @FXML
  void withdrawClicked(ActionEvent event) {

  }

  public CustomerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("customer-opening-view.fxml");
    setCurrentViewTitle("Customer");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    this.welcomeLabel.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!"
    + "\n\nPlease select your next action.");
  }
}

