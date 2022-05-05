package agrant.bankingapplication;

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
 * Controller for screen where customer can purchase goods with their credit card
 */
public class CustomerCCPurchaseController extends Controller {

    @FXML
    private Button makePurchaseButton;

    @FXML
    private TextField memoField;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField moneyField;

    /**
     * Button that allows customer to make a purchase
     */
    @FXML
    void makePurchaseClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        //check formatting
        if(!memoField.getText().isEmpty() && !moneyField.getText().isEmpty()) {
            try {
                //check numeric fields are numbers
                double paidAmt = Double.parseDouble(moneyField.getText());
                String memo = memoField.getText();
                String accID = String.format("%s_l", getLoginController().getCurrentUser().getSSN());

                //this formats the money amount into currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String formattedPaidAmt = formatter.format(paidAmt);

                //Check that purchase doesn't exceed limit
                if(getLoginController().findLoanByID(accID).makeCCPurchase(paidAmt)){

                    //Create transaction object
                    Transactions newTrans1 = new Transactions(
                            accID,
                            "purchase",
                            "Paid " + paidAmt + " from credit account. Memo: " + memo,
                            currentDate
                    );

                    //add transactions to log
                    getLoginController().getTransactionLog().add(newTrans1);

                    //write the data
                    getLoginController().writeBankData();

                    // create a confirmation screen
                    ConfirmationController confirmationController = new ConfirmationController(
                            getCurrentStage(),
                            getLoginController(),
                            getMainPage(),
                            "Congratulations, you paid " + formattedPaidAmt +
                                    " from account number " + accID + " with memo: " +
                                    memo + "."
                    );

                    confirmationController.showStage();
                }else{
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Payment Failed");
                    a.setHeaderText("Amount exceeds limit");
                    a.setContentText("You cannot spend more than your credit limit.");

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
        }else {
        // create an alert
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Unable to Make Purchase");
        a.setHeaderText("Null Fields");
        a.setContentText("Please enter memo and amount paid.");

        // show the dialog
        a.show();
        }
    }

    //constructor
    public CustomerCCPurchaseController(
            Stage currentStage,
            LoginController loginController,
            CustomerOpeningController customerOpeningController
    ) {
        super(currentStage, loginController, customerOpeningController);
        setCurrentViewFile("customer-cc-purchase.fxml");
        setCurrentViewTitle("Make Credit Card Purchase");
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
