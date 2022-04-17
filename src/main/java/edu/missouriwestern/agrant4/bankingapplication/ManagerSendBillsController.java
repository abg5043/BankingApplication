package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.classes.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ManagerSendBillsController extends Controller {

  @FXML
  private TextField accountNumber;

  @FXML
  private TableView<User> creditData;

  @FXML
  private Button exitButton;

  @FXML
  private Button mainPageButton;

  @FXML
  private Button sendBillButton;

  @FXML
  private Label welcomeLabel;


  @FXML
  private TableColumn<User, String> accountNumberCol;

  @FXML
  private TableColumn<User, String> firstNameCol;

  @FXML
  void sendBillClicked(ActionEvent event) {
    String billedAcc = accountNumber.getText();

    //Check that the text is not blank and matches an account
    //TODO: ADD IN THE LATTER LOGIC
    if(!billedAcc.equals("")) {
      // create a confirmation screen
      ConfirmationController confirmationController = new ConfirmationController(
          getCurrentStage(),
          getLoginController(),
          getMainPage(),
          //TODO: PUT IN NAME INSTEAD OF ACCOUNT NUMBER
          "Congratulations! Bills sent to " + billedAcc + "."
      );

      confirmationController.showStage();
    } else {
      // create an alert
      Alert a = new Alert(Alert.AlertType.WARNING);
      a.setTitle("Bills not sent");
      a.setHeaderText("Bills not sent");
      a.setContentText("Invalid account number. Please try again.");

      // show the dialog
      a.show();
    }

  }


  public ManagerSendBillsController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
    super(currentStage, loginController, managerOpeningController);
    setCurrentViewFile("manager-send-bills.fxml");
    setCurrentViewTitle("Send Credit Card Bills");
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
