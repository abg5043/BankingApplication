package agrant.bankingapplication;

import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller where customer can maanager their savings account
 */
public class CustomerSavingsController extends Controller {
  @FXML
  private Button withdrawButton;
  @FXML
  private Button depositButton;
  @FXML
  private Label welcomeLabel;
  @FXML
  private TextField amountField;

  @FXML
  private TextArea accountInfoArea;

  /**
   * Button that lets customer withdraw money
   */
  @FXML
  void withdrawClicked(ActionEvent event) {
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(dateFormatter);

    try {
      //check that numeric fields are formatted properly
      double withdrawAmount = Double.parseDouble(this.amountField.getText());

      //format withdraw amount as currency
      NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
      String formattedWDAmount = numberFormatter.format(withdrawAmount);

      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
      Savings targetedSavings = this.getLoginController().findSavingsByID(accID);

      //check that accID is valid
      if (this.getLoginController().hasValidSavingsAccount(accID)) {
        //check that you can withdraw this amount of money. If so, it withdraws
        if (this.getLoginController().findSavingsByID(accID).withdraw(withdrawAmount)) {
          //we can withdraw!
          //Create transaction
          Transactions newTrans = new Transactions(
              targetedSavings.getAccountId(),
              "withdraw",
              "Withdrew " + formattedWDAmount + " from account " + targetedSavings.getAccountId() + ".",
              currentDate
          );

          //add transaction to transaction log
          this.getLoginController().getTransactionLog().add(newTrans);

          //write data to csv
          this.getLoginController().writeBankData();

          //bring up confirmation controller
          ConfirmationController confirmationController = new ConfirmationController(
              this.getCurrentStage(),
              this.getLoginController(),
              this.getMainPage(),
              "Congratulations, you withdrew " + formattedWDAmount +
                  " from account number " + accID + ".");

          confirmationController.showStage();
        }
      }
    } catch (NumberFormatException var21) {
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Money Not Credited");
      a.setHeaderText("Invalid formatting");
      a.setContentText("Please ensure you use numbers in numeric fields.");
      a.show();
    }

  }

  /**
   * Button that lets customer deposit to savings account
   */
  @FXML
  void depositClicked(ActionEvent event) {
    //get local date
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(dateFormatter);

    try {
      //check that numeric fields are correctly formatted
      double depAmount = Double.parseDouble(this.amountField.getText());

      //format numbers as currency for later
      NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
      String formattedDepAmount = numberFormatter.format(depAmount);

      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());

      //check that accountID is valid
      if (this.getLoginController().hasValidSavingsAccount(accID)) {
        //deposit money
        this.getLoginController().findSavingsByID(accID).deposit(depAmount);
        //confirm that deposit
        this.confirmDeposit(currentDate, accID, formattedDepAmount);
      } else {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Too small a deposit.");
        a.setHeaderText("Transfer not processed.");
        a.setContentText("Transfer less than fee.");

        a.show();
      }
    } catch (NumberFormatException nfe) {
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Money Not Credited");
      a.setHeaderText("Invalid formatting");
      a.setContentText("Please ensure you use numbers in numeric fields.");
      a.show();
    }
  }

  /**
   * Method for confirming a savings deposit
   *
   * @param currentDate - today's date
   * @param accID - savings ID
   * @param formattedIncomingMoney - amount of money as $__.__
   */
  private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
    //create new transaction
    Transactions newTrans = new Transactions(accID, "deposit", "Deposited " + formattedIncomingMoney + " into account." + accID, currentDate);

    //add transaction to transaction log
    this.getLoginController().getTransactionLog().add(newTrans);

    //write data to
    this.getLoginController().writeBankData();

    //bring up confirmation screeen
    ConfirmationController confirmationController = new ConfirmationController(
        this.getCurrentStage(),
        this.getLoginController(),
        this.getMainPage(), "Congratulations, you deposited "
        + formattedIncomingMoney
        + " to account number "
        + accID
        + "."
    );

    confirmationController.showStage();
  }

  private void setAccountInfoArea(String text) {
    this.accountInfoArea.setText(findAccountInfo(text));
  }

  /**
   * Method for finding savings account toString
   *
   * @param accID - savings account ID
   * @return - toString for given savings
   */
  private String findAccountInfo(String accID) {
    String returnString = "Error: No Account found";
    for(Savings savings : getLoginController().getSavingsData()) {
      if(savings.getAccountId().equals(accID)) {
        returnString = savings.toString();
        break;
      }
    }
    return returnString;
  }

  //constructor
  public CustomerSavingsController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController) {
    super(currentStage, loginController, customerOpeningController);
    this.setCurrentViewFile("customer-savings.fxml");
    this.setCurrentViewTitle("Loan Account");
    this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_s");
  }

  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
  }
}
