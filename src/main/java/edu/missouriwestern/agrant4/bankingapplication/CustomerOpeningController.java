package edu.missouriwestern.agrant4.bankingapplication;

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

  public CustomerOpeningController(Stage previousStage, LoginController previousController) {
    super(previousStage, previousController);
    setCurrentView("customer-opening-view.fxml");
    setCurrentTitle("Customer");
    setNewScene(this, getCurrentView(), getCurrentTitle());
  }
}

