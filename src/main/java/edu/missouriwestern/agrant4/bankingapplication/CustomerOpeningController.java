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

  private User currentUser;

  @FXML
  void balanceClicked(ActionEvent event) {

  }


  @FXML
  void depositClicked(ActionEvent event) {

  }

  @FXML
  void withdrawClicked(ActionEvent event) {

  }

  public CustomerOpeningController(Stage previousStage, LoginController previousController, User currentUser) {
    super(previousStage, previousController);
    this.currentUser = currentUser;
    setCurrentView("customer-opening-view.fxml");
    setCurrentTitle("Customer");
    setNewScene(this, getCurrentView(), getCurrentTitle());
    this.welcomeLabel.setText("Hello, " + currentUser.getFirstName() + "!");
  }
}

