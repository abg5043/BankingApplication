package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

public class Checks {
    @CsvBindByName(column = "origin_account_id")
    private String originAccountID;

    @CsvBindByName(column = "destination_account_id")
    private String destinationAccountID;

    @CsvBindByName(column = "amount_cash")
    private double amountCash;

    @CsvBindByName(column = "check_number")
    private String checkNumber;

    @CsvBindByName(column = "date")
    private String date;

    public Checks(String originAccountID, double amountCash, String checkNumber, String date, String destinationAccountID){
        this.originAccountID = originAccountID;
        this.amountCash = amountCash;
        this.checkNumber = checkNumber;
        this.date = date;
        this.destinationAccountID = destinationAccountID;
    }

    public Checks(){}

    //______________Getters_____________
    public String getOriginAccountID() {
        return originAccountID;
    }
    public String getCheckNumber() {
        return checkNumber;
    }
    public String getDate() {
        return date;
    }
    public String getDestinationAccountID() {
        return destinationAccountID;
    }
    public double getAmountCash() {
        return amountCash;
    }

    //_____________Setters_______________
    public void setOriginAccountID(String originAccountID) {
        this.originAccountID = originAccountID;
    }
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDestinationAccountID(String destinationAccountID) {
        this.destinationAccountID = destinationAccountID;
    }
    public void setAmountCash(double amountCash) {
        this.amountCash = amountCash;
    }

    @Override
    public String toString() {
        return "Checks{" +
            "originAccountID='" + originAccountID + '\'' +
            ", destinationAccountID='" + destinationAccountID + '\'' +
            ", amountCash=" + amountCash +
            ", checkNumber='" + checkNumber + '\'' +
            ", date='" + date + '\'' +
            '}';
    }
}


