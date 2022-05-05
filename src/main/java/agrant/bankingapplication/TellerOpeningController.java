package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller for the opening view for tellers
 */
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


  /**
   * Button that takes teller to account info screen
   */
  @FXML
  void accountClicked(ActionEvent event) {
    TellerAccountInfoStartController controller = new TellerAccountInfoStartController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    controller.showStage();
  }

  /**
   * Button that takes teller to the credit/debit screen
   */
  @FXML
  void creditAccountClicked(ActionEvent event) {
    TellerCreditController controller = new TellerCreditController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    controller.showStage();
  }

  /**
   * Button that takes teller to transfer money screen
   */
  @FXML
  void transferClicked(ActionEvent event) {
    TellerTransferController tellerTransferController = new TellerTransferController(
        getCurrentStage(),
        getLoginController(),
        this
    );

    tellerTransferController.showStage();
  }

  //Constructor for TellerOpeningController
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
