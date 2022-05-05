package agrant.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

/**
 * A class that represents a check
 */
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

    @CsvBindByName(column = "memo")
    private String memo;


    /**
     * Check constructor
     *
     * @param originAccountID - where money is coming from
     * @param amountCash - amount of money check is for
     * @param checkNumber - check number
     * @param date - date of check
     * @param destinationAccountID - where money is going
     * @param memo - what check is for
     */
    public Checks(String originAccountID, double amountCash, String checkNumber, String date, String destinationAccountID, String memo){
        this.originAccountID = originAccountID;
        this.amountCash = amountCash;
        this.checkNumber = checkNumber;
        this.date = date;
        this.destinationAccountID = destinationAccountID;
        this.memo = memo;
    }

    //blank constructor for OpenCSV
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
    public String getMemo() {return memo; }
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
    public void setMemo(String memo) {this.memo = memo;}
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
            ", memo='" + memo + '\'' +
            '}';
    }
}


