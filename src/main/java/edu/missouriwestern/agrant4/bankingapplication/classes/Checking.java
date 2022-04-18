package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

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
    private int overDrafts;

    @CsvBindByName(column = "open_date")
    private String openDate;

    public Checking(String accountId, String accountType, double currentBalance, String backupAccountId, int overDrafts, String openDate){
        this.accountId = accountId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.backupAccountId = backupAccountId;
        this.overDrafts = overDrafts;
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
    public int getOverDrafts() {
        return overDrafts;
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
    public void setOverDrafts(int overDrafts) {
        this.overDrafts = overDrafts;
    }
    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    @Override
    public String toString() {
    return "Checking{" +
        "accountId='" + accountId + '\'' +
        ", accountType='" + accountType + '\'' +
        ", currentBalance='" + currentBalance + '\'' +
        ", backupAccountId='" + backupAccountId + '\'' +
        ", overDrafts='" + overDrafts + '\'' +
        ", openDate='" + openDate + '\'' +
        '}';
  }
}   
