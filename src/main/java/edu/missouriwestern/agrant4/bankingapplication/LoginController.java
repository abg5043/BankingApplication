package edu.missouriwestern.agrant4.bankingapplication;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import edu.missouriwestern.agrant4.bankingapplication.classes.Checking;
import edu.missouriwestern.agrant4.bankingapplication.classes.Loans;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
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

  private User currentUser;

  // Because we are going to pass this controller to every controller, we can just use that as a way to have access to
  // all of the files rather than passing every object through every constructor
  private ArrayList<User> usersData;
  private ArrayList<Savings> savingsData;
  private ArrayList<Loans> loansData;
  private ArrayList<Checking> checkingData;

  //TODO: MAKE LOG OF CHECKS (and checks object)
  //TODO: Make log of transactions (and transactions object with account num, date, transaction type, and memo)

  @Override
  @FXML
  void exitClicked(ActionEvent event) {
    System.exit(0);
  }

  @FXML
  void customerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {

      loadBankData();

      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getCustomer() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        CustomerOpeningController customerOpeningController = new CustomerOpeningController(
            getCurrentStage(),
            this
        );

        // Show the new stage/window
        customerOpeningController.showStage();
      } else {
        createUserTypeAlert("customer");
      }
    }
  }

  @FXML
  void tellerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {
      loadBankData();

      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getTeller() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        TellerOpeningController tellerOpeningController = new TellerOpeningController(
            getCurrentStage(),
            this
        );

        // Show the new stage/window
        tellerOpeningController.showStage();
      } else {
        createUserTypeAlert("teller");
      }

    }
  }

  @FXML
  void managerClicked(ActionEvent event) {
    String username = userNameField.getText();
    String pass = passwordField.getText();

    if (loginIsValid(username, pass)) {
      loadBankData();

      //embedded if so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getManager() == true) {
        // Create the second controller, which loads its own FXML file. We can pass arguments to this controller.
        // In fact, with "this", we could pass the whole controller
        ManagerOpeningController managerOpeningController = new ManagerOpeningController(
            getCurrentStage(),
            this
        );

        // Show the new stage/window
        managerOpeningController.showStage();
      } else {
        createUserTypeAlert("manager");
      }
    }
  }

  /**
   * Creates an alert that the wrong type of user is trying to log in.
   *
   * @param userType - a type of user for the bank program
   */
  private void createUserTypeAlert(String userType) {
    // create an alert
    Alert a = new Alert(Alert.AlertType.WARNING);
    a.setTitle("Invalid User Type");
    a.setHeaderText("Invalid User Type");
    a.setContentText("You are not a valid " + userType + ". Please try another account type");

    // show the dialog
    a.show();
  }

  /**
   * Loads bank data from csv files to ArrayLists in the object's fields
   */
  public void loadBankData() {
    //TODO: this should load all the data to the fields
  }

  /*
   * Validates login credentials
   */
  private boolean loginIsValid(String username, String pass) {

    //checks if entered username and password match a record in the system.
    for(User user : getUsersData()) {
      if(user.getUser().equals(username) && user.getPass().equals(pass)) {
        this.currentUser = user;
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

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    getCurrentStage().show();
  }

  /**
   * Constructor for LoginController
   */
  public LoginController() {
    //sets current user to null since there isn't one logged in yet.
    this.currentUser = null;
    setCurrentViewFile("login-view.fxml");
    setCurrentViewTitle("Login");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());

    // parse users from csv file as objects and store them in an ArrayList
    try {
      setUsersData((ArrayList<User>) new CsvToBeanBuilder(new FileReader("users.csv"))
          .withType(User.class)
          .build()
          .parse());
      setSavingsData((ArrayList<Savings>) new CsvToBeanBuilder(new FileReader("savings.csv"))
          .withType(Savings.class)
          .build()
          .parse());
      setCheckingData((ArrayList<Checking>) new CsvToBeanBuilder(new FileReader("checking.csv"))
          .withType(Checking.class)
          .build()
          .parse());

      setLoansData( (ArrayList<Loans>) new CsvToBeanBuilder(new FileReader("loans.csv"))
          .withType(Loans.class)
          .build()
          .parse());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public User getCurrentUser() {
    return currentUser;
  }


  public ArrayList<User> getUsersData() {
    return usersData;
  }

  public void setUsersData(ArrayList<User> usersData) {
    this.usersData = usersData;
  }

  public ArrayList<Savings> getSavingsData() {
    return savingsData;
  }

  public void setSavingsData(ArrayList<Savings> savingsData) {
    this.savingsData = savingsData;
  }

  public ArrayList<Loans> getLoansData() {
    return loansData;
  }

  public void setLoansData(ArrayList<Loans> loansData) {
    this.loansData = loansData;
  }

  public ArrayList<Checking> getCheckingData() {
    return checkingData;
  }

  public void setCheckingData(ArrayList<Checking> checkingData) {
    this.checkingData = checkingData;
  }

  //TODO: Need to flush out this writeBankData method for updating the bank's CSV files
  public void writeBankData() {
    Writer writer1 = null;
    Writer writer2 = null;
    Writer writer3 = null;
    Writer writer4 = null;
    try {
      //writes users to a users.csv file
      writer1 = new FileWriter("users.csv");
      StatefulBeanToCsv beanToCsv1 = new StatefulBeanToCsvBuilder(writer1).build();
      beanToCsv1.write(usersData);
      writer1.close();

      //writes savings data to a savings.csv file
      writer2 = new FileWriter("savings.csv");
      StatefulBeanToCsv beanToCsv2 = new StatefulBeanToCsvBuilder(writer2).build();
      beanToCsv2.write(savingsData);
      writer2.close();

      //writes checking data to a checking.csv file
      writer3 = new FileWriter("checking.csv");
      StatefulBeanToCsv beanToCsv3 = new StatefulBeanToCsvBuilder(writer3).build();
      beanToCsv3.write(checkingData);
      writer3.close();

      //writes loans data to a loans.csv file
      writer4 = new FileWriter("loans.csv");
      StatefulBeanToCsv beanToCsv4 = new StatefulBeanToCsvBuilder(writer4).build();
      beanToCsv4.write(loansData);
      writer4.close();


    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}
