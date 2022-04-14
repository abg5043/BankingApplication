package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ManagerOpeningController extends Controller {

  @FXML
  private Button creditButton;

  @FXML
  private Button debitButton;

  @FXML
  private TextArea mainTextBox;

  @FXML
  private Button sendBillButton;

  @FXML
  private Label welcomeLabel;

  private User currentUser;

  @FXML
  void creditClicked(ActionEvent event) {

  }

  @FXML
  void debitClicked(ActionEvent event) {

  }

  @FXML
  void sendBillButton(ActionEvent event) {

  }



  public ManagerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("manager-opening-view.fxml");
    setCurrentViewTitle("Manager");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    this.welcomeLabel.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!"
        + "\n\nPlease select your next action.");
  }

}
