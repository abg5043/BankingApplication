package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ManagerLoansController extends Controller{

    @FXML
    private TextField accountNumber;

    @FXML
    private TableColumn<?, ?> accountNumberCol;

    @FXML
    private Button approveButton;

    @FXML
    private Button denyButton;

    @FXML
    private TableColumn<?, ?> firstNameCol;

    @FXML
    private TableView<?> loanData;

    @FXML
    private Label welcomeLabel;

    @FXML
    void approveClicked(ActionEvent event) {
        String loanAcc = accountNumber.getText();

        //Check that the text is not blank and matches an account
        //TODO: ADD IN THE LATTER LOGIC
        if(!loanAcc.equals("")) {
            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                //TODO: PUT IN NAME INSTEAD OF ACCOUNT NUMBER
                "Loan approved for account number " + loanAcc + "."
            );

            confirmationController.showStage();
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

    @FXML
    void denyClicked(ActionEvent event) {
        String loanAcc = accountNumber.getText();

        //Check that the text is not blank and matches an account
        //TODO: ADD IN THE LATTER LOGIC
        if(!loanAcc.equals("")) {
            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                //TODO: PUT IN NAME INSTEAD OF ACCOUNT NUMBER
                "Loan for account number " + loanAcc + " was not approved."
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
        //TODO: THIS IS CODE FOR FILLING IN THE TABLE. ADJUST WITH NEW DATA STRUCTURES
    /*
    accountNumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("SSN"));
    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
    //bind list into the table
    creditData.setItems(FXCollections.observableArrayList(getLoginController().getUsers()));
    */
    }

}
