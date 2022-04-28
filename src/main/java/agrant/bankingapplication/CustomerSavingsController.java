package agrant.bankingapplication;

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
  void withdrawClicked(ActionEvent event) {
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(dateFormatter);

    try {
      double withdrawAmount = Double.parseDouble(this.amountField.getText());
      NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
      String formattedWDAmount = numberFormatter.format(withdrawAmount);
      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());
      Savings targetedSavings = this.getLoginController().findSavingsByID(accID);

      if (this.getLoginController().hasValidSavingsAccount(accID)) {
        if (this.getLoginController().findSavingsByID(accID).withdraw(withdrawAmount)) {
          this.confirmWithdraw(currentDate, accID, formattedWDAmount);

          Transactions newTrans = new Transactions(
              targetedSavings.getAccountId(),
              "withdraw",
              "Withdrew " + " from account " + targetedSavings.getAccountId() + ".",
              currentDate
          );

          this.getLoginController().getTransactionLog().add(newTrans);

          this.getLoginController().writeBankData();

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

  private void confirmWithdraw(String currentDate, String accID, String formattedIncomingMoney) {
    Transactions newTrans = new Transactions(accID, "withdraw", "Withdrew " + formattedIncomingMoney + " from account.", currentDate);
    this.getLoginController().getTransactionLog().add(newTrans);
    this.getLoginController().writeBankData();
    ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you withdrew " + formattedIncomingMoney + " from account number " + accID + ".");
    confirmationController.showStage();
  }

  @FXML
  void depositClicked(ActionEvent event) {
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String currentDate = date.format(dateFormatter);

    try {
      double depAmount = Double.parseDouble(this.amountField.getText());
      NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
      String formattedDepAmount = numberFormatter.format(depAmount);
      String accID = String.format("%s_s", this.getLoginController().getCurrentUser().getSSN());

      if (this.getLoginController().hasValidSavingsAccount(accID)) {
        this.getLoginController().findSavingsByID(accID).deposit(depAmount);
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

  private void confirmDeposit(String currentDate, String accID, String formattedIncomingMoney) {
    Transactions newTrans = new Transactions(accID, "deposit", "Deposited " + formattedIncomingMoney + " into account.", currentDate);
    this.getLoginController().getTransactionLog().add(newTrans);
    this.getLoginController().writeBankData();
    ConfirmationController confirmationController = new ConfirmationController(this.getCurrentStage(), this.getLoginController(), this.getMainPage(), "Congratulations, you deposited " + formattedIncomingMoney + " to account number " + accID + ".");
    confirmationController.showStage();
  }

  public CustomerSavingsController(Stage currentStage, LoginController loginController, CustomerPINCheckController customerPINCheckController) {
    super(currentStage, loginController, new CustomerOpeningController(currentStage, loginController));
    this.setCurrentViewFile("customer-savings.fxml");
    this.setCurrentViewTitle("Loan Account");
    this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
  }

  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
  }
}
