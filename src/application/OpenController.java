package application;

import java.io.IOException;
import java.util.InputMismatchException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class OpenController extends Main{
	
    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField balance;
    
    @FXML
    private TextField day;

    @FXML
    private TextField month;
    
    @FXML
    private TextField year;
    
    @FXML
    private CheckBox DirectDepositCheckBox;

    @FXML
    private CheckBox LoyalCheckBox;

    @FXML
    private RadioButton MoneyMarketRadio;

    @FXML
    private ToggleGroup AccountType;

    @FXML
    private RadioButton CheckingRadio;

    @FXML
    private RadioButton SavingsRadio;
    
    @FXML
    private TextField withdrawals;
    
    @FXML
    private Button openButton;
    
    @FXML
    private Button goHome;
    
    
    
    @FXML
    void showDirectDeposit(ActionEvent event) {
    	DirectDepositCheckBox.setDisable(false);
    	LoyalCheckBox.setDisable(true);
    	withdrawals.setDisable(true);
    }

    @FXML
    void showLoyalCustomer(ActionEvent event) {
    	DirectDepositCheckBox.setDisable(true);
    	LoyalCheckBox.setDisable(false);
    	withdrawals.setDisable(true);
    }

    @FXML
    void showWithdrawals(ActionEvent event) {
    	DirectDepositCheckBox.setDisable(true);
    	LoyalCheckBox.setDisable(true);
    	withdrawals.setDisable(false);
    }

    @FXML
    void openAccount(ActionEvent event) throws NumberFormatException, IOException {
    	try {
    	
	    	boolean added = false;
	    	String first_name = fname.getText();
	    	String last_name = lname.getText();
	    	double balance_amount = Double.valueOf(balance.getText());
	    	if (balance_amount < 0)
	    		throw new NumberFormatException("Input mismatch, try again.");
	    	String month_input = month.getText();
	    	String day_input = day.getText();
	    	String year_input = year.getText();
	    	String inputDate = month_input + "/" + day_input + "/" + year_input;
	    	Date date = parseDate(inputDate);
	    	if (!(date.isValid()))
	    		throw new InputMismatchException(date + " is not a valid date.");
	    	if (AccountType.getSelectedToggle().equals(CheckingRadio)) {
	    		boolean direct_deposit = DirectDepositCheckBox.isSelected();
	    		if (db.add(new Checking(new Profile(first_name, last_name), balance_amount, date, direct_deposit)))
	    			added = true;
	    		System.out.println(db.printAccounts());
	    	}
	    	else if (AccountType.getSelectedToggle().equals(SavingsRadio)) {
	    		boolean loyal = LoyalCheckBox.isSelected();
	    		if (db.add(new Savings(new Profile(first_name, last_name), balance_amount, date, loyal)))
	    			added = true;
	    	}
	    	else if (AccountType.getSelectedToggle().equals(CheckingRadio)) {
	    		int num_withdrawals = Integer.valueOf(withdrawals.getText());
	    		if (num_withdrawals < 0)
	    			throw new InputMismatchException();
	    		if (db.add(new MoneyMarket(new Profile(first_name, last_name), balance_amount, date, num_withdrawals)))
	    			added = true;
	    	}
	    	
	    	Dialog<String> info = new Dialog<String>();
	    	if (added) {
		    	info.setTitle("Account added.");
		    	ButtonType home = new ButtonType("Home", ButtonData.OK_DONE);
		    	info.getDialogPane().getButtonTypes().add(home);
		    	info.setContentText("Account opened and added to the database.");
		    	info.showAndWait();
		    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
		    	Scene openPage = new Scene(open);
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	stage.setScene(openPage);
		    	stage.show();
	    	}
	    	else {
	    		info.setTitle("Account exists.");
		    	ButtonType home = new ButtonType("Home", ButtonData.OK_DONE);
		    	info.getDialogPane().getButtonTypes().add(home);
		    	info.setContentText("Account is already in the database.");
		    	info.showAndWait();
		    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
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
	    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();

    	}
    	catch (InputMismatchException ime) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Input mismatch");
	    	alert.setContentText(ime.getMessage());
	    	alert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();
    	}
    	catch (NullPointerException npe) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Input mismatch");
	    	alert.setContentText("Invalid input");
	    	alert.showAndWait();
	    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
	    	Scene openPage = new Scene(open);
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.setScene(openPage);
	    	stage.show();
    	}
    }

    @FXML
    void quit(ActionEvent event) {

    }
    
    @FXML
    void goHome(ActionEvent event) throws IOException {
    	Parent open = FXMLLoader.load(getClass().getResource("Home.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }
    
    private Date parseDate(String input) {
		String[] dateTokens = input.split("/");
		if(dateTokens == null || dateTokens.length != 3)
			return null;
		
		Date inputedDate = new Date(Integer.valueOf(dateTokens[2]), Integer.valueOf(dateTokens[0]),
				Integer.valueOf(dateTokens[1]));
		return inputedDate;
	}

}
