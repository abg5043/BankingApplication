package edu.missouriwestern.agrant4.bankingapplication;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileReader;
import java.util.ArrayList;

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

  private ArrayList<User> users;

  private User currentUser;

  @FXML
  void customerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {
      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getCustomer() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        CustomerOpeningController customerOpeningController = new CustomerOpeningController(
            getCurrentStage(),
            this,
            currentUser
        );

        // Show the new stage/window
        customerOpeningController.showStage();
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid User Type");
        a.setHeaderText("Invalid User Type");
        a.setContentText("You are not a valid customer. Please try another account type");

        // show the dialog
        a.show();
      }
    }
  }

  @FXML
  void managerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {
      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getManager() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        ManagerOpeningController managerOpeningController = new ManagerOpeningController(
            getCurrentStage(),
            this,
            currentUser
        );

        // Show the new stage/window
        managerOpeningController.showStage();
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid User Type");
        a.setHeaderText("Invalid User Type");
        a.setContentText("You are not a valid manager. Please try another account type");

        // show the dialog
        a.show();
      }
    }
  }

  /*
   * Validates login credentials
   */
  private boolean loginIsValid(String username, String pass) {

    //checks if entered username and password match a record in the system.
    for(User user : users) {
      if(user.getUser().equals(username) && user.getPass().equals(pass)) {
        currentUser = user;
        break;
      }
    }

    Boolean isValid = currentUser != null;

    if (!isValid) {
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Invalid Login");
      a.setHeaderText("Invalid Login");
      a.setContentText("Please enter a valid username and password");

      // show the dialog
      a.show();
    }
    return isValid;
  }

  @FXML
  void tellerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {
      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getTeller() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        TellerOpeningController tellerOpeningController = new TellerOpeningController(
            getCurrentStage(),
            this,
            currentUser
        );

        // Show the new stage/window
        tellerOpeningController.showStage();
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid User Type");
        a.setHeaderText("Invalid User Type");
        a.setContentText("You are not a valid teller. Please try another account type");

        // show the dialog
        a.show();
      }

    }
  }

  public LoginController() {
    //sets current user to null since there isn't one logged in yet.
    this.currentUser = null;
    setCurrentView("login-view.fxml");
    setCurrentTitle("Login");
    setNewScene(this, getCurrentView(), getCurrentTitle());

    // parse users from csv file as objects and store them in an ArrayList
    try {
      users = (ArrayList<User>) new CsvToBeanBuilder(new FileReader("customers.csv")).withType(User.class).build().parse();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    getCurrentStage().show();
  }


}
