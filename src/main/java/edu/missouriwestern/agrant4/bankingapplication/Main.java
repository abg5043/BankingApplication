package edu.missouriwestern.agrant4.bankingapplication;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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