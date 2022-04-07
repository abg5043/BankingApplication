package edu.missouriwestern.agrant4.bankingapplication;

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

  @FXML
  void creditClicked(ActionEvent event) {

  }

  @FXML
  void debitClicked(ActionEvent event) {

  }

  @FXML
  void sendBillButton(ActionEvent event) {

  }



  public ManagerOpeningController(Stage previousStage, LoginController previousController) {
    super(previousStage, previousController);
    setCurrentView("manager-opening-view.fxml");
    setCurrentTitle("Manager");
    setNewScene(this, getCurrentView(), getCurrentTitle());
  }

}
