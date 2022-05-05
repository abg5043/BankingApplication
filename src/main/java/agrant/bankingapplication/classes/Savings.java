package agrant.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class for savings accounts
 */
public class Savings {
    @CsvBindByName(column = "account_id")
    private String accountId;

    @CsvBindByName(column = "account_balance")
    private double accountBalance;

    @CsvBindByName(column = "interest_rate")
    private double interestRate;

    @CsvBindByName(column = "account_open_date")
    private String accountOpenDate;

    @CsvBindByName(column = "due_date") private String dueDate;

    /**
     * Constructor for savings accounts
     *
     * @param accountId - account ID
     * @param accountBalance - balance of account
     * @param interestRate - interest rate for account (not decimal)
     * @param accountOpenDate - date account was open
     * @param dueDate - due date if this is a CD ("n/a" if not)
     */
    public Savings(String accountId, double accountBalance, double interestRate, String accountOpenDate, String dueDate){
        this.accountId = accountId;
        this.accountBalance = accountBalance;
        this.interestRate = interestRate;
        this.accountOpenDate = accountOpenDate;
        this.dueDate = dueDate;
    }

    //blank constructor for OpenCSV
    public Savings(){
    }

    //___________Getters___________
    public String getAccountId() {
        return accountId;
    }
    public double getAccountBalance() {
        return accountBalance;
    }
    public double getInterestRate() {
        return interestRate;
    }
    public String getAccountOpenDate() {
        return accountOpenDate;
    }
    public String getDueDate() {
        return dueDate;
    }
    public Boolean isCD() {return !dueDate.equals("n/a"); }

    //___________Setters___________
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public void setAccountOpenDate(String accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * method for depositing cash
     *
     * @param cashAmount - amount cash
     */
    public void deposit(double cashAmount) {
        this.accountBalance += cashAmount;
    }

    /**
     * method for withdrawing money
     *
     * @param cashAmount - amount to withdraw
     * @return - true or false depending on if this is successful
     */
    public Boolean withdraw(double cashAmount) {
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        //checks if this is a CD and that the due date hasn't arrived
        if(isCD() && LocalDate.parse(this.dueDate, newFormatter).isAfter(LocalDate.now())) {
            //you accrue a 20% penalty for early withdrawal
            cashAmount *= 1.2;
            cashAmount = Math.round(cashAmount*100.0)/100.0;
        }

        if(this.accountBalance >= cashAmount) {
            this.accountBalance -= cashAmount;
            return true;
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Not enough money.");
            a.setHeaderText("Withdraw not processed.");
            a.setContentText("Not enough money in savings account.");

            // show the dialog
            a.show();

            return false;
        }
    }

    @Override
    public String toString() {
    return "account ID='" + accountId + '\'' +
        ", account balance='" + accountBalance + '\'' +
        ", interest rate='" + interestRate + '\'' +
        ", account open date='" + accountOpenDate + '\'' +
        ", due date='" + dueDate;
  }
}
