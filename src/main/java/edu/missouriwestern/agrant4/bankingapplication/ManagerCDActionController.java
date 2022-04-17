package edu.missouriwestern.agrant4.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ManagerCDActionController extends Controller {

    @FXML
    private Button createCDButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Button sendNoticeButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void createCDClicked(ActionEvent event) {
        ManagerCreateCdController createCdController = new ManagerCreateCdController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        createCdController.showStage();
    }

    @FXML
    void sendNoticeClicked(ActionEvent event) {

        ManagerRolloverCdController rolloverCdController = new ManagerRolloverCdController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        rolloverCdController.showStage();
    }

    public ManagerCDActionController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-cd-action.fxml");
        setCurrentViewTitle("Manage CDs");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
        this.mainTextBox.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!"
            + "\n\nPlease select what CD action you wish to do.");
    }

}
