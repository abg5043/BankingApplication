package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ManagerChecksController extends Controller {

    @FXML
    private TableColumn<?, ?> accountNumberCol;

    @FXML
    private TableView<?> checkData;

    @FXML
    private TextField checkNumber;

    @FXML
    private TableColumn<?, ?> firstNameCol;

    @FXML
    private Button processButton;

    @FXML
    private Button stopButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void processClicked(ActionEvent event) {
        //TODO: IMPLEMENT LOGIC TO CREATE CD. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object

        // create a confirmation screen
        ConfirmationController confirmationController = new ConfirmationController(
            getCurrentStage(),
            getLoginController(),
            getMainPage(),
            "Congratulations, you processed all the checks!"
        );
    }

    @FXML
    void stopClicked(ActionEvent event) {
        String checkNumberText = checkNumber.getText();

        //Check that the text is not blank and matches a check
        //TODO: ADD IN THE LATTER LOGIC
        if(checkNumberText.length() == 9) {
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
        //TODO: THIS IS CODE FOR FILLING IN THE TABLE. ADJUST WITH NEW DATA STRUCTURES
    /*
    accountNumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("SSN"));
    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
    //bind list into the table
    creditData.setItems(FXCollections.observableArrayList(getLoginController().getUsers()));
    */
    }

}
