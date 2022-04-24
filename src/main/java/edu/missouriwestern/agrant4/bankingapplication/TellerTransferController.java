package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.Checking;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import edu.missouriwestern.agrant4.bankingapplication.classes.Transactions;
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

public class TellerTransferController extends Controller {

  @FXML
  private Button transferButton;

  @FXML
  private TextField transferFromField;

  @FXML
  private TextField transferToField;

  @FXML
  private Label welcomeLabel;

  @FXML
  private TextField moneyField;

  @FXML
  void transferClicked(ActionEvent event) {
    //get current date
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(formatters);

    try {
      double transferredMoney = Double.parseDouble(moneyField.getText());
      String fromID = transferFromField.getText();
      String toID = transferToField.getText();

      //this formats the money amount into currency
      NumberFormat formatter = NumberFormat.getCurrencyInstance();
      String formattedTransferredMoney = formatter.format(transferredMoney);

      //check that both accounts are for the same user
      if (fromID.substring(0, 9).equals(toID.substring(0, 9))) {
        //check that from account is valid
        if (
            getLoginController().hasValidSavingsAccount(fromID) ||
                getLoginController().hasValidCheckingAccount(fromID)
        ) {
          //check the type of from account
          if (fromID.substring(9, 11).equals("_s")) {
            //We are transferring from a savings, check that the other is a checking
            if (getLoginController().hasValidCheckingAccount(toID)) {
              Savings originSavings = getLoginController().findSavingsByID(fromID);
              Checking toChecking = getLoginController().findCheckingByID(toID);

              //Withdraw first. This only runs if there is enough money
              if (originSavings.withdraw(transferredMoney)) {
                //deposit next; only runs if deposit is less than fee
                if(toChecking.oneTimeDeposit(transferredMoney)) {
                  //Create two transaction objects
                  Transactions newTrans1 = new Transactions(
                      fromID,
                      "transfer",
                      "Transferred " + transferredMoney + " to account " + toID,
                      currentDate
                  );

                  Transactions newTrans2 = new Transactions(
                      toID,
                      "transfer",
                      "Transferred " + transferredMoney + " from account " + fromID,
                      currentDate
                  );

                  //add transactions to log
                  getLoginController().getTransactionLog().add(newTrans1);
                  getLoginController().getTransactionLog().add(newTrans2);

                  //write the data
                  getLoginController().writeBankData();

                  // create a confirmation screen
                  ConfirmationController confirmationController = new ConfirmationController(
                      getCurrentStage(),
                      getLoginController(),
                      getMainPage(),
                      "Congratulations, you transferred " + formattedTransferredMoney +
                          " from account number " + fromID + " to account number " +
                          toID + "."
                  );

                  confirmationController.showStage();
                } else {
                  // create an alert
                  Alert a = new Alert(Alert.AlertType.WARNING);
                  a.setTitle("Too small a deposit.");
                  a.setHeaderText("Transfer not processed.");
                  a.setContentText("Transfer less than fee.");

                  // show the dialog
                  a.show();
                }



              }
            } else {
              // create an alert
              Alert a = new Alert(Alert.AlertType.WARNING);
              a.setTitle("Money Not Transferred");
              a.setHeaderText("Invalid ID");
              a.setContentText("Please ensure you use valid checking ID for \"to\" account.");

              // show the dialog
              a.show();
            }

          } else {
            //We are transferring from a checking, check that the other is a savings
            if (getLoginController().hasValidSavingsAccount(toID)) {
              Savings toSavings = getLoginController().findSavingsByID(toID);
              Checking fromChecking = getLoginController().findCheckingByID(fromID);

              //Check that the checking has enough money since we can't cover with overdraft
              if (fromChecking.getCurrentBalance() >= transferredMoney) {
                //We have enough! Withdraw first.
                fromChecking.oneTimeWithdraw(transferredMoney);
                //deposit next
                toSavings.deposit(transferredMoney);

                //Create two transaction objects
                Transactions newTrans1 = new Transactions(
                    fromID,
                    "transfer",
                    "Transferred " + transferredMoney + " to account " + toID,
                    currentDate
                );

                Transactions newTrans2 = new Transactions(
                    toID,
                    "transfer",
                    "Transferred " + transferredMoney + " from account " + fromID,
                    currentDate
                );

                //add transactions to log
                getLoginController().getTransactionLog().add(newTrans1);
                getLoginController().getTransactionLog().add(newTrans2);

                //write the data
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you transferred " + formattedTransferredMoney +
                        " from account number " + fromID + " to account number " +
                        toID + "."
                );

                confirmationController.showStage();
              } else {
                  //we do not have enough money
                  // create an alert
                  Alert a = new Alert(Alert.AlertType.WARNING);
                  a.setTitle("Money Not Transferred");
                  a.setHeaderText("Not enough money in checking.");
                  a.setContentText("Please ensure you have enough money in checking.");

                  // show the dialog
                  a.show();
              }
            } else {
              // create an alert
              Alert a = new Alert(Alert.AlertType.WARNING);
              a.setTitle("Money Not Transferred");
              a.setHeaderText("Invalid ID");
              a.setContentText("Please ensure you use valid savings (not CD) ID for \"to\" account.");

              // show the dialog
              a.show();
            }
          }

        } else {
          // create an alert
          Alert a = new Alert(Alert.AlertType.WARNING);
          a.setTitle("Money Not Transferred");
          a.setHeaderText("Invalid ID");
          a.setContentText("Please ensure you use valid ID for \"from\" account that isn't a CD.");

          // show the dialog
          a.show();
        }

      } else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Money Not Transferred");
        a.setHeaderText("Invalid ID");
        a.setContentText("Same user must own both accounts.");

        // show the dialog
        a.show();
      }

    } catch (NumberFormatException e) {
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Money Not Transferred");
      a.setHeaderText("Invalid formatting");
      a.setContentText("Please ensure you use numbers in numeric fields.");

      // show the dialog
      a.show();
    }
  }

  private void transferMoney(Checking checking, Savings savings) {
  }

  public TellerTransferController(
      Stage currentStage,
      LoginController loginController,
      TellerOpeningController tellerOpeningController
  ) {
    super(currentStage, loginController, tellerOpeningController);
    setCurrentViewFile("teller-transfer.fxml");
    setCurrentViewTitle("Transfer Money");
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
