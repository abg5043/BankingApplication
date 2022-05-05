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
    private final ArrayList<Transactions> prunedTransactionLog;


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

        this.prunedTransactionLog = pruneTransactionLog(getLoginController().getTransactionLog(), currentAccountID);

        accountNumberCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("accountID"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("transactionType"));
        memoCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("memo"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("date"));

        //bind list into the table
        transactionData.setItems(FXCollections.observableArrayList(this.prunedTransactionLog));

    }

    private ArrayList<Transactions> pruneTransactionLog(ArrayList<Transactions> transactionLog, String accID) {
        ArrayList<Transactions> tempList = new ArrayList<>();

        for(Transactions transaction : transactionLog) {
            if(transaction.getAccountID().equals(accID)) {
                tempList.add(transaction);
            }
        }

        return tempList;
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
