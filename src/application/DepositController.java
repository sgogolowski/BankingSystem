package application;

import java.io.IOException;
import java.util.InputMismatchException;

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

public class DepositController extends Main {

    @FXML
    private TextField depositAmount;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private Button goHome;

    @FXML
    private RadioButton MoneyMarketRadio;

    @FXML
    private ToggleGroup AccountType;

    @FXML
    private Button depositButton;

    @FXML
    private RadioButton CheckingRadio;

    @FXML
    private RadioButton SavingsRadio;

    @FXML
    void goHome(ActionEvent event) throws IOException {
    	Parent open = FXMLLoader.load(getClass().getResource("Home.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }
    @FXML
    void depositAmount(ActionEvent event) throws IOException, NullPointerException {
    	try {
	    	boolean success = false;
	    	double amount = Double.valueOf(depositAmount.getText());
	    	if (amount < 0)
	    		throw new InputMismatchException("Input data type mismatch.");
	    	String first_name = fname.getText();
	    	String last_name = lname.getText();
	    	
	    	if (AccountType.getSelectedToggle().equals(CheckingRadio))
	    		if (db.deposit(new Checking(new Profile(first_name, last_name), 0, null, true), amount))
	    			success = true;
	    	else if (AccountType.getSelectedToggle().equals(SavingsRadio))
	    		if (db.deposit(new Savings(new Profile(first_name, last_name), 0, null, true), amount))
	    			success = true;
	    	else if (AccountType.getSelectedToggle().equals(MoneyMarketRadio))
	    		if (db.deposit(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount))
	    			success = true;
	    	
	    	Dialog<String> info = new Dialog<String>();
	    	if (success) {
		    	info.setTitle("Successful deposit.");
		    	ButtonType home = new ButtonType("Home", ButtonData.OK_DONE);
		    	info.getDialogPane().getButtonTypes().add(home);
		    	String text = String.format("$%.2f deposited to the account.", amount);
		    	info.setContentText(text);
		    	info.showAndWait();
		    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
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
		    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
		    	Scene openPage = new Scene(open);
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	stage.setScene(openPage);
		    	stage.show();
	    	}
    	}
    	catch (NumberFormatException nfe) {
    		Alert openAlert = new Alert(AlertType.ERROR);
    		openAlert.setTitle("Input mismatch");
    		openAlert.setContentText("Input mismatch, try again.");
    		openAlert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();

    	}
    	catch (InputMismatchException ime) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Input mismatch, try again.");
	    	alert.setContentText(ime.getMessage());
	    	alert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();
    	}
    	catch (NullPointerException npe) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Input mismatch");
	    	alert.setContentText("Invalid input.");
	    	alert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();
    	}
    }

}
