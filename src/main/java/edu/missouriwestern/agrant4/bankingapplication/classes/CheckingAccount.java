package edu.missouriwestern.agrant4.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

public class CheckingAccount {
  @CsvBindByName(column = "customer_id")
  private String id;

  @CsvBindByName(column = "account_type")
  private String accountType;

  @CsvBindByName(column = "current_balance")
  private String balance;

  @CsvBindByName(column = "backup_account_id")
  private String backupID;

  //overdrafts this month
  @CsvBindByName private String overdrafts;

  @CsvBindByName(column = "account_open_date")
  private String openDate;
}
