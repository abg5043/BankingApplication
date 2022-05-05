package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller for main checking/savings menu for manager
 */
public class ManagerCheckingController extends Controller {

    @FXML
    private Button checkingButton;

    @FXML
    private TextArea mainTextBox;

    @FXML
    private Button checksButton;

    @FXML
    private Button savingsButton;

    @FXML
    private Button linkAccountsButton;

    @FXML
    private Button setInterestButton;

    @FXML
    private Label welcomeLabel;

    /**
     * Button that brings manager to screen to create checks
     */
    @FXML
    void checksClicked(ActionEvent event) {
        ManagerChecksController controller = new ManagerChecksController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        controller.showStage();
    }

    /**
     * Button that brings manager to screen to create checking accounts
     */
    @FXML
    void checkingClicked(ActionEvent event) {
        ManagerCreateCheckingController managerCreateCheckingController = new ManagerCreateCheckingController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        managerCreateCheckingController.showStage();
    }

    /**
     * Button that brings manager to screen to link checking and savings accounts
     */
    @FXML
    void linkAccountsClicked(ActionEvent event) {
        ManagerLinkAccountsController managerLinkAccountsController = new ManagerLinkAccountsController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        managerLinkAccountsController.showStage();
    }

    /**
     * Button that brings manager to screen to create savings accounts
     */
    @FXML
    void savingsClicked(ActionEvent event) {
        ManagerCreateSavingsController managerCreateSavingsController = new ManagerCreateSavingsController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        managerCreateSavingsController.showStage();
    }

    /**
     * Button that brings manager to screen to set interest rates for accounts
     */
    @FXML
    void setInterestClicked(ActionEvent event) {
        ManagerInterestController managerInterestController = new ManagerInterestController(
            getCurrentStage(),
            getLoginController(),
            (ManagerOpeningController) getMainPage()
        );

        // Show the new stage/window
        managerInterestController.showStage();
    }

    //constructor
    public ManagerCheckingController(
        Stage currentStage,
        LoginController loginController,
        ManagerOpeningController managerOpeningController
    ) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-checking.fxml");
        setCurrentViewTitle("Manage Checking and Savings");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
        this.mainTextBox.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!"
            + "\n\nHere you can manage your checking and savings accounts." +
            "\n\nPlease select your next action.");
    }

}
