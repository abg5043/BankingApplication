package agrant.bankingapplication;

import agrant.bankingapplication.classes.Loans;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for screen where manager can manage loans
 */
public class ManagerLoansController extends Controller{

    @FXML
    private TextField accountNumber;

    @FXML
    private Button approveButton;

    @FXML
    private Button denyButton;

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
    private TableView<Loans> loansData;

    @FXML
    private Label welcomeLabel;

    /**
     * Button that let's manager approve loans
     */
    @FXML
    void approveClicked(ActionEvent event) {
        String loanAcc = accountNumber.getText();

        //Check that the text is not blank and matches an application
        if(
            loanAcc.length() == 11 &&
            getLoginController().hasValidLoanApplication(loanAcc)
        ) {
            //check that the user doesn't already have a loan account
            if(!getLoginController().hasValidLoanAccount(loanAcc)) {
                //create pointer to chosen loan application
                Loans newLoan = getLoginController().findLoanApplicationByID(loanAcc);

                //add loan application to loans data arraylist
                getLoginController().getLoansData().add(newLoan);

                //remove loan application
                getLoginController().getLoanApplications().remove(newLoan);

                //write new data to csv
                getLoginController().writeBankData();


                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Loan approved for account number " + loanAcc + "."
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Loan Not Approved");
                a.setHeaderText("User already has loan");
                a.setContentText("Sorry. Users are only allowed one loan.");

                // show the dialog
                a.show();
            }
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid Account Number");
            a.setHeaderText("Loan not approved");
            a.setContentText("Invalid account number. Please try again.");

            // show the dialog
            a.show();
        }
    }

    /**
     * Button that lets manager deny loans
     */
    @FXML
    void denyClicked(ActionEvent event) {
        String loanAcc = accountNumber.getText();

        //Check that the text is not blank and matches a loan application
        if(loanAcc.length() == 11 && getLoginController().hasValidLoanApplication(loanAcc)) {

            //create pointer to chosen loan application
            Loans newLoan = getLoginController().findLoanApplicationByID(loanAcc);

            //remove loan application
            getLoginController().getLoanApplications().remove(newLoan);

            //write new data to csv
            getLoginController().writeBankData();

            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Loan for account number " + loanAcc + " was denied."
            );

            confirmationController.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid Account Number");
            a.setHeaderText("Loan not denied");
            a.setContentText("Invalid account number. Please try again.");

            // show the dialog
            a.show();
        }
    }

    //constructor
    public ManagerLoansController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-loans.fxml");
        setCurrentViewTitle("Manage Loan Applications");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");

        //set table columns
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
        loansData.setItems(FXCollections.observableArrayList(getLoginController().getLoanApplications()));
    }

}
