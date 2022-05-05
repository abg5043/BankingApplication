package agrant.bankingapplication;

import agrant.bankingapplication.classes.Checking;
import agrant.bankingapplication.classes.Loans;
import agrant.bankingapplication.classes.Savings;
import agrant.bankingapplication.classes.Transactions;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Controller for teller's account info display screen
 */
public class TellerAccountInfoDisplayController extends Controller {

    @FXML
    private TextArea accountInfoArea;

    @FXML
    private TableColumn<Transactions, String> accountNumberCol;

    @FXML
    private TableColumn<Transactions, String> dateCol;

    @FXML
    private Button exitButton;

    @FXML
    private Button mainPageButton;

    @FXML
    private TableColumn<Transactions, String> memoCol;

    @FXML
    private TableView<Transactions> transactionData;

    @FXML
    private TableColumn<Transactions, String> transactionTypeCol;

    @FXML
    private Label welcomeLabel;

    private final String currentAccountID;
    private String accountInfo;
    //transaction ArrayList that only contains transactions for this account
    private final ArrayList<Transactions> prunedTransactionLog;

    //constructor for TellerAccountInfoDisplayController
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

        //get only transactions for this account
        this.prunedTransactionLog = pruneTransactionLog(getLoginController().getTransactionLog(), currentAccountID);

        //set table columns
        accountNumberCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("accountID"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("transactionType"));
        memoCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("memo"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("date"));

        //bind list into the table
        transactionData.setItems(FXCollections.observableArrayList(this.prunedTransactionLog));

    }

    /**
     * Method for extracting transactions relevant to this account
     * @param transactionLog - overall ArrayList of transactions
     * @param accID - this accountID
     * @return - a pruned ArrayList
     */
    private ArrayList<Transactions> pruneTransactionLog(ArrayList<Transactions> transactionLog, String accID) {
        ArrayList<Transactions> tempList = new ArrayList<>();

        //go through each transaction in original ArrayList
        for(Transactions transaction : transactionLog) {
            //check if the account ids match
            if(transaction.getAccountID().equals(accID)) {
                tempList.add(transaction);
            }
        }

        return tempList;
    }

    //setter
    private void setAccountInfoArea(String text) {
        this.accountInfoArea.setText(findAccountInfo(text));
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }

    /**
     * Method for getting entered account's toString
     *
     * @param accID - account ID entered by customer
     * @return - that account's toString
     */
    private String findAccountInfo(String accID) {
        String returnString = "";

        switch (accID.substring(9,11)) {
            case "_l":
                for(Loans loans : getLoginController().getLoansData()) {
                    if(loans.getAccountId().equals(accID)) {
                        returnString = loans.toString();
                        break;
                    }
                }
                break;
            case "_s":
                for(Savings savings : getLoginController().getSavingsData()) {
                    if(savings.getAccountId().equals(accID)) {
                        returnString = savings.toString();
                        break;
                    }
                }
                break;
            case "_c":
                for(Checking checking : getLoginController().getCheckingData()) {
                    if(checking.getAccountId().equals(accID)) {
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
