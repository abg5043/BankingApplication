package agrant.bankingapplication;

import agrant.bankingapplication.classes.Loans;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Controller for screen where customer can create a new loan
 */
public class CustomerNewLoanController extends Controller {
    ObservableList<Integer> mortgageChoices = FXCollections.observableArrayList(15, 30);

    @FXML
    private Button newMortgageButton;

    @FXML
    private Button newShortTermButton;

    @FXML
    private Button newCreditButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private TextField paymentAmountField;

    @FXML
    private ChoiceBox mortgageLoanYearChoice;

    @FXML
    private Label welcomeLabel;

    /**
     * Button that lets customer create a new mortgage application
     */
    @FXML
    void newMortgageClicked(ActionEvent event) {
        String accID = String.format("%s_l", this.getLoginController().getCurrentUser().getSSN());

        //check that the customer doesn't have a loan yet
        if(this.getLoginController().findLoanByID(accID) == null){
            try{
                //check that numeric fields are numbers
                double payAmt = Double.parseDouble(paymentAmountField.getText());

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedPayAmt = formatter.format(payAmt);

                //apply for the loan
                applyForLoan(accID, "Mortgage", payAmt);

            }catch(NumberFormatException nfe){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Loan Application Failed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");
                a.show();
            }

        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot create a new loan.");
            a.setHeaderText("Existing Account.");
            a.setContentText("Cannot create a new loan while one exists.");
            a.show();
        }
    }

    /**
     * Button that lets customer apply for a new short-term loan
     */
    @FXML
    void newShortTermClicked(ActionEvent event) {
        String accID = String.format("%s_l", this.getLoginController().getCurrentUser().getSSN());

        //check that they don't already have a short-term loan
        if(this.getLoginController().findLoanByID(accID) == null){
            try{
                //check that numeric fields are numbers
                double payAmt = Double.parseDouble(paymentAmountField.getText());

                //make sure that they want a loan more than 0 dollars
                if(payAmt > 0) {
                    //this formats the money amount into currency
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    String formattedPayAmt = formatter.format(payAmt);

                    applyForLoan(accID, "Short-Term", payAmt);
                }else {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Cannot Create Empty Loan.");
                    a.setHeaderText("Invalid loan amount.");
                    a.setContentText("Loan amount must be over 0.");
                    a.show();
                }

            }catch(NumberFormatException nfe){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Loan Application Failed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");
                a.show();
            }

        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot create a new loan.");
            a.setHeaderText("Existing Account.");
            a.setContentText("Cannot create a new loan while one exists.");
            a.show();
        }
    }

    /**
     * Button that lets customer create a new credit card application
     */
    @FXML
    void newCreditClicked(ActionEvent event) {
        String accID = String.format("%s_l", this.getLoginController().getCurrentUser().getSSN());

        if(this.getLoginController().findLoanByID(accID) == null){
            try{
                double payAmt = Double.parseDouble(paymentAmountField.getText());

                if(payAmt > 0) {
                    //this formats the money amount into currency
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    String formattedPayAmt = formatter.format(payAmt);

                    applyForLoan(accID, "Credit", payAmt);
                }else {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Cannot Create Empty Loan.");
                    a.setHeaderText("Invalid loan amount.");
                    a.setContentText("Loan amount must be over 0.");
                    a.show();
                }

            }catch(NumberFormatException nfe){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Loan Application Failed");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you use numbers in numeric fields.");
                a.show();
            }

        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot create a new loan.");
            a.setHeaderText("Existing Account.");
            a.setContentText("Cannot create a new loan while one exists.");
            a.show();
        }
    }

    /**
     * Method for applying for a loan
     *
     * @param accID - customer's account ID
     * @param loanType - loan type
     * @param loanAmount - starting balance for loan
     */
    public void applyForLoan(String accID, String loanType, double loanAmount){
        //get current date
        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = currentDate;
        LocalDate nextPayment = currentDate;

        try {
            //check numeric fields are numbers
            if (loanType.equals("Mortgage") || loanType.equals("Short-Term")) {
                if (loanType.equals("Mortgage")) {
                    //Get selected loan term
                    int loanTerm = Integer.parseInt(mortgageLoanYearChoice.getSelectionModel().getSelectedItem().toString());
                    dueDate = currentDate.plusYears(loanTerm);
                    loanType = loanType + "-" + loanTerm;
                } else {
                    //5 years for short-term loan
                    dueDate = currentDate.plusYears(5);
                }
                //Set next payment due
                nextPayment = currentDate.plusMonths(1);

                //Get months between current date and due date
                int monthsLeft = Period.between(currentDate, dueDate).getYears() * 12;

                //get dates
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String formattedCurrentDate = currentDate.format(formatters);
                String formattedDueDate = dueDate.format(formatters);
                String formattedNextPaymentDate = nextPayment.format(formatters);

                //Interest rates are set here
                double interestRate = 0.017;
                String formattedInterest = String.format("%.2f",(interestRate * 100));

                //calculate next payment amount
                double nextPaymentAmt = ((loanAmount/monthsLeft) + ((loanAmount/2.0) * (monthsLeft/12.0) * interestRate))/2.0;
                nextPaymentAmt = Math.round(nextPaymentAmt * 100.0) / 100.0;

                //Create new loan object
                Loans loanApp = new Loans(accID, loanAmount, Double.parseDouble(formattedInterest), formattedNextPaymentDate, nextPaymentAmt, "n/a", 0, loanType, -1, monthsLeft);

                //Add loan to loan applications
                getLoginController().getLoanApplications().add(loanApp);

                //write data
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you applied for a loan!"
                );

                confirmationController.showStage();
            }else{
                //Credit account
                nextPayment = currentDate.plusMonths(1);

                //get dates
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String formattedCurrentDate = currentDate.format(formatters);
                String formattedDueDate = dueDate.format(formatters);
                String formattedNextPaymentDate = nextPayment.format(formatters);

                //Set interest rate here
                NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
                double interestRate = 1.7;

                //Create new loan object
                Loans loanApp = new Loans(accID, 0.00, interestRate, formattedNextPaymentDate, 0.00, "n/a", 0, loanType, (int)loanAmount, -1);

                //Add loan to loan applications
                getLoginController().getLoanApplications().add(loanApp);

                //write data
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you applied for a loan!"
                );

                confirmationController.showStage();
            }
        }catch (NumberFormatException var21) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Loan Application Unsuccessful");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields and/or select a loan term.");
            a.show();
        }
    }

    //constructor
    public CustomerNewLoanController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-new-loan.fxml");
        this.setCurrentViewTitle("Apply For New Loan");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
        mortgageLoanYearChoice.setValue("15");
        mortgageLoanYearChoice.setItems(mortgageChoices);
    }
}