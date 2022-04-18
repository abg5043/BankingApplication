package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TellerOpeningController extends Controller {

  @FXML
  private Button accountButton;

  @FXML
  private Button creditAccountButton;


  @FXML
  private TextArea mainTextBox;

  @FXML
  private Button transferButton;

  @FXML
  private Label welcomeLabel;


  @FXML
  void accountClicked(ActionEvent event) {
    TellerAccountInfoStartController controller = new TellerAccountInfoStartController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    controller.showStage();
  }

  @FXML
  void creditAccountClicked(ActionEvent event) {
    TellerCreditController controller = new TellerCreditController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    controller.showStage();
  }

  @FXML
  void transferClicked(ActionEvent event) {
    TellerTransferController tellerTransferController = new TellerTransferController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    tellerTransferController.showStage();
  }


  public TellerOpeningController(Stage currentStage, LoginController loginController) {
    super(currentStage, loginController, loginController);
    setCurrentViewFile("teller-opening-view.fxml");
    setCurrentViewTitle("Teller");
    setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
  }

  /**
   * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
   */
  @FXML
  private void initialize() {
    this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    this.mainTextBox.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!"
        + "\n\nPlease select your next action.");
  }
}
