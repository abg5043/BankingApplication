package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.classes.Checking;
import edu.missouriwestern.agrant4.bankingapplication.classes.Loans;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TellerAccountInfoDisplayController extends Controller {

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private TableColumn<?, ?> accountNumberCol1;

    @FXML
    private TableColumn<?, ?> firstNameCol1;

    @FXML
    private TableView<?> transactionData;

    @FXML
    private Label welcomeLabel;

    private String currentAccountID;
    private String accountInfo;


    public TellerAccountInfoDisplayController (
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController,
        String currentAccountID
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-accountinfo-display.fxml");
        setCurrentViewTitle("Get account info");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
        this.currentAccountID = currentAccountID;
        setAccountInfoArea(currentAccountID);
    }

    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    /*
    accountNumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("SSN"));
    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
    //bind list into the table
    creditData.setItems(FXCollections.observableArrayList(getLoginController().getUsers()));
    */

    }

    private String findAccountInfo(String text) {
        String returnString = "";

        switch (text.substring(9,11)) {
            case "_l":
                for(Loans loans : getLoginController().getLoansData()) {
                    if(loans.getAccountId().equals(text)) {
                        returnString = loans.toString();
                        break;
                    }
                }
                break;
            case "_s":
                for(Savings savings : getLoginController().getSavingsData()) {
                    if(savings.getAccountId().equals(text)) {
                        returnString = savings.toString();
                        break;
                    }
                }
                break;
            case "_c":
                for(Checking checking : getLoginController().getCheckingData()) {
                    if(checking.getAccountId().equals(text)) {
                        returnString = checking.toString();
                        break;
                    }
                }
                break;
            default:
                returnString = "Error: No Account found";
                break;
        }
        return returnString;
    }
}
