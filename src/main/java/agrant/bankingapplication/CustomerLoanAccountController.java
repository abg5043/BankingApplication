package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
import agrant.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerLoanAccountController extends Controller {
    private boolean isMortgage;
    @FXML
    private Button newLoanButton;

    @FXML
    private Button mortgageButton;

    @FXML
    private Button shortTermButton;

    @FXML
    private Button creditCardButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    void newLoanClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());

        if(!getLoginController().hasValidLoanApplication(accID) && !getLoginController().hasValidLoanAccount(accID)) {
            CustomerNewLoanController customerNewLoanController = new CustomerNewLoanController(
                    this.getCurrentStage(),
                    this.getLoginController(),
                (CustomerOpeningController) this.getMainPage()
            );

            customerNewLoanController.showStage();
        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot create a new loan.");
            a.setHeaderText("Existing Loan Account or Application.");
            a.setContentText("Cannot create a new loan while valid loan account or application exists.");
            a.show();
        }
    }

    @FXML
    void mortgageClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());

        if(getLoginController().hasValidLoanAccount(accID) &&
            (getLoginController().findLoanByID(accID).getLoanType().equals("Mortgage-15") ||
                getLoginController().findLoanByID(accID).getLoanType().equals("Mortgage-30")
            )
        ){
        isMortgage = true;
        CustomerLoanAccountTermController customerLoanAccountTermController = new CustomerLoanAccountTermController(
                getCurrentStage(),
                getLoginController(),
            (CustomerOpeningController) this.getMainPage(),
                isMortgage);

        customerLoanAccountTermController.showStage();
        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot Enter Mortgage Account");
            a.setHeaderText("No Existing Mortgage Account");
            a.setContentText("Please create a new mortgage account or click correct loan account type");
            a.show();
        }
    }


    @FXML
    void shortTermClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());

        if(getLoginController().hasValidLoanAccount(accID) &&
                getLoginController().findLoanByID(accID).getLoanType().equals("Short-Term")){
        isMortgage = false;
        CustomerLoanAccountTermController customerLoanCreditCardController = new CustomerLoanAccountTermController(
                getCurrentStage(),
                getLoginController(),
            (CustomerOpeningController) this.getMainPage(),
                isMortgage);

        customerLoanCreditCardController.showStage();
        }else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Cannot Enter Short-Term Account");
            a.setHeaderText("No Existing Short-Term Account");
            a.setContentText("Please create a new short-term account or click correct loan account type");
            a.show();
        }
    }

    @FXML
    void creditCardClicked(ActionEvent event) {
        //get account ID
        String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());
        String checkAccID = String.format("%s_c", getLoginController().getCurrentUser().getSSN());

        //Check if user has a valid loan already
        if(getLoginController().hasValidLoanAccount(accID)){
            if(getLoginController().findLoanByID(accID).getLoanType().equals("Credit")) {
                CustomerLoanCreditCardController customerLoanCreditCardController = new CustomerLoanCreditCardController(
                        getCurrentStage(),
                        getLoginController(),
                        (CustomerOpeningController) this.getMainPage());

                customerLoanCreditCardController.showStage();
            }else{
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Unable to proceed.");
                a.setHeaderText("Account Mismatch");
                a.setContentText("Incorrect account type found. Please select correct account.");
                a.show();
            }
        }else{
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Unable to proceed.");
            a.setHeaderText("Account not found");
            a.setContentText("Unable to locate account.");
            a.show();
        }
    }

    public CustomerLoanAccountController(Stage currentStage, LoginController loginController, CustomerOpeningController customerOpeningController) {
        super(currentStage, loginController, customerOpeningController);
        this.setCurrentViewFile("customer-loan-account.fxml");
        this.setCurrentViewTitle("Loan Account");
        this.setNewScene(this, this.getCurrentViewFile(), this.getCurrentViewTitle());
    }

    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + this.getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
