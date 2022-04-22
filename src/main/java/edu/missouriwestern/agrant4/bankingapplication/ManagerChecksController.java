package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.Checks;
import edu.missouriwestern.agrant4.bankingapplication.classes.Transactions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerChecksController extends Controller {

  @FXML
  private TableColumn<Checks, String> accountNumberCol;

  @FXML
  private TableColumn<Checks, Double> amountCashCol;

  @FXML
  private TableView<Checks> checkData;

  @FXML
  private TextField checkNumber;

  @FXML
  private TableColumn<Checks, String> checkNumberCol;

  @FXML
  private TableColumn<Checks, String> dateCol;

  @FXML
  private Button processButton;

  @FXML
  private Button stopButton;

  @FXML
  private Label welcomeLabel;

  @FXML
  private TableColumn<Checks, String> withdrawOrDepositCol;

  @FXML
  private TextField acctNumField;


  @FXML
  void processClicked(ActionEvent event) {
    String checkNumberText = checkNumber.getText();
    String accNum = acctNumField.getText();

    if (
        checkNumberText.length() != 0 &&
            getLoginController().hasValidPendingCheck(checkNumberText)
    ) {
      if (
          accNum.length() != 0 &&
          (getLoginController().hasValidSavingsAccount(accNum) || getLoginController().hasValidCheckingAccount(accNum))
      ) {

        //find the specific check
        Checks processingCheck = getLoginController().findChecksByCheckNum(checkNumberText);
        String transactionType = processingCheck.getWithdrawOrDeposit();

        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        //check if check is deposit or withdraw
        if (transactionType.equals("deposit")) {
          String typeOfAccount = processingCheck.getAccountID().substring(9, 11);

          //check which account the check is slated to go to
          if (typeOfAccount.equals("_c")) {
            //check is for a checking account

            //get the deposit amount and acct id from check
            double depositAmt = processingCheck.getAmountCash();
            String destinationAcctId = processingCheck.getAccountID();

            //Check if the check is larger than the fee
            //If yes, deposit money
            if(getLoginController().findCheckingByID(destinationAcctId).oneTimeDeposit(depositAmt)) {
              //Create transaction object
              Transactions newTrans = new Transactions(
                  destinationAcctId,
                  transactionType,
                  "Check number " + checkNumberText + " deposited " + depositAmt + " into account.",
                  currentDate
              );

              //add transaction to log
              getLoginController().getTransactionLog().add(newTrans);

              //transaction is done. remove check from arraylist
              getLoginController().getPendingChecks().remove(processingCheck);

              //write the data
              getLoginController().writeBankData();


              // create a confirmation screen
              ConfirmationController confirmationController = new ConfirmationController(
                  getCurrentStage(),
                  getLoginController(),
                  getMainPage(),
                  "Congratulations, you processed check number " + checkNumberText
              );

              confirmationController.showStage();

            } else {
              //check is not larger than fee
              // create an alert
              Alert a = new Alert(Alert.AlertType.WARNING);
              a.setTitle("Too small a deposit.");
              a.setHeaderText("Check not processed.");
              a.setContentText("Deposit amount less than fee.");

              // show the dialog
              a.show();
            }
          } else {
            //this is not a valid account type for a check
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid Account Type");
            a.setHeaderText("Check not processed");
            a.setContentText("Checks can only be used with checking accounts.");

            // show the dialog
            a.show();
          }
        } else if (transactionType.equals("withdraw")) {
          String typeOfAccount = processingCheck.getAccountID().substring(9, 11);
          //check type of account
          if (typeOfAccount.equals("_c")) {
            //this is for a checking account

            //get the withdraw amount and acct id from check
            double withdrawAmt = processingCheck.getAmountCash();
            String originAcctId = processingCheck.getAccountID();

            /*
             * Withdraw money if there is money in account;
             * returns true or false depending on if there is money in the account
             */
            if (getLoginController().findCheckingByID(originAcctId).oneTimeWithdraw(withdrawAmt)) {
              //transaction is done. remove check from list

              //Create transaction object
              Transactions newTrans = new Transactions(
                  originAcctId,
                  transactionType,
                  "Check number " + checkNumberText + " withdrew " + withdrawAmt + " from account.",
                  currentDate
              );

              //add transaction to log
              getLoginController().getTransactionLog().add(newTrans);

              //remove pending check from arraylist now that it is processed
              getLoginController().getPendingChecks().remove(processingCheck);

              //write the data
              getLoginController().writeBankData();

              // create a confirmation screen
              ConfirmationController confirmationController = new ConfirmationController(
                  getCurrentStage(),
                  getLoginController(),
                  getMainPage(),
                  "Congratulations, you processed check number " + checkNumberText
              );

              confirmationController.showStage();
            }

          } else {
            //this is not a valid account type for a check
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid Account Type");
            a.setHeaderText("Check not processed");
            a.setContentText("Checks can only be used with checking accounts.");

            // show the dialog
            a.show();
          }
        } else {
          //this is not a valid procedure
          // create an alert
          Alert a = new Alert(Alert.AlertType.WARNING);
          a.setTitle("Invalid Transaction Type");
          a.setHeaderText("Check not processed");
          a.setContentText("Checks can only be used to withdraw or deposit.");

          // show the dialog
          a.show();
        }
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid input");
        a.setHeaderText("Check not processed");
        a.setContentText("Account number does not link to current account. Please stop check.");

        // show the dialog
        a.show();
      }

    } else {
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Invalid input");
      a.setHeaderText("Check not processed");
      a.setContentText("Please enter valid check number.");

      // show the dialog
      a.show();
    }

  }

  @FXML
  void stopClicked(ActionEvent event) {
    String checkNumberText = checkNumber.getText();
    String accNum = acctNumField.getText();


    //Check that the text is not blank and matches a check
    if (checkNumberText.length() != 0 && getLoginController().hasValidPendingCheck(checkNumberText)) {
      if (
          accNum.length() != 0 &&
              (getLoginController().hasValidSavingsAccount(accNum) || getLoginController().hasValidCheckingAccount(accNum))
      ) {

        //find the specific check
        Checks stoppedCheck = getLoginController().findChecksByCheckNum(checkNumberText);

        //remove check from pending
        getLoginController().getPendingChecks().remove(stoppedCheck);

        getLoginController().writeBankData();

        // create a confirmation screen
        ConfirmationController confirmationController = new ConfirmationController(
            getCurrentStage(),
            getLoginController(),
            getMainPage(),
            "Check number " + checkNumberText + " was stopped."
        );

        confirmationController.showStage();
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid input");
        a.setHeaderText("Check not processed");
        a.setContentText("Account number does not link to current account. Please stop check.");

        // show the dialog
        a.show();
      }
    } else {
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Invalid Check Number");
      a.setHeaderText("Check not stopped");
      a.setContentText("Invalid check number. Please try again.");

      // show the dialog
      a.show();
    }

  }

  public ManagerChecksController(
      Stage currentStage,
      LoginController loginController,
      ManagerOpeningController managerOpeningController
  ) {
    super(currentStage, loginController, managerOpeningController);
    setCurrentViewFile("manager-checks.fxml");
    setCurrentViewTitle("Manage Checks");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());

  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");

    accountNumberCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("accountID"));
    amountCashCol.setCellValueFactory(new PropertyValueFactory<Checks, Double>("amountCash"));
    withdrawOrDepositCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("withdrawOrDeposit"));
    checkNumberCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("checkNumber"));
    dateCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("date"));

    //bind list into the table
    checkData.setItems(FXCollections.observableArrayList(getLoginController().getPendingChecks()));
  }

}
