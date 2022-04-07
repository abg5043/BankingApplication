package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController extends Controller {

  @FXML
  private Button customerButton;

  @FXML
  private Button managerButton;

  @FXML
  private TextField passwordField;

  @FXML
  private Button tellerButton;

  @FXML
  private Label testLabel;

  @FXML
  private TextField userNameField;

  @FXML
  void customerClicked(ActionEvent event) {
    String user = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(user, pass)) {
      // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
      // In fact, with "this", we could pass the whole controller
      CustomerOpeningController customerOpeningController = new CustomerOpeningController(
          getCurrentStage(),
          this
      );

      // Show the new stage/window
      customerOpeningController.showStage();
    }
  }

  @FXML
  void managerClicked(ActionEvent event) {
    String user = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(user, pass)) {
      // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
      // In fact, with "this", we could pass the whole controller
      ManagerOpeningController managerOpeningController = new ManagerOpeningController(
          getCurrentStage(),
          this
      );

      // Show the new stage/window
      managerOpeningController.showStage();
    }
    
    
  }

  /*
   * Validates login
   */
  private boolean loginIsValid(String user, String pass) {

    Boolean isValid = user.length() > 4 && pass.length() > 4;


    if (!isValid) {
      // create an alert
      Alert a = new Alert(Alert.AlertType.INFORMATION);
      a.setTitle("Invalid Login");
      a.setHeaderText("Invalid Login");
      a.setContentText("Please enter a valid password");

      // show the dialog
      a.show();
    }
    return isValid;
  }

  @FXML
  void tellerClicked(ActionEvent event) {
    String user = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(user, pass)) {
      // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
      // In fact, with "this", we could pass the whole controller
      TellerOpeningController tellerOpeningController = new TellerOpeningController(
          getCurrentStage(),
          this
      );

      // Show the new stage/window
      tellerOpeningController.showStage();
    }
  }

  public LoginController() {
    setCurrentView("login-view.fxml");
    setCurrentTitle("Login");
    setNewScene(this, getCurrentView(), getCurrentTitle());
  }

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    getCurrentStage().show();
  }


}
