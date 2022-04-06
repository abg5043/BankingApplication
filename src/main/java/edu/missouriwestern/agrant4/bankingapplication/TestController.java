package edu.missouriwestern.agrant4.bankingapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TestController {
  // Holds this controller's Stage
  private Stage currentStage;

  private String user;

  @FXML
  private Label testLabel;

  public TestController(String user, Stage previousStage) {
    this.user = user;


    // Create the new stage
    currentStage = previousStage;

    // Load the FXML file
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("test-view.fxml"));

      // Set this class as the controller
      loader.setController(this);

      // Load the scene
      currentStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      currentStage.setTitle("Test Scene");

      //You need to make sure that any visible things
      this.testLabel.setText(this.user);

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
