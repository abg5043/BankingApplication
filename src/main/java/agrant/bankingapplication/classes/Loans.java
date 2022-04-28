package agrant.bankingapplication.classes;

import agrant.bankingapplication.ConfirmationController;
import com.opencsv.bean.CsvBindByName;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public Loans(String ID, double currentBalance, double interestRate, String nextPaymentDueDate, String dateDue, double dateNotifiedOfPayment, String currentPaymentDue, String lastPaymentMade, int missedPaymentFlag, String loanType, int creditLimit, int monthsLeft){
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

    public boolean makeLoanPayment(double payAmt){
        if(this.loanType.equals("Mortgage") || this.loanType.equals("Short-Term")) {
            //get current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String currentDate = date.format(formatters);
            //parse next payment due date to compare to current date
            LocalDate dueDate = LocalDate.parse(this.nextPaymentDueDate, formatters);
            String formattedDueDate = formatters.format(dueDate);

            if((this.currentBalance - payAmt) >= 0) {
                this.currentBalance -= payAmt;
                this.lastPaymentMade = formattedDueDate;
                if (this.monthsLeft > 0 && this.currentBalance == 0) {
                    this.monthsLeft--;
                }

                return true;
            }else{
                //Would result in overpayment
                return false;
            }
        }else{
            //Incorrect account type
            return false;
        }
    }

    public boolean makeCCPurchase(double payAmt){
        if(this.loanType.equals("Credit")){
            //Check that purchase doesn't exceed limit
            if((this.currentBalance + payAmt) <= this.creditLimit){
                this.currentBalance += payAmt;

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean ccPayInFull(double payAmt){
        if(this.loanType.equals("Credit")) {
            //get current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String currentDate = date.format(formatters);
            //parse next payment due date to compare to current date
            LocalDate dueDate = LocalDate.parse(this.nextPaymentDueDate, formatters);
            String formattedDueDate = formatters.format(dueDate);

            if((this.currentBalance - payAmt) == 0) {
                this.currentBalance = 0;

                return true;
            }else{
                //Would result in overpayment or underpayment
                return false;
            }
        }else{
            //called for non-credit account
            return false;
        }
    }

    public boolean ccOnetimePay(double payAmt){
        if(this.loanType.equals("Credit")) {
            //get current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String currentDate = date.format(formatters);
            //parse next payment due date to compare to current date
            LocalDate dueDate = LocalDate.parse(this.nextPaymentDueDate, formatters);
            String formattedDueDate = formatters.format(dueDate);

            if((this.currentBalance - payAmt) >= 0) {
                this.currentBalance -= payAmt;
                this.lastPaymentMade = formattedDueDate;
                if (this.monthsLeft > 0 && this.currentBalance == 0) {
                    this.monthsLeft--;
                }

                return true;
            }else{
                //Would result in overpayment
                return false;
            }
        }else{
            //called by non-credit account
            return false;
        }
    }

    @Override
    public String toString() {
    return "account ID='" + accountId + '\'' +
        ", current balance='" + currentBalance + '\'' +
        ", interest rate='" + interestRate + '\'' +
        ", next payment due date='" + nextPaymentDueDate + '\'' +
        ", date notified of payment='" + currentPaymentAmount + '\'' +
        ", current payment due='" + dateBillSent + '\'' +
        ", last payment made=" + lastPaymentMade +
        ", missed payment flag=" + missedPaymentFlag +
        ", loan type='" + loanType + '\'' +
        ", credit limit='" + creditLimit + '\'' +
        ", months left='" + monthsLeft;
  }
}
