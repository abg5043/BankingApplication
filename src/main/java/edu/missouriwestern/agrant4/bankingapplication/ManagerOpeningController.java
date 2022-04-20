package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.Checks;
import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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


  @FXML
  void loanClicked(ActionEvent event) {
    ManagerLoansController managerLoansController = new ManagerLoansController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    managerLoansController.showStage();

  }

  @FXML
  void manageCDClicked(ActionEvent event) {
    ManagerCDActionController cdActionController = new ManagerCDActionController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    cdActionController.showStage();
  }

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
