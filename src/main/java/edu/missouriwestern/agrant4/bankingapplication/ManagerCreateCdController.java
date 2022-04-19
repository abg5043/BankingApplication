package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManagerCreateCdController extends Controller {

    @FXML
    private TextField accBal;

    @FXML
    private Button createCDButton;

    @FXML
    private TextField currentInterest;

    @FXML
    private TextField custID;

    @FXML
    private TextField dueDate;

    @FXML
    private Label welcomeLabel;

    @FXML
    void createCDClicked(ActionEvent event) {
        String ID = custID.getText();

        try {
            double balance = Double.parseDouble(accBal.getText());
            int yearsUntilComplete = Integer.parseInt(dueDate.getText());
            double interest = Double.parseDouble(currentInterest.getText());

            //get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String currentDate = date.format(formatters);

            LocalDate dueDateObject = date.plusYears(yearsUntilComplete);
            String dueDateString = dueDateObject.format(formatters);

            //Check that the text is not blank and matches an account
            //TODO: ADD IN THE LATTER LOGIC
            if( ID.length() == 11) {
                //TODO: IMPLEMENT LOGIC TO CREATE CD. SHOULD BE SUPER EASY, but make sure to write to CSV *AND* object


                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you created a CD!"
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("CD Not Crated");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("CD Not Created");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }
    public ManagerCreateCdController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-create-cd.fxml");
        setCurrentViewTitle("Create a CD");
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
