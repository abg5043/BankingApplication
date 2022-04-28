package agrant.bankingapplication.classes;

import agrant.bankingapplication.LoginController;
import com.opencsv.bean.CsvBindByName;

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

    public Checking(String accountId, String accountType, double currentBalance, String backupAccountId, int overDrafts, String openDate, String interest){
        this.accountId = accountId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.backupAccountId = backupAccountId;
        this.overdrafts = overDrafts;
        this.openDate = openDate;
        this.interest = interest;
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

    public Boolean oneTimeDeposit(double cashAmount, LoginController loginController) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 50c per transaction
            if(cashAmount < 0.5) {
                return false;
            } else {
                this.currentBalance += (cashAmount - 0.5);
                if(this.currentBalance >= 1000) {
                    this.accountType = "Gold";
                    //check if there is backup savings
                    Savings linkedSavings = loginController.findSavingsByID(loginController.getCurrentUser().getSSN() + "_s");
                    if(linkedSavings != null) {
                        this.interest = String.valueOf(0.5 * linkedSavings.getInterestRate());
                    }
                }
                return true;
            }
        } else {
            //"Gold" has no transaction fee
            this.currentBalance += cashAmount;
            return true;
        }

    }

    public Boolean oneTimeWithdraw(double cashAmount) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 50c per transaction
            if(this.currentBalance >= (cashAmount + 0.5)) {
                this.currentBalance -= (cashAmount + 0.5) ;
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

    public Boolean monthlyDeposit(double cashAmount) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 75c per transaction
            if(cashAmount < 0.75) {
                return false;
            } else {
                this.currentBalance += (cashAmount - 0.75);
                return true;
            }
        } else {
            //"Gold" has no transaction fee
            this.currentBalance += cashAmount;
            return true;
        }

    }

    public Boolean monthlyWithdraw(double cashAmount) {
        if(this.accountType.equals("Regular")) {
            //"That's my bank" type of account has 75c per monthly transaction
            if(this.currentBalance >= (cashAmount + 0.75)) {
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
