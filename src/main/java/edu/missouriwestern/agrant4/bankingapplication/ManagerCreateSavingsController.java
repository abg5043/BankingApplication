package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerCreateSavingsController extends Controller {

    @FXML
    private TextField ssnField;

    @FXML
    private Button savingsButton;

    @FXML
    private TextField interestRateField;

    @FXML
    private TextField startingBalanceField;

    @FXML
    private Label welcomeLabel;


    @FXML
    void savingsClicked(ActionEvent event) {
        String customerSSN = ssnField.getText();

        try {
            double balance = Double.parseDouble(startingBalanceField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());

            //get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String openDate = date.format(formatters);

            //Check that the text is not blank and matches an account
            if( customerSSN.length() == 9) {
                if (getLoginController().isValidUser(customerSSN)) {
                    if(!getLoginController().hasValidSavingsAccount(customerSSN + "_s")) {
                        Savings newSavings = new Savings(
                            customerSSN + "_s",
                            balance,
                            interestRate,
                            openDate,
                            "n/a"
                        );

                        //update data and write to csv
                        getLoginController().getSavingsData().add(newSavings);
                        getLoginController().writeBankData();

                        // create a confirmation screen
                        ConfirmationController confirmationController = new ConfirmationController(
                            getCurrentStage(),
                            getLoginController(),
                            getMainPage(),
                            "Congratulations, you created a savings account!"
                        );

                        confirmationController.showStage();
                    } else {
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Savings Not Created");
                        a.setHeaderText("User already has account");
                        a.setContentText("Sorry. Users are only allowed one account.");

                        // show the dialog
                        a.show();
                    }

                } else {
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Savings Not Created");
                    a.setHeaderText("Invalid user SSN");
                    a.setContentText("Please ensure you enter in a valid SSN.");

                    // show the dialog
                    a.show();
                }


            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Savings Not Created");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Savings Not Created");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    public ManagerCreateSavingsController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-create-savings.fxml");
        setCurrentViewTitle("Create a Savings Account");
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
