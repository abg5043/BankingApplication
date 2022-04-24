package agrant.bankingapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/** This is the base class for all controllers. */
public class Controller {

  // holds this program's Stage
  private Stage currentStage;
  // holds a reference to the system home page
  private LoginController loginController;
  // holds a reference to the current user type's home page
  private Controller mainPage;
  // holds the view's title
  private String currentViewTitle;
  // holds a reference to the current view file
  private String currentViewFile;

  @FXML
  private Button exitButton;

  @FXML
  private Button mainPageButton;

  @FXML
  void exitClicked(ActionEvent event) {
    loginController.writeBankData();
    System.exit(0);
  }

  @FXML
  void mainPageClicked(ActionEvent event) {
    setNewScene(mainPage,  mainPage.getCurrentViewFile(), mainPage.getCurrentViewTitle());
  }


  /**
   * Constructor for controller class
   *
   * @param currentStage - the Stage object for the program
   * @param loginController - a pointer to the system login page
   * @param mainPage - a pointer to the user type's main page
   */
  public Controller(Stage currentStage, LoginController loginController, Controller mainPage) {
    this.loginController = loginController;
    this.currentStage = currentStage;
    this.mainPage = mainPage;
  }

  public LoginController getLoginController() {
    return loginController;
  }

  /**
   * Default no-arg controller; used for new stages.
   * constructs a new stage and sets previous controller
   * to null since there is no view to go back to.
   *
   * Similarly, the only constructor to use a no-arg constructor
   * should be the LoginController, which doesn't need
   * a reference to itself.
   */
  public Controller() {
    this.currentStage = new Stage();
    this.loginController = null;
    this.mainPage = null;
  }

  public Stage getCurrentStage() {
    return currentStage;
  }

  public void setCurrentStage(Stage currentStage) {
    this.currentStage = currentStage;
  }

  public String getCurrentViewTitle() {
    return currentViewTitle;
  }

  public void setCurrentViewTitle(String currentViewTitle) {
    this.currentViewTitle = currentViewTitle;
  }

  public String getCurrentViewFile() {
    return currentViewFile;
  }

  public void setCurrentViewFile(String currentViewFile) {
    this.currentViewFile = currentViewFile;
  }

  public Controller getMainPage() {
    return mainPage;
  }

  public void setNewScene(Object scene, String view, String title) {
    try {

      //create new FXMLLoader so we can use .fxml files as views
      FXMLLoader loader = new FXMLLoader(getClass().getResource(view));

      // Set this class as the controller
      loader.setController(scene);

      // Load the scene
      currentStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      currentStage.setTitle(title);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    currentStage.show();
  }

}
