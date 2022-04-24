module bankingapplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;
  requires org.apache.logging.log4j;

  opens agrant.bankingapplication to javafx.fxml;
  exports agrant.bankingapplication;
  exports agrant.bankingapplication.classes;
  opens agrant.bankingapplication.classes to javafx.fxml;
}