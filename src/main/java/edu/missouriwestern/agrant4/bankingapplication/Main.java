package edu.missouriwestern.agrant4.bankingapplication;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws IOException {

    // Create the first controller, which loads login-view.fxml within its own constructor
    LoginController loginController = new LoginController();

    // Show the new stage
    loginController.showStage();
  }

  public static void main(String[] args) {
    launch(args);
  }
}