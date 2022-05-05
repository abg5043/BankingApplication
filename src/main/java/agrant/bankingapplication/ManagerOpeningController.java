package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller for the opening menu for the manager
 */
public class ManagerOpeningController extends Controller {

  @FXML
  private Button billsButton;

  @FXML
  private Button userButton;

  @FXML
  private Button loanButton;

  @FXML
  private TextArea mainTextBox;

  @FXML
  private Button manageCDButton;

  @FXML
  private Button checkingClicked;

  @FXML
  private Button tellerButton;

  @FXML
  private Label welcomeLabel;

  /**
   * Button that takes manager to send bill screen
   */
  @FXML
  void billsClicked(ActionEvent event) {

    ManagerSendBillsController managerSendBillsController = new ManagerSendBillsController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    // Show the new stage/window
    managerSendBillsController.showStage();

  }

  /**
   * Button that takes manager to screen where they can create a user
   */
  @FXML
  void userClicked(ActionEvent event) {

    ManagerCreateUserController managerCreateUserController = new ManagerCreateUserController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    // Show the new stage/window
    managerCreateUserController.showStage();

  }

  /**
   * Button that takes manager to screen where they can manage loans
   */
  @FXML
  void loanClicked(ActionEvent event) {
    ManagerLoansController managerLoansController = new ManagerLoansController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    managerLoansController.showStage();

  }

  /**
   * Button that takes manager to screen where they can manage CDs
   */
  @FXML
  void manageCDClicked(ActionEvent event) {
    ManagerCDActionController cdActionController = new ManagerCDActionController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    cdActionController.showStage();
  }

  /**
   * Button that takes manager to screen where they can manage checking accounts
   */
  @FXML
  void checkingClicked(ActionEvent event) {
    ManagerCheckingController managerCheckingController = new ManagerCheckingController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    // Show the new stage/window
    managerCheckingController.showStage();
  }

  /**
   * Button that takes manager to teller opening screen
   */
  @FXML
  void tellerClicked(ActionEvent event) {

    // Create teller object
    TellerOpeningController tellerOpeningController = new TellerOpeningController(
        getCurrentStage(),
        getLoginController()
    );

    // Show the new stage/window
    tellerOpeningController.showStage();

  }

  //constructor
  public ManagerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("manager-opening-view.fxml");
    setCurrentViewTitle("Manager");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!"
        + "\n\nWelcome to the Manager Main Screen. " +
        "\n\nPlease select your next action.");
  }

}
