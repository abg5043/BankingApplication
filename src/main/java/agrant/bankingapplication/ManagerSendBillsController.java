package agrant.bankingapplication;

import agrant.bankingapplication.classes.Loans;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerSendBillsController extends Controller {

  @FXML
  private TextField accountNumber;

  @FXML
  private TableView<Loans> creditData;

  @FXML
  private Button exitButton;

  @FXML
  private Button mainPageButton;

  @FXML
  private Button sendBillButton;

  @FXML
  private Label welcomeLabel;


  @FXML
  private TableColumn<Loans, String> accountNumberCol;

  @FXML
  private TableColumn<Loans, Double> currentBalanceCol;

  @FXML
  private TableColumn<Loans, Double> interestRateCol;

  @FXML
  private TableColumn<Loans, String> nextPaymentDueCol;

  @FXML
  private TableColumn<Loans, String> dateBillSentCol;

  @FXML
  private TableColumn<Loans, Double> currentPaymentCol;

  @FXML
  private TableColumn<Loans, String> lastPaymentMadeCol;

  @FXML
  private TableColumn<Loans, Integer> missedPaymentFlagCol;

  @FXML
  private TableColumn<Loans, String> loanTypeCol;

  @FXML
  private TableColumn<Loans, Integer> creditLimitCol;

  @FXML
  private TableColumn<Loans, Integer> monthLeftCol;

  @FXML
  void sendBillClicked(ActionEvent event) {
    String billedAcc = accountNumber.getText();
    //Check that the text is not blank and matches an account
    if(billedAcc.length() == 11 && getLoginController().hasValidLoanAccount(billedAcc)) {

      //get current date
      LocalDate date = LocalDate.now();
      DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
      String currentDate = date.format(formatters);

      //get loan
      Loans currentLoan = getLoginController().findLoanByID(billedAcc);

      //print bill for sending
      currentLoan.printBill(
          currentDate + "_Bill.md",
          getLoginController().getTransactionLog(),
          getLoginController().getCurrentUser()
      );

      //update bill sent date
      currentLoan.setDateBillSent(currentDate);

      //write the data
      getLoginController().writeBankData();

      // create a confirmation screen
      ConfirmationController confirmationController = new ConfirmationController(
          getCurrentStage(),
          getLoginController(),
          getMainPage(),
          "Congratulations! Bill sent to account " + billedAcc + "."
      );

      confirmationController.showStage();
    } else {
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Bill not sent");
      a.setHeaderText("Bill not sent");
      a.setContentText("Invalid account number. Please try again.");

      // show the dialog
      a.show();
    }

  }


  public ManagerSendBillsController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
    super(currentStage, loginController, managerOpeningController);
    setCurrentViewFile("manager-send-bills.fxml");
    setCurrentViewTitle("Send Loan Bills");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");

    accountNumberCol.setCellValueFactory(new PropertyValueFactory<Loans, String>("accountId"));
    currentBalanceCol.setCellValueFactory(new PropertyValueFactory<Loans, Double>("currentBalance"));
    interestRateCol.setCellValueFactory(new PropertyValueFactory<Loans, Double>("interestRate"));
    nextPaymentDueCol.setCellValueFactory(new PropertyValueFactory<Loans, String>("nextPaymentDueDate"));
    dateBillSentCol.setCellValueFactory(new PropertyValueFactory<Loans, String>("dateBillSent"));
    currentPaymentCol.setCellValueFactory(new PropertyValueFactory<Loans, Double>("currentPaymentAmount"));
    lastPaymentMadeCol.setCellValueFactory(new PropertyValueFactory<Loans, String>("lastPaymentMade"));
    missedPaymentFlagCol.setCellValueFactory(new PropertyValueFactory<Loans, Integer>("missedPaymentFlag"));
    loanTypeCol.setCellValueFactory(new PropertyValueFactory<Loans, String>("loanType"));
    creditLimitCol.setCellValueFactory(new PropertyValueFactory<Loans, Integer>("creditLimit"));
    monthLeftCol.setCellValueFactory(new PropertyValueFactory<Loans, Integer>("monthsLeft"));



    //bind list into the table
    creditData.setItems(FXCollections.observableArrayList(getLoginController().getLoansData()));

  }

}
