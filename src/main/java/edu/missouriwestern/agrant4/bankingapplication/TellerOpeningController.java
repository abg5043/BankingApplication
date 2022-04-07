package edu.missouriwestern.agrant4.bankingapplication;

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

  @FXML
  void accountClicked(ActionEvent event) {

  }

  @FXML
  void creditAccountClicked(ActionEvent event) {

  }

  @FXML
  void transferClicked(ActionEvent event) {

  }


  public TellerOpeningController(Stage previousStage, LoginController previousController) {
    super(previousStage, previousController);
    setCurrentView("teller-opening-view.fxml");
    setCurrentTitle("Teller");
    setNewScene(this, getCurrentView(), getCurrentTitle());
  }
}
