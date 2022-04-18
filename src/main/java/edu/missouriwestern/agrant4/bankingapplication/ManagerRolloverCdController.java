package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ManagerRolloverCdController extends Controller {

    @FXML
    private TextField accountNumber;

    @FXML
    private TableColumn<?, ?> accountNumberCol;

    @FXML
    private TableView<?> creditData;

    @FXML
    private TableColumn<?, ?> firstNameCol;

    @FXML
    private Button sendNoticeButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void sendNoticeClicked(ActionEvent event) {
        String billedAcc = accountNumber.getText();

        //Check that the text is not blank and matches an account
        //TODO: ADD IN THE LATTER LOGIC
        if(billedAcc.length() == 9) {
            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                //TODO: PUT IN NAME INSTEAD OF ACCOUNT NUMBER
                "Congratulations! Rollover notice sent to account " + billedAcc + "."
            );

            confirmationController.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Notice not sent");
            a.setHeaderText("Rollover notice not sent");
            a.setContentText("Invalid account number. Please try again.");

            // show the dialog
            a.show();
        }
    }

    public ManagerRolloverCdController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-rollover-cd.fxml");
        setCurrentViewTitle("Send Rollover Notices");
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
