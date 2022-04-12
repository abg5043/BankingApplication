package edu.missouriwestern.agrant4.bankingapplication;

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
  // holds a reference to the previous controller for back button
  private Controller previousController;
  // holds the view's title
  private String currentTitle;
  // holds a reference to the current view
  private String currentView;

  @FXML
  private Button exitButton;

  @FXML
  private Button backButton;

  @FXML
  void backClicked(ActionEvent event) {
    setNewScene(previousController,  previousController.getCurrentView(), previousController.getCurrentTitle() );
  }

  @FXML
  void exitClicked(ActionEvent event) {
    System.exit(0);
  }


  /**
   * Constructor for controller class
   *
   * @param previousStage - the Stage object from the previous class
   * @param previousController - a pointer to the previous controller
   */
  public Controller(Stage previousStage,Controller previousController) {
    this.currentStage = previousStage;
    this.previousController = previousController;
  }


  /**
   * Default no-arg controller; used for new stages.
   * constructs a new stage and sets previous controller
   * to null since there is no view to go back to.
   */
  public Controller() {
    this.currentStage = new Stage();
    this.previousController = null;
  }

  public Stage getCurrentStage() {
    return currentStage;
  }

  public void setCurrentStage(Stage currentStage) {
    this.currentStage = currentStage;
  }

  public Controller getPreviousController() {
    return previousController;
  }

  public void setPreviousController(Controller previousController) {
    this.previousController = previousController;
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

  public String getCurrentTitle() {
    return currentTitle;
  }

  public void setCurrentTitle(String currentTitle) {
    this.currentTitle = currentTitle;
  }

  public String getCurrentView() {
    return currentView;
  }

  public void setCurrentView(String currentView) {
    this.currentView = currentView;
  }

  /**
   * Show the stage that was loaded in the constructor
   */
  public void showStage() {
    currentStage.show();
  }

}
