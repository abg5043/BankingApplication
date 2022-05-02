package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
import agrant.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerMortgageController extends Controller {

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private Label amtOwedLabel;

    @FXML
    private Button makePaymentButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void makePaymentClicked(ActionEvent event) {

    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    private String findAccountInfo(String text) {
        String returnString = "Error: No Account found";

        for(Loans loans : getLoginController().getLoansData()) {
            if(loans.getAccountId().equals(text)) {
                returnString = loans.toString();
                break;
            }
        }
        return returnString;
    }

    public CustomerMortgageController(
        Stage currentStage,
        LoginController loginController,
        CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-mortgage.fxml");
        setCurrentViewTitle("Mortgage");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
        setAccountInfoArea(getLoginController().getCurrentUser().getSSN() + "_l");

    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }
}
