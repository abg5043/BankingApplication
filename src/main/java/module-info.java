module edu.missouriwestern.agrant4.bankingapplication {
  requires javafx.controls;
  requires javafx.fxml;


  opens edu.missouriwestern.agrant4.bankingapplication to javafx.fxml;
  exports edu.missouriwestern.agrant4.bankingapplication;
}