package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TellerOpeningController extends Controller {

  @FXML
  private Button accountButton;

  @FXML
  private Button creditAccountButton;


  @FXML
  private TextArea mainTextBox;

  @FXML
  private Button transferButton;

  @FXML
  private Label welcomeLabel;

  private User currentUser;

  @FXML
  void accountClicked(ActionEvent event) {

  }

  @FXML
  void creditAccountClicked(ActionEvent event) {
  }

  @FXML
  void transferClicked(ActionEvent event) {
  }


  public TellerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("teller-opening-view.fxml");
    setCurrentViewTitle("Teller");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    this.welcomeLabel.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + loginController.getCurrentUser().getFirstName() + "!"
        + "\n\nPlease select your next action.");

    //TODO: DELETE THIS LINE; It is a test
    getLoginController().getUsers().add(new User("test", "test", "test", "test", "test", "test", "test", true, true, true, "test", "test"));
  }
}
