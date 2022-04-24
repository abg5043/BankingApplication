module edu.missouriwestern.agrant4.bankingapplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;
  requires org.apache.logging.log4j;

  opens edu.missouriwestern.agrant4.bankingapplication to javafx.fxml;
  exports edu.missouriwestern.agrant4.bankingapplication;
  exports edu.missouriwestern.agrant4.bankingapplication.classes;
  opens edu.missouriwestern.agrant4.bankingapplication.classes to javafx.fxml;
}