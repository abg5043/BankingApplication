package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TellerAccountInfoDisplayController extends Controller {

    @FXML
    private TableView<?> accountInfo;

    @FXML
    private TableColumn<?, ?> accountNumberCol;

    @FXML
    private TableColumn<?, ?> accountNumberCol1;

    @FXML
    private TableColumn<?, ?> firstNameCol;

    @FXML
    private TableColumn<?, ?> firstNameCol1;

    @FXML
    private TableView<?> transactionData;

    @FXML
    private Label welcomeLabel;


    public TellerAccountInfoDisplayController (
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-accountinfo-display.fxml");
        setCurrentViewTitle("Get account info");
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
