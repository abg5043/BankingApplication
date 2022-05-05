package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for teller screen that allows them to credit/debit accounts
 */
public class TellerCreditController extends Controller {

  @FXML
  private TextField accountField;

  @FXML
  private Button creditButton;

  @FXML
  private Button debitButton;

  @FXML
  private TextField moneyField;

  @FXML
  private Label welcomeLabel;

  /**
   * Button that lets teller credit an account with money
   */
  @FXML
  void creditClicked(ActionEvent event) {
    //get current date
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(formatters);

    try {
      //check formatting of doubles
      double incomingMoney = Double.parseDouble(moneyField.getText());
      String accID = accountField.getText();

      //this formats the money amount into currency
      NumberFormat formatter = NumberFormat.getCurrencyInstance();
      String formattedIncomingMoney = formatter.format(incomingMoney);


      //Checks if this is a valid savings or checking account
      if (
          accID.length() == 11 &&
              (
                  getLoginController().hasValidCheckingAccount(accID) ||
                      getLoginController().hasValidSavingsAccount(accID)
              )
      ) {

        String typeOfAccount = accID.substring(9, 11);

        if (typeOfAccount.equals("_c")) {
          //is going to checking account

          //deposit the money into the appropriate account; only runs if deposit is less than fee
          if (getLoginController().findCheckingByID(accID).oneTimeDeposit(incomingMoney, getLoginController())) {
            confirmDeposit(currentDate, accID, formattedIncomingMoney);

          } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Too small a deposit.");
            a.setHeaderText("Transfer not processed.");
            a.setContentText("Transfer less than fee.");

            // show the dialog
            a.show();
          }


        } else {
          //is going to savings account

          //deposit the money into the appropriate account
          getLoginController().findSavingsByID(accID).deposit(incomingMoney);

          //confirm deposit
          confirmDeposit(currentDate, accID, formattedIncomingMoney);
        }


      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Money Not Credited");
        a.setHeaderText("Invalid account");
        a.setContentText("Please ensure you enter in a valid savings or checking account (no CD).");

        // show the dialog
        a.show();
      }

    } catch (NumberFormatException e) {
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Money Not Credited");
      a.setHeaderText("Invalid formatting");
      a.setContentText("Please ensure you use numbers in numeric fields.");

      // show the dialog
      a.show();
    }
  }

  /**
   * Method that confirms a deposit by creating transaction objects and starting confirmation page
   *
   * @param currentDate - String of current date
   * @param accID - account ID
   * @param formattedIncomingMoney - incoming money as $__.__
   */
  private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
    //Create transaction object
    Transactions newTrans = new Transactions(
        accID,
        "deposit",
        "Deposited " + formattedIncomingMoney + " into account " + accID + ".",
        currentDate
    );

    //add transaction to log
    getLoginController().getTransactionLog().add(newTrans);

    //write the data
    getLoginController().writeBankData();


    // create a confirmation screen
    ConfirmationController confirmationController = new ConfirmationController(
        getCurrentStage(),
        getLoginController(),
        getMainPage(),
        "Congratulations, you credited " + formattedIncomingMoney +
            " to account number " + accID + "."
    );

    confirmationController.showStage();
  }

  /**
   * Button that lets teller withdraw money from accounts for customers
   */
  @FXML
  void debitClicked(ActionEvent event) {
    //get current date
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(formatters);

    try {
      //checks if fields contain numbers
      double outgoingMoney = Double.parseDouble(moneyField.getText());
      String accID = accountField.getText();

      //this formats the money amount into currency
      NumberFormat formatter = NumberFormat.getCurrencyInstance();
      String formattedOutgoingMoney = formatter.format(outgoingMoney);

      //Checks if this is a valid savings or checking account
      if (
          accID.length() == 11 &&
              (getLoginController().hasValidCheckingAccount(accID) ||
                  getLoginController().hasValidSavingsAccount(accID) ||
                  getLoginController().hasValidCDAccount(accID)
              )
      ) {

        String typeOfAccount = accID.substring(9, 11);

        if (typeOfAccount.equals("_c")) {
          //money is being withdrawn from a checking account
          Checking targetedChecking = getLoginController().findCheckingByID(accID);

          /*
           * Withdraw money if there is money in account;
           * returns true or false depending on if there is money in the account
           */
          if (targetedChecking.oneTimeWithdraw(outgoingMoney)) {
            //There was money; transaction is done.

            //Create transaction object
            Transactions newTrans = new Transactions(
                accID,
                "withdraw",
                "Withdrew " + formattedOutgoingMoney + " from account " + targetedChecking.getAccountId(),
                currentDate
            );

            //add transaction to log
            getLoginController().getTransactionLog().add(newTrans);

            //write the data
            getLoginController().writeBankData();


            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Congratulations, you debited " + formattedOutgoingMoney +
                    " from account number " + accID + ".");

            confirmationController.showStage();
          } else {
            //There wasn't enough money; must overdraft
            double originalCheckingBalance = targetedChecking.getCurrentBalance();
            double overdraftAmount = outgoingMoney - originalCheckingBalance;

            if (targetedChecking.getAccountType().equals("Regular")) {
              overdraftAmount += 0.5; //includes 50 cent charge
            }
            /*
             * Check if there is a backup savings and that the savings
             * has enough to cover the rest
             */
            if (
                !targetedChecking.getBackupAccountId().equals("n/a") &&
                    (overdraftAmount <= getLoginController().findSavingsByID(targetedChecking.getBackupAccountId()).getAccountBalance())
            ) {
              Savings backUpSavings = getLoginController().findSavingsByID(targetedChecking.getBackupAccountId());

              //Withdraw from checking and savings
              targetedChecking.setCurrentBalance(0);
              targetedChecking.setInterest("n/a");
              targetedChecking.setAccountType("Regular");
              backUpSavings.withdraw(overdraftAmount);
              targetedChecking.setOverdrafts(targetedChecking.getOverdrafts() + 1);

              String formattedChecking = formatter.format(originalCheckingBalance);
              String formattedOverdraft = formatter.format(overdraftAmount);


              //Create two transaction objects
              Transactions newTrans1 = new Transactions(
                  targetedChecking.getAccountId(),
                  "withdraw",
                  "Withdrew " + formattedChecking + " from account " + targetedChecking.getAccountId() + ".",
                  currentDate
              );

              //Create two transaction objects
              Transactions newTrans2 = new Transactions(
                  backUpSavings.getAccountId(),
                  "withdraw",
                  "Withdrew " + formattedOverdraft + " from account " + backUpSavings.getAccountId() + ".",
                  currentDate
              );

              //add transaction to log
              getLoginController().getTransactionLog().add(newTrans1);
              getLoginController().getTransactionLog().add(newTrans2);

              //write the data
              getLoginController().writeBankData();


              // create a confirmation screen
              ConfirmationController confirmationController = new ConfirmationController(
                  getCurrentStage(),
                  getLoginController(),
                  getMainPage(),
                  "Congratulations, you debited " + formattedOutgoingMoney +
                      " from account number " + accID + ".");

              confirmationController.showStage();

            } else {
              //There wasn't a linked account or there was not enough in that backup account
              // create an alert
              Alert a = new Alert(Alert.AlertType.WARNING);
              a.setTitle("Not enough money.");
              a.setHeaderText("Withdraw not processed.");
              a.setContentText("Not enough money in account, even with overdraft.");

              // show the dialog
              a.show();
            }
          }

        } else {
          //is coming from savings account

          //make pointer to savings account
          Savings targetedAccount;
          Boolean isCD;
          if (getLoginController().findSavingsByID(accID) == null) {
            //this is a CD
            targetedAccount = getLoginController().findCDByID(accID);
          } else {
            //this is not a CD
            targetedAccount = getLoginController().findSavingsByID(accID);
          }

          DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

          //Check if this is a CD that is withdrawing early
          if (targetedAccount.isCD() && LocalDate.parse(targetedAccount.getDueDate(), newFormatter).isAfter(LocalDate.now())) {

            //withdraw now
            withdrawFromSavings(
                currentDate,
                outgoingMoney,
                accID,
                formattedOutgoingMoney,
                targetedAccount,
                "\n\nPlease note we also debited a 20% penalty for early withdrawal."
            );

          } else {
            //CD and due date has passed or normal savings. Withdraw with no penalty
            withdrawFromSavings(
                currentDate,
                outgoingMoney,
                accID,
                formattedOutgoingMoney,
                targetedAccount,
                ""
            );
          }

        }
      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Money Not Debited");
        a.setHeaderText("Invalid account");
        a.setContentText("Please ensure you enter in a valid account.");

        // show the dialog
        a.show();
      }
    } catch (NumberFormatException e) {
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Money Not Debited");
      a.setHeaderText("Invalid formatting");
      a.setContentText("Please ensure you use numbers in numeric fields.");

      // show the dialog
      a.show();
    }

  }

  /**
   * Method for withdrawing money from a savings account
   *
   * @param currentDate - today's date
   * @param outgoingMoney - money to be withdrawn
   * @param accID - account ID of savings account
   * @param formattedOutgoingMoney - formatted money as $__.__
   * @param targetedAccounted - Savings object of targetted account
   * @param appendedMessage - message if there is a fee
   */
  private void withdrawFromSavings(
      String currentDate,
      double outgoingMoney,
      String accID,
      String formattedOutgoingMoney,
      Savings targetedAccounted,
      String appendedMessage
  ) {

    //checks if there is money. If there is, it withdraws and returns true
    if (targetedAccounted.withdraw(outgoingMoney)) {
      //transaction is done.
      //Create transaction object
      Transactions newTrans = new Transactions(
          accID,
          "withdraw",
          "Withdrew " + formattedOutgoingMoney + " from account " + targetedAccounted.getAccountId(),
          currentDate
      );

      //add transaction to log
      getLoginController().getTransactionLog().add(newTrans);

      //write the data
      getLoginController().writeBankData();


      // create a confirmation screen
      ConfirmationController confirmationController = new ConfirmationController(
          getCurrentStage(),
          getLoginController(),
          getMainPage(),
          "Congratulations, you credited " + formattedOutgoingMoney +
              " to account number " + accID + ". " + appendedMessage
      );

      confirmationController.showStage();
    }
  }


  //Constructor for TellerCreditController
  public TellerCreditController(
      Stage currentStage,
      LoginController loginController,
      TellerOpeningController tellerOpeningController
  ) {
    super(currentStage, loginController, tellerOpeningController);
    setCurrentViewFile("teller-credit.fxml");
    setCurrentViewTitle("Credit/Debit Money");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
  }

}
