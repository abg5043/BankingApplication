package agrant.bankingapplication.classes;

import agrant.bankingapplication.LoginController;
import com.opencsv.bean.CsvBindByName;

/**
 * This class encapsulates a checking account.
 */
public class Checking {
    @CsvBindByName(column = "account_id")
    private String accountId;

    @CsvBindByName(column = "interest")
    private String interest;

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

    /**
     * Constructor for checking account
     *
     * @param accountId - account id
     * @param accountType - account type (gold or regular)
     * @param currentBalance - current balance of the account
     * @param backupAccountId - backup savings ID (no CDs)
     * @param overDrafts - number of overdrafts
     * @param openDate - when the account was opened
     * @param interest - account interest rate (if this is gold)
     */
    public Checking(String accountId, String accountType, double currentBalance, String backupAccountId, int overDrafts, String openDate, String interest){
        this.accountId = accountId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.backupAccountId = backupAccountId;
        this.overdrafts = overDrafts;
        this.openDate = openDate;
        this.interest = interest;
    }

    //blank constructor for OpenCSV
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

    public String getInterest() {
        return interest;
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
    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * does a one-time deposit into this account.
     *
     * @param cashAmount - how much the deposit is
     * @param loginController - the current loginController
     * @return true or false depending on if you can do the deposit
     */
    public Boolean oneTimeDeposit(double cashAmount, LoginController loginController) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 50c per transaction
            if(cashAmount < 0.5) {
                //return false if the deposit is lower than the fee
                return false;
            } else {
                //adjust balance
                this.currentBalance += (cashAmount - 0.5);
                //check if balance is over 1000
                if(this.currentBalance >= 1000) {
                    //if so, this account should be changed to gold
                    this.accountType = "Gold";
                    //check if there is backup savings or CD
                    Savings customerSavings;
                    if(loginController.findSavingsByID(this.getAccountId().substring(0,9) + "_s") != null) {
                        //there is a savings
                        customerSavings = loginController.findSavingsByID(this.getAccountId().substring(0,9) + "_s");
                    } else {
                        //check if there is a cd
                        customerSavings = loginController.findCDByID(this.getAccountId().substring(0,9) + "_s");
                    }
                    if(customerSavings != null) {
                        //there is a linked savings, so interest is calculated
                        double rate = 0.5 * customerSavings.getInterestRate();
                        rate = Math.round(rate *100.0)/100.0;
                        this.interest = String.valueOf(rate);
                    } else {
                        //no linked savings, so no interest
                        this.interest = "n/a";
                    }
                }
                return true; //deposit went through
            }
        } else {
            //"Gold" has no transaction fee
            this.currentBalance += cashAmount;
            return true; //deposit went through
        }

    }

    /**
     * A method for withdrawing from the account one time
     *
     * @param cashAmount - how much the withdraw is
     * @return - true or false depending on if the withdraw was successful
     */
    public Boolean oneTimeWithdraw(double cashAmount) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 50c per transaction
            if(this.currentBalance >= (cashAmount + 0.5)) {
                //we can withdraw. Do so including fee
                this.currentBalance -= (cashAmount + 0.5) ;
                return true;
            } else {
                //not enough money. Do not withdraw
                return false;
            }
        } else {
            //"Gold" type of account has no transaction fee
            //check if we have enough money
            if(this.currentBalance >= cashAmount) {
                this.currentBalance -= cashAmount;
                //Check if the balance goes too low to be Gold
                if(this.currentBalance < 1000) {
                    this.accountType = "Regular";
                    this.interest = "n/a";
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * A method for depositing money into the account monthly
     *
     * @param cashAmount - how much the deposit is
     * @param loginController - the current loginController
     * @return true or false depending on if you can do the deposit
     */
    public Boolean monthlyDeposit(double cashAmount, LoginController loginController) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 75c per transaction
            if(cashAmount < 0.75) {
                //if trying to deposit less than the fee, don't allow it
                return false;
            } else {
                this.currentBalance += (cashAmount - 0.75);
                if(this.currentBalance >= 1000) {
                    //if we deposit enough to take balance >= 1000, we turn the account to gold status
                    this.accountType = "Gold";
                    //check if there is savings or CD
                    Savings customerSavings;
                    if(loginController.findSavingsByID(this.getAccountId().substring(0,9) + "_s") != null) {
                        //there is a savings
                        customerSavings = loginController.findSavingsByID(this.getAccountId().substring(0,9) + "_s");
                    } else {
                        //check if there is a cd
                        customerSavings = loginController.findCDByID(this.getAccountId().substring(0,9) + "_s");
                    }

                    if(customerSavings != null) {
                        //linked savings, so interest is calculated
                        double rate = 0.5 * customerSavings.getInterestRate();
                        rate = Math.round(rate *100.0)/100.0;
                        this.interest = String.valueOf(rate);
                    } else {
                        //no linked savings, so no interest
                        this.interest = "n/a";
                    }
                }
                return true;
            }
        } else {
            //"Gold" has no transaction fee, so just deposit
            this.currentBalance += cashAmount;
            return true;
        }

    }

    /**
     * A method for withdrawing money into the account monthly
     *
     * @param cashAmount - how much the deposit is
     * @return true or false depending on if you can do the deposit
     */
    public Boolean monthlyWithdraw(double cashAmount) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 75c per monthly transaction
            if(this.currentBalance >= (cashAmount + 0.75)) {
                //if the fee is smaller than the withdraw, we don't allow so customer doesn't lose money
                this.currentBalance -= (cashAmount + 0.75) ;
                return true;
            } else {
                return false;
            }
        } else {
            //"Gold" type of account has no transaction fee
            if(this.currentBalance >= cashAmount) {
                this.currentBalance -= cashAmount;
                //Check if the balance goes too low to be Gold
                if(this.currentBalance < 1000) {
                    this.accountType = "Regular";
                    this.interest = "n/a";
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
    return "account ID='" + accountId + '\'' +
        ", account type='" + accountType + '\'' +
        ", current balance='" + currentBalance + '\'' +
        ", backup account id='" + backupAccountId + '\'' +
        ", overdrafts='" + overdrafts + '\'' +
        ", open date='" + openDate + '\'' +
        ", interest='" + interest;
  }
}   
