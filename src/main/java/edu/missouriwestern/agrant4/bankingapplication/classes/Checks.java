package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

public class Checks {
    @CsvBindByName(column = "account_id")
    private String accountID;

    @CsvBindByName(column = "amount_cash")
    private double amountCash;

    @CsvBindByName(column = "withdraw_or_deposit")
    private String withdrawOrDeposit;

    @CsvBindByName(column = "check_number")
    private String checkNumber;

    @CsvBindByName(column = "date")
    private String date;

    public Checks(String accountID, double amountCash, String withdrawOrDeposit, String checkNumber, String date){
        this.accountID = accountID;
        this.amountCash = amountCash;
        this.withdrawOrDeposit = withdrawOrDeposit;
        this.checkNumber = checkNumber;
        this.date = date;
    }

    public Checks(){}

    //______________Getters_____________
    public String getAccountID() {
        return accountID;
    }
    public String getCheckNumber() {
        return checkNumber;
    }
    public String getWithdrawOrDeposit() {
        return withdrawOrDeposit;
    }
    public String getDate() {
        return date;
    }

    public double getAmountCash() {
        return amountCash;
    }

    //_____________Setters_______________
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
    public void setCreditOrDebit(String withdrawOrDeposit) {
        this.withdrawOrDeposit = withdrawOrDeposit;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setWithdrawOrDeposit(String withdrawOrDeposit) {
        this.withdrawOrDeposit = withdrawOrDeposit;
    }

    public void setAmountCash(double amountCash) {
        this.amountCash = amountCash;
    }

    @Override
    public String toString() {
        return "Checks{" +
            "accountID='" + accountID + '\'' +
            ", amountCash=" + amountCash +
            ", withdrawOrDeposit='" + withdrawOrDeposit + '\'' +
            ", checkNumber='" + checkNumber + '\'' +
            ", date='" + date + '\'' +
            '}';
    }
}


