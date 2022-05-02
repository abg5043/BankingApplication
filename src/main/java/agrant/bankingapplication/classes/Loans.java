package agrant.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public Loans(
        String ID,
        double currentBalance,
        double interestRate,
        String nextPaymentDueDate,
        double currentPaymentDue,
        String lastPaymentMade,
        int missedPaymentFlag,
        String loanType,
        int creditLimit,
        int monthsLeft
    ){
        this.accountId = ID;
        this.currentBalance = currentBalance;
        this.interestRate = interestRate;
        this.nextPaymentDueDate = nextPaymentDueDate;
        this.currentPaymentAmount = currentPaymentDue;
        this.dateBillSent = nextPaymentDueDate;
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
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            //parse next payment due date to compare to current date
            LocalDate dueDate = LocalDate.parse(this.nextPaymentDueDate, formatters);
            String formattedDueDate = formatters.format(dueDate);

            if((this.currentBalance - payAmt) >= 0) {
                this.currentBalance -= payAmt;
                this.lastPaymentMade = formattedDueDate;

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

    /**
     * Prints the report as a formatted markdown file.
     *
     * @param fileName -- the file name for the created markdown file
     */
    public void printBill(String fileName, ArrayList<Transactions> transactionsArrayList, User currentUser) {
        try {
            PrintWriter output = new PrintWriter(fileName);
            output.println("# Bill for " + currentUser.getFirstName() + " " + currentUser.getLastName());

            output.println("## Account ID " + this.accountId);

            output.println("### Account Information");

            output.println("* Loan type: " + this.loanType);
            output.println("* Credit Limit: " + this.creditLimit);
            output.println("* Current balance: " + this.currentBalance);
            output.println("* Interest Rate: " + this.interestRate);


            output.println("### Payment Information");
            output.println("* Current Payment Due: " + this.currentPaymentAmount);
            output.println("* Next Payment Due: " + this.nextPaymentDueDate);
            output.println("* Last Payment Made: " + this.lastPaymentMade);
            output.println("* Months left on loan: " + this.monthsLeft);

            output.println("### Recent Transactions");
            int j = 1;
            for(int i = 0; i < transactionsArrayList.size(); i++) {
                Transactions currentTransaction = transactionsArrayList.get(i);
                if(currentTransaction.getAccountID().equals(this.accountId)) {
                    output.println("\nTransaction " + j + ": " + currentTransaction.getDate() + ": " + currentTransaction.getMemo());
                    j++;
                }

            }

            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "Loans{" +
            "accountId='" + accountId + '\'' +
            ", currentBalance=" + currentBalance +
            ", interestRate=" + interestRate +
            ", nextPaymentDueDate='" + nextPaymentDueDate + '\'' +
            ", dateBillSent='" + dateBillSent + '\'' +
            ", currentPaymentAmount=" + currentPaymentAmount +
            ", lastPaymentMade='" + lastPaymentMade + '\'' +
            ", missedPaymentFlag=" + missedPaymentFlag +
            ", loanType='" + loanType + '\'' +
            ", creditLimit=" + creditLimit +
            ", monthsLeft=" + monthsLeft +
            '}';
    }
}
