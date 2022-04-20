package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.Checking;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerCreateCheckingController extends Controller {

    @FXML
    private TextField acctIDField;

    @FXML
    private TextField acctTypeField;

    @FXML
    private Button checkingButton;

    @FXML
    private TextField startingBalanceField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void checkingClicked(ActionEvent event) {
        String SSN = acctIDField.getText();
        String acctType = acctTypeField.getText();

        try {
            double balance = Double.parseDouble(startingBalanceField.getText());

            //get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String openDate = date.format(formatters);

            //Check that the text is not blank and matches an account
            if(
                SSN.length() == 9 &&
                (acctType.equals("Regular") || acctType.equals("Gold"))
            ) {
                if (getLoginController().isValidUser(SSN) ) {
                    if (!getLoginController().hasValidCheckingAccount(SSN + "_c")) {
                        Checking newChecking = new Checking(
                            SSN + "_c",
                            acctType,
                            balance,
                            "n/a",
                            0,
                            openDate
                        );

                        //update data and write to csv
                        getLoginController().getCheckingData().add(newChecking);
                        getLoginController().writeBankData();

                        // create a confirmation screen
                        ConfirmationController confirmationController = new ConfirmationController(
                            getCurrentStage(),
                            getLoginController(),
                            getMainPage(),
                            "Congratulations, you created a checking account!"
                        );

                        confirmationController.showStage();
                    } else {
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Checking Not Created");
                        a.setHeaderText("User already has account");
                        a.setContentText("Sorry. Users are only allowed one account.");

                        // show the dialog
                        a.show();
                    }
                } else {
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Checking Not Created");
                    a.setHeaderText("Invalid SSN");
                    a.setContentText("Please ensure you enter a SSN for a valid user.");

                    // show the dialog
                    a.show();
                }
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Checking Not Created");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }
        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Checking Not Created");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    public ManagerCreateCheckingController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-create-checking.fxml");
        setCurrentViewTitle("Create a Checking Account");
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
