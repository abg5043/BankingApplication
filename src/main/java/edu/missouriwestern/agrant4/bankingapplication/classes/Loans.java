package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

public class Loans {
    @CsvBindByName(column = "account_id")
    private String accountId;

    @CsvBindByName(column = "current_balance")
    private double currentBalance;

    @CsvBindByName(column = "interest_rate")
    private double interestRate;

    @CsvBindByName(column = "next_payment_due_date")
    private String nextPaymentDueDate;

    @CsvBindByName(column = "date_bill_sent")
    private String dateBillSent;

    @CsvBindByName(column = "current_payment_amount")
    private double currentPaymentAmount;

    @CsvBindByName(column = "last_payment_made")
    private String lastPaymentMade;

    @CsvBindByName(column = "missed_payment_flag")
    private int missedPaymentFlag;

    @CsvBindByName(column = "loan_type")
    private String loanType;

    @CsvBindByName(column = "credit_limit")
    private int creditLimit;

    @CsvBindByName(column = "months_left")
    private int monthsLeft;

    public Loans(String ID, int currentBalance, double interestRate, String nextPaymentDueDate, String dateDue, double dateNotifiedOfPayment, String currentPaymentDue, String lastPaymentMade, int missedPaymentFlag, String loanType, int creditLimit, int monthsLeft){
        this.accountId = ID;
        this.currentBalance = currentBalance;
        this.interestRate = interestRate;
        this.nextPaymentDueDate = nextPaymentDueDate;
        this.currentPaymentAmount = dateNotifiedOfPayment;
        this.dateBillSent = currentPaymentDue;
        this.lastPaymentMade = lastPaymentMade;
        this.missedPaymentFlag = missedPaymentFlag;
        this.loanType = loanType;
        this.creditLimit = creditLimit;
        this.monthsLeft = monthsLeft;
    }
    public Loans(){}

    //_________________Getters_______________
    public int getCreditLimit() {
        return creditLimit;
    }
    public String getNextPaymentDueDate() {
        return nextPaymentDueDate;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public String getDateBillSent() {
        return dateBillSent;
    }  
    public double getCurrentPaymentAmount() {
        return currentPaymentAmount;
    }
    public double getInterestRate() {
        return interestRate;
    }
    public String getLastPaymentMade() {
        return lastPaymentMade;
    }
    public String getLoanType() {
        return loanType;
    }
    public int getMissedPaymentFlag() {
        return missedPaymentFlag;
    }
    public int getMonthsLeft() {
        return monthsLeft;
    }
    public String getAccountId() {
        return accountId;
    }
    
    //________________Setters________________
    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }
    public void setNextPaymentDueDate(String nextPaymentDueDate) {
        this.nextPaymentDueDate = nextPaymentDueDate;
    }
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
    public void setDateBillSent(String dateBillSent) {
        this.dateBillSent = dateBillSent;
    }
    public void setCurrentPaymentAmount(double currentPaymentAmount) {
        this.currentPaymentAmount = currentPaymentAmount;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public void setLastPaymentMade(String lastPaymentMade) {
        this.lastPaymentMade = lastPaymentMade;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    public void setMissedPaymentFlag(int missedPaymentFlag) {
        this.missedPaymentFlag = missedPaymentFlag;
    }
    public void setMonthsLeft(int monthsLeft) {
        this.monthsLeft = monthsLeft;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
    return "Loans{" +
        "ID='" + accountId + '\'' +
        ", currentBalance='" + currentBalance + '\'' +
        ", interestRate='" + interestRate + '\'' +
        ", nextPaymentDueDate='" + nextPaymentDueDate + '\'' +
        ", dateNotifiedOfPayment='" + currentPaymentAmount + '\'' +
        ", currentPaymentDue='" + dateBillSent + '\'' +
        ", lastPaymentMade=" + lastPaymentMade +
        ", missedPaymentFlag=" + missedPaymentFlag +
        ", loanType='" + loanType + '\'' +
        ", creditLimit='" + creditLimit + '\'' +
        ", monthsLeft='" + monthsLeft + '\'' +
        '}';
  }
}
