package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;
import javafx.scene.control.Alert;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public Savings(String accountId, double account_balance, double interestRate, String accountOpenDate, String dueDate){
        this.accountId = accountId;
        this.accountBalance = account_balance;
        this.interestRate = interestRate;
        this.accountOpenDate = accountOpenDate;
        this.dueDate = dueDate;
    }
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

    public void oneTimeDeposit(double cashAmount) {
        this.accountBalance += cashAmount;
    }

    public Boolean oneTimeWithdraw(double cashAmount) {
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        //checks if this is a CD and that the due date hasn't arrived
        if(isCD() && LocalDate.parse(this.dueDate, newFormatter).isAfter(LocalDate.now())) {
            //you accrue a 20% penalty for early withdrawal
            cashAmount *= 1.2;
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
