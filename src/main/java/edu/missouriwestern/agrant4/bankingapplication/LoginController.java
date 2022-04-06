package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

  // Holds this controller's Stage
  private final Stage currentStage;

  @FXML
  private Button customerButton;

  @FXML
  private Button exitButton;

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

  }

  @FXML
  void exitClicked(ActionEvent event) {

  }

  @FXML
  void managerClicked(ActionEvent event) {
    String user = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(user, pass)) {
      // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
      // In fact, with "this", we could pass the whole controller
      TestController testController = new TestController(
          user,
          currentStage
      );

      // Show the new stage/window
      testController.showStage();
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

  }

  public LoginController() {
    // Create the new stage since this is the first window
    currentStage = new Stage();

    // Load the FXML file
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));

      // Set this class as the controller
      loader.setController(this);

      // Load the scene
      currentStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      currentStage.setTitle("Login Window");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    currentStage.show();
  }


}
