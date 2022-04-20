package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;
import javafx.scene.control.Alert;

public class Checking {
    @CsvBindByName(column = "account_id")
    private String accountId;

    @CsvBindByName(column = "account_type")
    private String accountType;

    @CsvBindByName(column = "current_balance")
    private double currentBalance;

    @CsvBindByName(column = "backup_account_id")
    private String backupAccountId;

    //overdrafts this month
    @CsvBindByName(column = "overdrafts")
    private int overdrafts;

    @CsvBindByName(column = "open_date")
    private String openDate;

    public Checking(String accountId, String accountType, double currentBalance, String backupAccountId, int overDrafts, String openDate){
        this.accountId = accountId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.backupAccountId = backupAccountId;
        this.overdrafts = overDrafts;
        this.openDate = openDate;
    }

    public Checking(){
    }

    //_________Getters__________
    public String getAccountId() {
        return accountId;
    }
    public String getAccountType() {
        return accountType;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public String getBackupAccountId() {
        return backupAccountId;
    }
    public int getOverdrafts() {
        return overdrafts;
    }
    public String getOpenDate() {
        return openDate;
    }

    //________Setters___________
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
    public void setBackupAccountId(String backupAccountId) {
        this.backupAccountId = backupAccountId;
    }
    public void setOverdrafts(int overdrafts) {
        this.overdrafts = overdrafts;
    }
    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public void deposit(double cashAmount) {
        this.currentBalance += cashAmount;
    }

    public Boolean withdraw(double cashAmount) {
        if(this.currentBalance >= cashAmount) {
            this.currentBalance -= cashAmount;
            return true;
        } else {
            // create an alert
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Not enough money.");
            a.setHeaderText("Withdraw not processed.");
            a.setContentText("Not enough money in account.");

            // show the dialog
            a.show();
            return false;
        }
    }

    @Override
    public String toString() {
    return "account ID='" + accountId + '\'' +
        ", account type='" + accountType + '\'' +
        ", current balance='" + currentBalance + '\'' +
        ", backup account id='" + backupAccountId + '\'' +
        ", overdrafts='" + overdrafts + '\'' +
        ", open date='" + openDate;
  }
}   
