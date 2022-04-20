package edu.missouriwestern.agrant4.bankingapplication;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import edu.missouriwestern.agrant4.bankingapplication.classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
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
  private ArrayList<Loans> loanApplications;
  private ArrayList<Checks> pendingChecks;
  private ArrayList<Transactions> transactionLog;

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

      //embedded so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getCustomer()) {
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

      //embedded so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getTeller()) {
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

      //embedded so that we can differentiate alerts. This will only cause a user type alert now
      if (currentUser.getManager()) {
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

      setLoanApplications( (ArrayList<Loans>) new CsvToBeanBuilder(new FileReader("loanApplications.csv"))
          .withType(Loans.class)
          .build()
          .parse());
      setPendingChecks((ArrayList<Checks>) new CsvToBeanBuilder(new FileReader("checks.csv"))
          .withType(Checks.class)
          .build()
          .parse());

      setTransactionLog((ArrayList<Transactions>) new CsvToBeanBuilder(new FileReader("transactions.csv"))
          .withType(Transactions.class)
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

  public ArrayList<Loans> getLoanApplications() {
    return loanApplications;
  }

  public void setLoanApplications(ArrayList<Loans> loanApplications) {
    this.loanApplications = loanApplications;
  }

  public ArrayList<Checks> getPendingChecks() {
    return pendingChecks;
  }

  public void setPendingChecks(ArrayList<Checks> pendingChecks) {
    this.pendingChecks = pendingChecks;
  }

  public ArrayList<Transactions> getTransactionLog() {
    return transactionLog;
  }

  public void setTransactionLog(ArrayList<Transactions> transactionLog) {
    this.transactionLog = transactionLog;
  }

  //TODO: Need to flush out this writeBankData method for updating the bank's CSV files
  public void writeBankData() {
    Writer userWriter = null;
    Writer savingsWriter = null;
    Writer checkingWriter = null;
    Writer loansWriter = null;
    Writer loanApplicationWriter = null;
    Writer checksWriter = null;
    Writer transactionsWriter = null;

    try {
      //writes users to a users.csv file
      userWriter = new FileWriter("users.csv");
      StatefulBeanToCsv beanToCsv1 = new StatefulBeanToCsvBuilder(userWriter).build();
      beanToCsv1.write(usersData);
      userWriter.close();

      //writes savings data to a savings.csv file
      savingsWriter = new FileWriter("savings.csv");
      StatefulBeanToCsv beanToCsv2 = new StatefulBeanToCsvBuilder(savingsWriter).build();
      beanToCsv2.write(savingsData);
      savingsWriter.close();

      //writes checking data to a checking.csv file
      checkingWriter = new FileWriter("checking.csv");
      StatefulBeanToCsv beanToCsv3 = new StatefulBeanToCsvBuilder(checkingWriter).build();
      beanToCsv3.write(checkingData);
      checkingWriter.close();

      //writes loans data to a loans.csv file
      loansWriter = new FileWriter("loans.csv");
      StatefulBeanToCsv beanToCsv4 = new StatefulBeanToCsvBuilder(loansWriter).build();
      beanToCsv4.write(loansData);
      loansWriter.close();

      //writes loans application data to a loanApplications.csv file
      loanApplicationWriter = new FileWriter("loanApplications.csv");
      StatefulBeanToCsv beanToCsv5 = new StatefulBeanToCsvBuilder(loanApplicationWriter).build();
      beanToCsv5.write(loanApplications);
      loanApplicationWriter.close();

      //writes checks data to a checks.csv file
      checksWriter = new FileWriter("checks.csv");
      StatefulBeanToCsv beanToCsv6 = new StatefulBeanToCsvBuilder(checksWriter).build();
      beanToCsv6.write(pendingChecks);
      checksWriter.close();

      //writes transactions data to a transactions.csv file
      transactionsWriter = new FileWriter("transactions.csv");
      StatefulBeanToCsv beanToCsv7 = new StatefulBeanToCsvBuilder(transactionsWriter).build();
      beanToCsv7.write(transactionLog);
      transactionsWriter.close();


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public boolean isValidUser(String SSN) {
    for (User user : this.getUsersData()) {
      if (user.getSSN().equals(SSN)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasValidLoanAccount(String accNum) {
    for (Loans loan : this.getLoansData()) {
      if (loan.getAccountId().equals(accNum)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasValidPendingCheck(String checkNum) {
    for (Checks check : this.getPendingChecks()) {
      if (check.getCheckNumber().equals(checkNum)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasValidLoanApplication(String accNum) {
    for (Loans loan : this.getLoanApplications()) {
      if (loan.getAccountId().equals(accNum)) {
        return true;
      }
    }
    return false;
  }


  public boolean hasValidCheckingAccount(String checkingID) {
    for (Checking checking : this.getCheckingData()) {
      if (checking.getAccountId().equals(checkingID)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasValidSavingsAccount(String savingsID) {
    for (Savings savings : this.getSavingsData()) {
      if (savings.getAccountId().equals(savingsID) && savings.getDueDate().equals("n/a")) {
        return true;
      }
    }
    return false;
  }

  public boolean hasValidCDAccount(String savingsID) {
    for (Savings savings : this.getSavingsData()) {
      if (savings.getAccountId().equals(savingsID) && !savings.getDueDate().equals("n/a")) {
        return true;
      }
    }
    return false;
  }

  public Loans findLoanByID(String accNum) {
    for (Loans loan : this.getLoansData()) {
      if (loan.getAccountId().equals(accNum)) {
        return loan;
      }
    }
    return null;
  }

  public Loans findLoanApplicationByID(String accNum) {
    for (Loans loan : this.getLoanApplications()) {
      if (loan.getAccountId().equals(accNum)) {
        return loan;
      }
    }
    return null;
  }

  public Savings findSavingsByID(String accNum) {
    for (Savings savings : this.getSavingsData()) {
      if (savings.getAccountId().equals(accNum) && savings.getDueDate().equals("n/a")) {
        return savings;
      }
    }
    return null;
  }

  public Savings findCDByID(String accNum) {
    for (Savings savings : this.getSavingsData()) {
      if (savings.getAccountId().equals(accNum) && !savings.getDueDate().equals("n/a")) {
        return savings;
      }
    }
    return null;
  }

  public Checking findCheckingByID(String accNum) {
    for (Checking checking : this.getCheckingData()) {
      if (checking.getAccountId().equals(accNum)) {
        return checking;
      }
    }
    return null;
  }

  public Checks findChecksByCheckNum(String checkNum) {
    for (Checks check : this.getPendingChecks()) {
      if (check.getCheckNumber().equals(checkNum)) {
        return check;
      }
    }
    return null;
  }
}
