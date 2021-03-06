package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Checks;
import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for screen where manager can stop or process checks
 */
public class ManagerChecksController extends Controller {

  @FXML
  private TableColumn<Checks, String> originAcctNumCol;

  @FXML
  private TableColumn<Checks, String> destAcctNumCol;

  @FXML
  private TableColumn<Checks, String> memoCol;

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
  private TextField originAcctField;

  @FXML
  private TextField destAcctField;


  /**
   * Button that lets manager process checks
   */
  @FXML
  void processClicked(ActionEvent event) {
    //get current date
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(formatters);

    String checkNumberText = checkNumber.getText();
    String destAcctFieldText = destAcctField.getText();
    String originAcctFieldText = originAcctField.getText();

    //check that the check number is not blank and matches a pending check
    if (
        checkNumberText.length() != 0 &&
            getLoginController().hasValidPendingCheck(checkNumberText)
    ) {
      //check that the entered account numbers match actual checking accounts (no savings)
      if (
          originAcctFieldText.length() != 0 &&
              destAcctFieldText.length() != 0 &&
              getLoginController().hasValidCheckingAccount(originAcctFieldText) &&
              getLoginController().hasValidCheckingAccount(destAcctFieldText)
      ) {
        //accounts match
        //make sure the accounts are not the same
        if (!originAcctFieldText.equals(destAcctFieldText)) {
          //find the specific check
          Checks processingCheck = getLoginController().findChecksByCheckNum(checkNumberText, originAcctFieldText, destAcctFieldText);

          //check that info entered matches a pending check
          if (processingCheck != null) {

            String typeOfAccount = processingCheck.getOriginAccountID().substring(9, 11);

            //check which account the check is slated to go to
            if (typeOfAccount.equals("_c")) {
              //check is for a checking account

              //get the deposit amount and acct id from check
              double depositAmt = processingCheck.getAmountCash();

              //check if processing check with cause overdraft
              Checking originCheckingAcct = getLoginController().findCheckingByID(originAcctFieldText);
              double withdrawAmt = depositAmt;
              if (originCheckingAcct.getAccountType().equals("Regular")) {
                //has fee
                withdrawAmt += 0.5;
              }
              //We frown on overdrafts with checks. So check the checking balance alone
              if (originCheckingAcct.getCurrentBalance() < withdrawAmt) {
                /*
                 * there is not enough money. Don't process checks.
                 * (We used to charge fees, but we got rid of them!)
                 */
                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Sorry, not enough money in origin account. Please contact account " +
                        originCheckingAcct.getAccountId() + " about check number " + checkNumberText +
                        " before proceeding."
                );

                confirmationController.showStage();
              } else {
                /*
                 * there is enough money in the checking account! Process the check.
                 * Check if the check is larger than the fee
                 * If yes, deposit money
                 */

                Checking destinationCheckingAcct = getLoginController().findCheckingByID(destAcctFieldText);
                if (destinationCheckingAcct.oneTimeDeposit(depositAmt, getLoginController())) {
                  //The deposit is not smaller than the fee

                  //now withdraw the money
                  originCheckingAcct.oneTimeWithdraw(withdrawAmt);

                  //Create 2 transaction objects
                  Transactions newTrans1 = new Transactions(
                      originAcctFieldText,
                      "withdrawal",
                      "Check number " + checkNumberText + " withdrew " + withdrawAmt + " from your account. Memo: " + processingCheck.getMemo(),
                      currentDate
                  );

                  Transactions newTrans2 = new Transactions(
                      destAcctFieldText,
                      "deposit",
                      "Check number " + checkNumberText + " deposited " + depositAmt + " into account. Memo: " + processingCheck.getMemo(),
                      currentDate
                  );

                  //add transactions to log
                  getLoginController().getTransactionLog().add(newTrans1);
                  getLoginController().getTransactionLog().add(newTrans2);

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
            //something was not entered correctly
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid input");
            a.setHeaderText("Check not processed");
            a.setContentText("Entered information does not match a check needing to be processed.");

            // show the dialog
            a.show();
          }


        } else {
          //origin and destination are not allowed to be the same
          // create an alert
          Alert a = new Alert(Alert.AlertType.WARNING);
          a.setTitle("Invalid input");
          a.setHeaderText("Check not processed");
          a.setContentText("Account numbers cannot match.");

          // show the dialog
          a.show();
        }


      } else {
        //invalid account number
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid input");
        a.setHeaderText("Check not processed");
        a.setContentText("Account number(s) invalid: Please contact the person depositing the check.");

        // show the dialog
        a.show();
      }

    } else {
      //this is not a valid check number
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Invalid input");
      a.setHeaderText("Check not processed");
      a.setContentText("Please enter valid check number.");

      // show the dialog
      a.show();
    }

  }

  /**
   * Button that lets manager stop checks
   */
  @FXML
  void stopClicked(ActionEvent event) {
    //get current date
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(formatters);

    String checkNumberText = checkNumber.getText();
    String destAcctFieldText = destAcctField.getText();
    String originAcctFieldText = originAcctField.getText();


    //check that the check number is not blank and matches a pending check
    if (
        checkNumberText.length() != 0 &&
            getLoginController().hasValidPendingCheck(checkNumberText)
    ) {
      //check that the entered account numbers match actual checking accounts (no savings)
      if (
          originAcctFieldText.length() != 0 &&
              destAcctFieldText.length() != 0 &&
              getLoginController().hasValidCheckingAccount(originAcctFieldText)
      ) {
        //accounts match

        //find the specific check
        Checks stoppedCheck = getLoginController().findChecksByCheckNum(checkNumberText, originAcctFieldText, destAcctFieldText);

        //check that info entered matches a pending check
        if (stoppedCheck != null) {
          //find origin checking account to penalize
          Checking originChecking = getLoginController().findCheckingByID(originAcctFieldText);

          /*
           * Stopped payment requires 15 dollar penalty. Withdraw money if there is money in account;
           * returns true or false depending on if there is money in the account
           */
          if (originChecking.oneTimeWithdraw(15)) {
            //There was money; transaction is done.

            //Create transaction object to reflect penalty
            Transactions newTrans = new Transactions(
                originAcctFieldText,
                "penalty",
                "Penalized $15.00 from account " + originChecking.getAccountId() + " for stopping a check.",
                currentDate
            );

            //add transaction to log
            getLoginController().getTransactionLog().add(newTrans);

            //remove check from pending
            getLoginController().getPendingChecks().remove(stoppedCheck);

            //write the data
            getLoginController().writeBankData();

            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Congratulations, you stopped check " + checkNumberText + ".");

            confirmationController.showStage();
          } else {
            //There wasn't enough money. Need to overdraft.
            double originalCheckingBal = originChecking.getCurrentBalance();
            double overdraftAmount = 15 - originalCheckingBal;
            /*
             * Check if there is a backup savings and that the savings
             * has enough to cover the rest
             */
            if (
                !originChecking.getBackupAccountId().equals("n/a") &&
                    (overdraftAmount <= getLoginController().findSavingsByID(originChecking.getBackupAccountId()).getAccountBalance())
            ) {
              //there is enough money in savings
              Savings backUpSavings = getLoginController().findSavingsByID(originChecking.getBackupAccountId());

              //Withdraw from checking and savings
              originChecking.setCurrentBalance(0);
              originChecking.setInterest("n/a");
              originChecking.setAccountType("Regular");
              backUpSavings.withdraw(overdraftAmount);
              originChecking.setOverdrafts(originChecking.getOverdrafts() + 1);

              //this formats the money amount into currency
              NumberFormat formatter = NumberFormat.getCurrencyInstance();
              String formattedOverdraft = formatter.format(overdraftAmount);
              String formattedOriginalChecking = formatter.format(originalCheckingBal);

              //Create 2 transaction objects
              Transactions newTrans1 = new Transactions(
                  originChecking.getAccountId(),
                  "penalty",
                  "Penalized " + formattedOriginalChecking + " from account " + originChecking.getAccountId() + ".",
                  currentDate
              );

              Transactions newTrans2 = new Transactions(
                  backUpSavings.getAccountId(),
                  "penalty",
                  "Penalized " + formattedOverdraft + " from account " + backUpSavings.getAccountId() + " through overdraft.",
                  currentDate
              );

              //add transaction to log
              getLoginController().getTransactionLog().add(newTrans1);
              getLoginController().getTransactionLog().add(newTrans2);

              //remove check from pending
              getLoginController().getPendingChecks().remove(stoppedCheck);

              //write the data
              getLoginController().writeBankData();

              // create a confirmation screen
              ConfirmationController confirmationController = new ConfirmationController(
                  getCurrentStage(),
                  getLoginController(),
                  getMainPage(),
                  "Congratulations, you have stopped check " + checkNumberText + ".");

              confirmationController.showStage();

            } else {
              //there was not enough in that backup account
              // create an alert
              Alert a = new Alert(Alert.AlertType.WARNING);
              a.setTitle("Not enough money.");
              a.setHeaderText("Check not stopped.");
              a.setContentText("Not enough money in account, even with overdraft, to stop check. Contact account owner.");

              // show the dialog
              a.show();
            }
          }

        } else {
          //something was not entered correctly
          // create an alert
          Alert a = new Alert(Alert.AlertType.WARNING);
          a.setTitle("Invalid input");
          a.setHeaderText("Check not stopped");
          a.setContentText("Entered information does not match a check needing to be processed.");

          // show the dialog
          a.show();
        }


      } else {
        //There wasn't a linked account
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Invalid input");
        a.setHeaderText("Check not stopped");
        a.setContentText("Origin account number does not link to current checking account OR enter \"n/a\" for destination.");

        // show the dialog
        a.show();
      }
    } else {
      //no pending check with that number
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Invalid Check Number");
      a.setHeaderText("Check not stopped");
      a.setContentText("Invalid check number. Please try again.");

      // show the dialog
      a.show();
    }

  }

  //constructor
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

    //sets table columns
    destAcctNumCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("destinationAccountID"));
    originAcctNumCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("originAccountID"));
    amountCashCol.setCellValueFactory(new PropertyValueFactory<Checks, Double>("amountCash"));
    checkNumberCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("checkNumber"));
    dateCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("date"));
    memoCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("memo"));

    //bind list into the table
    checkData.setItems(FXCollections.observableArrayList(getLoginController().getPendingChecks()));
  }

}
