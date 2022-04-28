package agrant.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

public class Transactions {
    @CsvBindByName(column = "account_id")
    private String accountID;

    @CsvBindByName(column = "transaction_type")
    private String transactionType;

    @CsvBindByName(column = "memo")
    private String memo;

    @CsvBindByName(column = "date")
    private String date;

    public Transactions(String accountID, String transactionType, String memo, String date) {
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.memo = memo;
        this.date = date;
    }

    public Transactions() {
    }

    //______________Getters_____________
    public String getAccountID() {
        return accountID;
    }

    public String getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getMemo() {
        return memo;
    }

    //_____________Setters_______________
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "account ID='" + accountID + '\'' +
            ", transaction type='" + transactionType + '\'' +
            ", memo='" + memo + '\'' +
            ", date='" + date;
    }
}