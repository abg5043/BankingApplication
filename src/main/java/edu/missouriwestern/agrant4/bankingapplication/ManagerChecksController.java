package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.classes.Checks;
import edu.missouriwestern.agrant4.bankingapplication.classes.Loans;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ManagerChecksController extends Controller {

    @FXML
    private TableColumn<Checks, String> accountNumberCol;

    @FXML
    private TableColumn<Checks, Double> amountCashCol;

    @FXML
    private TableView<Checks> checkData;

    @FXML
    private TextField checkNumber;

    @FXML
    private TableColumn<Checks, String> checkNumberCol;

    @FXML
    private TableColumn<Checks, String> dateCol;

    @FXML
    private Button processButton;

    @FXML
    private Button stopButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableColumn<Checks, String> withdrawOrDepositCol;


    @FXML
    void processClicked(ActionEvent event) {

        if(getLoginController().getPendingChecks().size() != 0) {

            //TODO: IMPLEMENT LOGIC TO process checks. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Congratulations, you processed all the checks!"
            );

            confirmationController.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Checks to Process");
            a.setHeaderText("Check not processed");
            a.setContentText("No checks to process. Please try again.");

            // show the dialog
            a.show();
        }

    }

    @FXML
    void stopClicked(ActionEvent event) {
        String checkNumberText = checkNumber.getText();

        //Check that the text is not blank and matches a check
        //TODO: ADD IN THE LATTER LOGIC
        if(checkNumberText.length() != 0) {

          //TODO: Add in logic here

          // create a confirmation screen
          ConfirmationController confirmationController = new ConfirmationController(
              getCurrentStage(),
              getLoginController(),
              getMainPage(),
              //TODO: PUT IN NAME INSTEAD OF ACCOUNT NUMBER
              "Check number " + checkNumberText + " was stopped."
          );

            confirmationController.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Invalid Check Number");
            a.setHeaderText("Check not stopped");
            a.setContentText("Invalid check number. Please try again.");

            // show the dialog
            a.show();
        }

    }

    public ManagerChecksController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-checks.fxml");
        setCurrentViewTitle("Manage Checks");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());

    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");

        accountNumberCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("accountID"));
        amountCashCol.setCellValueFactory(new PropertyValueFactory<Checks, Double>("amountCash"));
        withdrawOrDepositCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("withdrawOrDeposit"));
        checkNumberCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("checkNumber"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Checks, String>("date"));

        //bind list into the table
        checkData.setItems(FXCollections.observableArrayList(getLoginController().getPendingChecks()));
    }

}
