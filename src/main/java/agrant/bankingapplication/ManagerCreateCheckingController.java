package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for screen where manager can create a checking account
 */
public class ManagerCreateCheckingController extends Controller {

    @FXML
    private TextField acctIDField;

    @FXML
    private Button checkingButton;

    @FXML
    private TextField startingBalanceField;

    @FXML
    private Label welcomeLabel;

    /**
     * Button that lets manager create a checking account
     */
    @FXML
    void checkingClicked(ActionEvent event) {
        String SSN = acctIDField.getText();

        try {
            //checks that starting balance was actually a number
            double balance = Double.parseDouble(startingBalanceField.getText());

            //get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String openDate = date.format(formatters);

            //check that the SSN text is not blank
            if(
                SSN.length() == 9
            ) {
                //starting balance determines account type
                String accType;
                if (balance >= 1000 ) {
                    accType = "Gold";
                } else {
                    accType = "Regular";
                }

                //check if the SSN matches a valid user
                if (getLoginController().isValidUser(SSN) ) {
                    //check if the user doesn't already have a checking account
                    if (!getLoginController().hasValidCheckingAccount(SSN + "_c")) {
                        //make checking object
                        Checking newChecking = new Checking(
                            SSN + "_c",
                            accType,
                            balance,
                            "n/a",
                            0,
                            openDate,
                            "n/a"
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
                        //user already has a checking account
                        // create an alert
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setTitle("Checking Not Created");
                        a.setHeaderText("User already has account");
                        a.setContentText("Sorry. Users are only allowed one account.");

                        // show the dialog
                        a.show();
                    }
                } else {
                    //SSN does not match a valid user
                    // create an alert
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Checking Not Created");
                    a.setHeaderText("Invalid SSN");
                    a.setContentText("Please ensure you enter a SSN for a valid user.");

                    // show the dialog
                    a.show();
                }

            } else {
                //Text is blank or does not matche an account type
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

    //Constructor
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
