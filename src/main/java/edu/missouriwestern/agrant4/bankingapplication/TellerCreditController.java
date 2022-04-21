package edu.missouriwestern.agrant4.bankingapplication;

import edu.missouriwestern.agrant4.bankingapplication.ConfirmationController;
import edu.missouriwestern.agrant4.bankingapplication.Controller;
import edu.missouriwestern.agrant4.bankingapplication.classes.Savings;
import edu.missouriwestern.agrant4.bankingapplication.classes.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TellerCreditController extends Controller {

    @FXML
    private TextField accountField;

    @FXML
    private Button creditButton;

    @FXML
    private Button debitButton;

    @FXML
    private TextField moneyField;

    @FXML
    private Label welcomeLabel;

    @FXML
    void creditClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        try {
            double incomingMoney = Double.parseDouble(moneyField.getText());
            String accID = accountField.getText();

            //this formats the money amount into currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String formattedIncomingMoney = formatter.format(incomingMoney);


            //Checks if this is a valid savings or checking account
            if(
                accID.length() == 11 &&
                (getLoginController().hasValidCheckingAccount(accID) ||
                    getLoginController().hasValidSavingsAccount(accID))
            ) {

                String typeOfAccount = accID.substring(9, 11);

                if(typeOfAccount.equals("_c")) {
                    //is going to checking account

                    //deposit the money into the appropriate account
                    getLoginController().findCheckingByID(accID).deposit(incomingMoney);

                } else {
                    //is going to savings account

                    //deposit the money into the appropriate account
                    getLoginController().findSavingsByID(accID).deposit(incomingMoney);
                }

                //Create transaction object
                Transactions newTrans = new Transactions(
                    accID,
                    "deposit",
                    "Deposited " + formattedIncomingMoney + " into account.",
                    currentDate
                );

                //add transaction to log
                getLoginController().getTransactionLog().add(newTrans);

                //write the data
                getLoginController().writeBankData();


                // create a confirmation screen
                ConfirmationController confirmationController = new ConfirmationController(
                    getCurrentStage(),
                    getLoginController(),
                    getMainPage(),
                    "Congratulations, you credited " + formattedIncomingMoney +
                        " to account number " + accID + "."
                );

                confirmationController.showStage();
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Money Not Credited");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }

        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Money Not Credited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    @FXML
    void debitClicked(ActionEvent event) {
        //get current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatters);

        try {
            double outgoingMoney = Double.parseDouble(moneyField.getText());
            String accID = accountField.getText();

            //this formats the money amount into currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String formattedOutgoingMoney = formatter.format(outgoingMoney);

            //Checks if this is a valid savings or checking account
            if(
                accID.length() == 11 &&
                    (getLoginController().hasValidCheckingAccount(accID) ||
                        getLoginController().hasValidSavingsAccount(accID) ||
                        getLoginController().hasValidCDAccount(accID)
                    )
            ) {

                String typeOfAccount = accID.substring(9, 11);

                if(typeOfAccount.equals("_c")) {
                    //is coming from checking account

                    /*
                     * Withdraw money if there is money in account;
                     * returns true or false depending on if there is money in the account
                     */
                    if (getLoginController().findCheckingByID(accID).withdraw(outgoingMoney)) {
                        //transaction is done.
                        //Create transaction object
                        Transactions newTrans = new Transactions(
                            accID,
                            "withdraw",
                            "Withdrew " + formattedOutgoingMoney + " from account.",
                            currentDate
                        );

                        //add transaction to log
                        getLoginController().getTransactionLog().add(newTrans);

                        //write the data
                        getLoginController().writeBankData();


                        // create a confirmation screen
                        ConfirmationController confirmationController = new ConfirmationController(
                            getCurrentStage(),
                            getLoginController(),
                            getMainPage(),
                            "Congratulations, you credited " + formattedOutgoingMoney +
                                " to account number " + accID + ".");

                        confirmationController.showStage();
                    }

                } else {
                    //is coming from savings account

                    //make pointer to savings account
                    Savings targetedAccounted;
                    Boolean isCD;
                    if (getLoginController().findSavingsByID(accID) == null) {
                        //this is a CD
                        isCD = true;
                        targetedAccounted = getLoginController().findCDByID(accID);
                    } else {
                        //this is not a CD
                        isCD = false;
                        targetedAccounted = getLoginController().findSavingsByID(accID);
                    }

                    if(isCD) {
                        //check if early withdrawal

                        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                        LocalDate dueDateLocalDate = LocalDate.parse(targetedAccounted.getDueDate(), newFormatter);

                        if(dueDateLocalDate.isAfter(LocalDate.now())) {
                            //due date hasn't arrived. accrue 20% penalty
                            //withdraw if there is money in the account. Apply 20% penalty since this is early withdrawal

                            formattedOutgoingMoney = formatter.format(outgoingMoney * 1.2);

                            withdrawFromSavings(
                                currentDate,
                                outgoingMoney * 1.2,
                                accID,
                                formattedOutgoingMoney,
                                targetedAccounted,
                                "\n\nThis includes a penalty of 20% for early withdrawal."
                            );

                        } else {
                            //CD and due date has passed. Withdraw with no penalty
                            withdrawFromSavings(
                                currentDate,
                                outgoingMoney,
                                accID,
                                formattedOutgoingMoney,
                                targetedAccounted,
                                ""
                            );
                        }

                    } else {
                        //Normal savings account. Withdraw normally.
                        withdrawFromSavings(
                            currentDate,
                            outgoingMoney,
                            accID,
                            formattedOutgoingMoney,
                            targetedAccounted,
                            "");
                    }
                }
            } else {
                // create an alert
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Money Not Debited");
                a.setHeaderText("Invalid formatting");
                a.setContentText("Please ensure you follow the suggested formatting.");

                // show the dialog
                a.show();
            }
        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Money Not Debited");
            a.setHeaderText("Invalid formatting");
            a.setContentText("Please ensure you use numbers in numeric fields.");

            // show the dialog
            a.show();
        }
    }

    private void withdrawFromSavings(
        String currentDate,
        double outgoingMoney,
        String accID,
        String formattedOutgoingMoney,
        Savings targetedAccounted,
        String appendedMessage
    ) {
        //checks if there is money. If there is, it withdraws and returns true
        if (targetedAccounted.withdraw(outgoingMoney)) {
            //transaction is done.
            //Create transaction object
            Transactions newTrans = new Transactions(
                accID,
                "withdraw",
                "Withdrew " + formattedOutgoingMoney + " from account.",
                currentDate
            );

            //add transaction to log
            getLoginController().getTransactionLog().add(newTrans);

            //write the data
            getLoginController().writeBankData();


            // create a confirmation screen
            ConfirmationController confirmationController = new ConfirmationController(
                getCurrentStage(),
                getLoginController(),
                getMainPage(),
                "Congratulations, you credited " + formattedOutgoingMoney +
                    " to account number " + accID + ". " + appendedMessage
            );

            confirmationController.showStage();
        }
    }


    public TellerCreditController(
        Stage currentStage,
        LoginController loginController,
        TellerOpeningController tellerOpeningController
    ) {
        super(currentStage, loginController, tellerOpeningController);
        setCurrentViewFile("teller-credit.fxml");
        setCurrentViewTitle("Credit/Debit Money");
        setNewScene(this, getCurrentViewFile(), getCurrentViewTitle());
    }

    /**
     * The initialize() method allows you set setup your scene, adding actions, configuring nodes, etc.
     */
    @FXML
    private void initialize() {
        this.welcomeLabel.setText("Hello, " + getLoginController().getCurrentUser().getFirstName() + "!");
    }

}
