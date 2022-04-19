package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.Loans;
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

public class ManagerCreateUserController extends Controller {

    @FXML
    private TextField address;

    @FXML
    private TextField city;

    @FXML
    private Button createUserButton;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField password;

    @FXML
    private TextField role;

    @FXML
    private TextField ssn;

    @FXML
    private TextField state;

    @FXML
    private TextField username;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField zip;

    @FXML
    void createUserClicked(ActionEvent event) {
        String ssn = this.ssn.getText();
        String zip = this.zip.getText();
        String username = this.username.getText();
        String address = this.address.getText();
        String state = this.state.getText();
        String city = this.city.getText();
        String fName = this.fName.getText();
        String lName = this.lName.getText();
        String role = this.role.getText();
        String password = this.password.getText();

        //Check that the text is not blank and matches the expected formatting
        if(
            ssn.length() == 9 &&
            zip.length() == 5 &&
            username.length() > 0 &&
            address.length() > 0 &&
            state.length() == 2 &&
            city.length() > 0 &&
            fName.length() > 0 &&
            lName.length() > 0 &&
            (role.equals("c") || role.equals("m") || role.equals("t")) &&
            password.length() > 0
        ) {
            if(!getLoginController().isValidUser(ssn)) {
                Boolean manager = role.equals("m");
                Boolean teller = role.equals("t");
                Boolean customer = role.equals("c");

                //Create user
                User newUser = new User(
                    username,
                    password,
                    ssn,
                    address,
                    city,
                    state,
                    zip,
                    manager,
                    customer,
                    teller,
                    fName,
                    lName
                );

                //update user data
                getLoginController().getUsersData().add(newUser);

                //write the data
                getLoginController().writeBankData();

                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations! You created a new user!"
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("User note created");
                a.setHeaderText("User already exists");
                a.setContentText("Please check SSN and try again.");

                // show the dialog
                a.show();
            }

        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Incorrect data entry");
            a.setHeaderText("User not created");
            a.setContentText("Please check formatting of data and try again.");

            // show the dialog
            a.show();
        }
    }

    public ManagerCreateUserController (
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-create-user.fxml");
        setCurrentViewTitle("Create User");
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
