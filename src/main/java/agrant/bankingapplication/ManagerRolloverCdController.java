package agrant.bankingapplication;

import agrant.bankingapplication.Controller;
import agrant.bankingapplication.classes.Savings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ManagerRolloverCdController extends Controller {

    @FXML
    private TextField accountNumber;

    @FXML
    private TableColumn<Savings, String> accountNumberCol;

    @FXML
    private TableColumn<Savings, Double> balanceCol;

    @FXML
    private TableView<Savings> cdData;

    @FXML
    private TableColumn<Savings, String> dueDateCol;

    @FXML
    private TableColumn<Savings, Double> interestRateCol;

    @FXML
    private TableColumn<Savings, String> openDateCol;

    @FXML
    private Button sendNoticeButton;

    @FXML
    private Label welcomeLabel;

    private final ArrayList<Savings> cdList;

    @FXML
    void sendNoticeClicked(ActionEvent event) {
        String billedAcc = accountNumber.getText();

        //Check that the text is not blank and matches an account
        if(billedAcc.length() == 11 && getLoginController().hasValidCDAccount(billedAcc)) {

            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Congratulations! Rollover notice sent to account " + billedAcc + "."
            );

            confirmationController.showStage();
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Notice not sent");
            a.setHeaderText("Rollover notice not sent");
            a.setContentText("Invalid account number. Please try again.");

            // show the dialog
            a.show();
        }
    }

    public ManagerRolloverCdController(Stage currentStage, LoginController loginController, ManagerOpeningController managerOpeningController) {
        super(currentStage, loginController, managerOpeningController);
        setCurrentViewFile("manager-rollover-cd.fxml");
        setCurrentViewTitle("Send Rollover Notices");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());

        //get only cds
        this.cdList = new ArrayList<>();
        for(Savings savings : getLoginController().getSavingsData()) {
            if (!savings.getDueDate().equals("n/a")) {
                cdList.add(savings);
            }
        }
        accountNumberCol.setCellValueFactory(new PropertyValueFactory<Savings, String>("accountId"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<Savings, Double>("accountBalance"));
        interestRateCol.setCellValueFactory(new PropertyValueFactory<Savings, Double>("interestRate"));
        openDateCol.setCellValueFactory(new PropertyValueFactory<Savings, String>("accountOpenDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<Savings, String>("dueDate"));

        //bind list into the table
        cdData.setItems(FXCollections.observableArrayList(cdList));
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }

}
