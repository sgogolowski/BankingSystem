/**
 * Controller class for Close.fxml.
 * Contains all the scene objects and appropriate event handler methods.
 * @authors Szymon Gogolowski, Julio Collado
 */
package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class CloseController extends Main {

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private Button goHome;

    @FXML
    private Button closeButton;

    @FXML
    private RadioButton MoneyMarketRadio;

    @FXML
    private ToggleGroup AccountType;

    @FXML
    private RadioButton CheckingRadio;

    @FXML
    private RadioButton SavingsRadio;

    /**
     * goHome event handler method.
     * Changes the scene to the home scene when 'Home' button is clicked.
     * @param event being fired.
     */
    @FXML
    void goHome(ActionEvent event) throws IOException {
    	Parent open = FXMLLoader.load(getClass().getResource("Home.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }
    
    /**
     * closeAccount event handler method.
     * Extracts all the user input from the scene controls and attempt to remove
     * the Account from the database. Afterwards, a dialog box appears with the
     * result of the attempted account removal.
     * @param event being fired
     */
    @FXML
    void closeAccount(ActionEvent event) throws IOException {
    	try {
	    	boolean closed = false;
	    	String first_name = fname.getText();
	    	String last_name = lname.getText();
	    	if (AccountType.getSelectedToggle().equals(CheckingRadio)) {
	    		if (db.remove(new Checking(new Profile(first_name, last_name), 0, null, true)))
	    			closed = true;
	    	}
	    	else if (AccountType.getSelectedToggle().equals(SavingsRadio)) {
	    		if (db.remove(new Savings(new Profile(first_name, last_name), 0, null, true)))
	    			closed = true;
	    	}
	    	else if (AccountType.getSelectedToggle().equals(MoneyMarketRadio)) {
	    		if (db.remove(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0)))
	    			closed = true;
	    	}
	    	
	    	Dialog<String> info = new Dialog<String>();
	    	if (closed) {
		    	info.setTitle("Account closed.");
		    	ButtonType home = new ButtonType("Home", ButtonData.OK_DONE);
		    	info.getDialogPane().getButtonTypes().add(home);
		    	info.setContentText("Account closed and removed from the database.");
		    	info.showAndWait();
		    	Parent open = FXMLLoader.load(getClass().getResource("Close.fxml"));
		    	Scene openPage = new Scene(open);
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	stage.setScene(openPage);
		    	stage.show();
	    	}
	    	else {
	    		info.setTitle("Account does not exist.");
		    	ButtonType home = new ButtonType("Home", ButtonData.OK_DONE);
		    	info.getDialogPane().getButtonTypes().add(home);
		    	info.setContentText("Account does not exist.");
		    	info.showAndWait();
		    	Parent open = FXMLLoader.load(getClass().getResource("Close.fxml"));
		    	Scene openPage = new Scene(open);
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	stage.setScene(openPage);
		    	stage.show();
	    	}
    	}
    	catch (NullPointerException npe) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Input mismatch");
	    	alert.setContentText("Invalid input");
	    	alert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Close.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();
    	}
    	
    }

}

