package agrant.bankingapplication.classes;

import com.opencsv.bean.CsvBindByName;

/**
 * Object that represents a user of the banking program.
 */
public class User {
  @CsvBindByName private String user;

  @CsvBindByName private String pass;

  @CsvBindByName private String SSN;

  @CsvBindByName private String address;

  @CsvBindByName private String city;

  @CsvBindByName private String state;

  @CsvBindByName private String zip;

  @CsvBindByName private Boolean manager;

  @CsvBindByName private Boolean customer;

  @CsvBindByName private Boolean teller;

  @CsvBindByName(column = "first_name")
  private String firstName;

  @CsvBindByName(column = "last_name")
  private String lastName;

  @CsvBindByName private String pin;

  /**
   * Full constructor for User
   *
   * @param user - user's username
   * @param pass - user's password
   * @param SSN - user's SSN
   * @param address - user's address
   * @param city - user's city
   * @param state - user's state
   * @param zip - user's zip
   * @param manager - boolean to tell if user is a manager
   * @param customer - boolean to tell if user is a customer
   * @param teller - boolean to tell if user is a teller
   * @param firstName - user's first name
   * @param lastName - user's last name
   * @param pin - the user's ATM pin
   */
  public User(String user, String pass, String SSN, String address, String city, String state, String zip, Boolean manager, Boolean customer, Boolean teller, String firstName, String lastName, String pin) {
    this.user = user;
    this.pass = pass;
    this.SSN = SSN;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.manager = manager;
    this.customer = customer;
    this.teller = teller;
    this.firstName = firstName;
    this.lastName = lastName;
    this.pin = pin;
  }

  /** Default no-arg constructor needed for openCSV */
  public User() {
  }

  public String getSSN() {
    return SSN;
  }

  public void setSSN(String SSN) {
    this.SSN = SSN;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }


  public void setZip(String zip) {
    this.zip = zip;
  }


  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public Boolean getManager() {
    return manager;
  }

  public void setManager(Boolean manager) {
    this.manager = manager;
  }

  public Boolean getCustomer() {
    return customer;
  }

  public void setCustomer(Boolean customer) {
    this.customer = customer;
  }

  public Boolean getTeller() {
    return teller;
  }

  public void setTeller(Boolean teller) {
    this.teller = teller;
  }

  @Override
  public String toString() {
    return "user='" + user + '\'' +
        ", pass='" + pass + '\'' +
        ", SSN='" + SSN + '\'' +
        ", address='" + address + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", zip='" + zip + '\'' +
        ", manager=" + manager +
        ", customer=" + customer +
        ", teller='" + teller + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", pin='" + pin;
  }
}
