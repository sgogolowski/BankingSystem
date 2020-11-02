package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *  Main controller class for TransactionManager.fxml.
 *  Contains all the objects on the GUI and the appropriate event handlers.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class MainController {
	protected AccountDatabase db = new AccountDatabase();
	
	@FXML
    private TabPane tabPane;
	
	@FXML
    private TextField month, year, fnameClose, balanceDW, day, balanceOpen, lnameDW, fnameOpen, lnameOpen, lnameClose, fnameDW;

    @FXML
    private MenuItem exportMenu,importMenu, pnMenu, pdMenu, paMenu;

    @FXML
    private RadioButton moneymarketRadioDW, moneymarketRadioOpen, checkingRadioDW, savingsRadioOpen, savingsRadioDW, checkingRadioOpen, 
    checkingRadioClose, moneymarketRadioClose, savingsRadioClose;


    @FXML
    private CheckBox DirectDepositCheckbox, LoyalCheckbox;

    @FXML
    private MenuButton databaseMenu, statementsMenu;

    @FXML
    private ToggleGroup AccountTypeClose, AccountTypeOpen, AccountTypeDW;
    
    @FXML
    private TextArea closeOutput, openOutput, deposit_withdraw_output, databaseOutput;
    
    /**
     * Method to enables the direct deposit checkbox and disable the the loyal customer checkbox and withdrawals.
     * @param event being handled
     */
    @FXML
    void disableSM(ActionEvent event) {
    	DirectDepositCheckbox.setDisable(false);
    	LoyalCheckbox.setDisable(true);
    	LoyalCheckbox.setSelected(false);
    }
    
    /**
     * Method to enables the loyal customer checkbox and disable the the direct depsoit checkbox and withdrawals.
     * @param event being handled
     */
    @FXML
    void disableCM(ActionEvent event) {
    	LoyalCheckbox.setDisable(false);
    	DirectDepositCheckbox.setDisable(true);
    	DirectDepositCheckbox.setSelected(false);
    }
    
    /**
     * Method to enables the withdrawals textfield and disable the the loyal customer checkbox and direct deposit checkbox.
     * @param event being handled
     */
    @FXML
    void disableCS(ActionEvent event) {
    	DirectDepositCheckbox.setDisable(true);
    	DirectDepositCheckbox.setSelected(false);
    	LoyalCheckbox.setDisable(true);
    	LoyalCheckbox.setSelected(false);
    }

    /**
     * Event handler to open an account.
     * Gets all the user input from the GUI then creates the appropriate account. Prints the output into the textarea and 
     * handles all possible errors.
     * @param event being handled
     */
    @FXML
    void openAccount(ActionEvent event) {

    	try {
    		//get user input
	    	String first_name = fnameOpen.getText();
	    	String last_name = lnameOpen.getText();
	    	if (first_name.equals("") || last_name.equals("") )
	    		throw new InputMismatchException("Missing input, try again.");
	    	String balance = balanceOpen.getText();
	    	if (balance.equals(""))
	    		throw new InputMismatchException("Missing input, try again.");
	    	if (Double.valueOf(balance) < 0) 
	    		throw new NumberFormatException("Input mismatch, try again.");
	    	double balance_amount = Double.valueOf(balance);
	    	String month_input = month.getText();
	    	String day_input = day.getText();
	    	String year_input = year.getText();
	    	String inputDate = month_input + "/" + day_input + "/" + year_input;
	    	Date date = parseDate(inputDate);
	    	if (!(date.isValid()))
	    		throw new InputMismatchException(date + " is not a valid date.");
	    	//checking account
	    	if (AccountTypeOpen.getSelectedToggle().equals(checkingRadioOpen)) {
	    		boolean direct_deposit = DirectDepositCheckbox.isSelected();
	    		if (db.add(new Checking(new Profile(first_name, last_name), balance_amount, date, direct_deposit)))
	    			openOutput.appendText("Account opened and added to the database.\n");
	    		else
	    			openOutput.appendText("Account is already in the database.\n");
	    	}
	    	//savings account
	    	else if (AccountTypeOpen.getSelectedToggle().equals(savingsRadioOpen)) {
	    		boolean loyal = LoyalCheckbox.isSelected();
	    		if (db.add(new Savings(new Profile(first_name, last_name), balance_amount, date, loyal)))
	    			openOutput.appendText("Account opened and added to the database.\n");
	    		else
	    			openOutput.appendText("Account is already in the database.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeOpen.getSelectedToggle().equals(moneymarketRadioOpen)) {
	    		if (db.add(new MoneyMarket(new Profile(first_name, last_name), balance_amount, date, 0)))
	    			openOutput.appendText("Account opened and added to the database.\n");
	    		else
	    			openOutput.appendText("Account is already in the database.\n");
	    	}
	    	
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		openOutput.appendText("Input mismatch.\n");		
    	}
    	catch (InputMismatchException ime) {
    		openOutput.appendText(ime.getMessage() +"\n");
    	}
    	catch (NullPointerException npe) {
    		openOutput.appendText("Missing input, try again.\n");
    	}
    	finally {
    		//clear all inputs on the GUI
    		fnameOpen.setText("");
    		lnameOpen.setText("");
    		balanceOpen.setText("");
    		month.setText("");
    		day.setText("");
    		year.setText("");
    		if (AccountTypeOpen.getSelectedToggle() != null)
    			AccountTypeOpen.getSelectedToggle().setSelected(false);
    		DirectDepositCheckbox.setDisable(false);
    		LoyalCheckbox.setDisable(false);
    		DirectDepositCheckbox.setSelected(false);
    		LoyalCheckbox.setSelected(false);
    		
    	}
    	
    
    }

    /**
     * Event handler to close an account.
     * Gets all the user input from the GUI then closes the appropriate account. Prints the output into the textarea and 
     * handles all possible errors.
     * @param event being handled
     */
    @FXML
    void closeAccount(ActionEvent event) throws IOException {
    	try {
    		//get user input
	    	String first_name = fnameClose.getText();
	    	String last_name = lnameClose.getText();
	    	if (first_name.equals("") || last_name.equals("") )
	    		throw new InputMismatchException("Missing input, try again.");
	    	//checking account
	    	if (AccountTypeClose.getSelectedToggle().equals(checkingRadioClose)) {
	    		if (db.remove(new Checking(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.appendText("Account closed and removed from the database.\n");
	    		else
	    			closeOutput.appendText("Account does not exist.\n");
	    	}
	    	//savings account
	    	else if (AccountTypeClose.getSelectedToggle().equals(savingsRadioClose)) {
	    		if (db.remove(new Savings(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.appendText("Account closed and removed from the database.\n");
	    		else
	    			closeOutput.appendText("Account does not exist.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeClose.getSelectedToggle().equals(moneymarketRadioClose)) {
	    		if (db.remove(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0)))
	    			closeOutput.appendText("Account closed and removed from the database.\n");
	    		else
	    			closeOutput.appendText("Account does not exist.\n");
	    	}
	  
    	}
    	//error-handling for missing inputs
    	catch (NullPointerException npe) {
    		closeOutput.appendText("Missing input, try again.\n");
    	}
    	catch (InputMismatchException ime) {
    		closeOutput.appendText(ime.getMessage() + "\n");
    	}
    	finally {
    		//clear all inputs on the GUI
    		fnameClose.setText("");
    		lnameClose.setText("");
    		if (AccountTypeClose.getSelectedToggle() != null)
    			AccountTypeClose.getSelectedToggle().setSelected(false);
    		
    	}
    	
    }
    
    /**
     * Event handler to deposit into an account.
     * Gets all the user input from the GUI then deposits into the appropriate account. Prints the output into the textarea and 
     * handles all possible errors.
     * @param event being handled
     */
    @FXML
    void deposit(ActionEvent event) throws IOException, NullPointerException {
    	try {
    		//get user input
    		String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	if (first_name.equals("") || last_name.equals("") )
	    		throw new InputMismatchException("Missing input, try again.");
	    	String balance = balanceDW.getText();
	    	if (balance.equals(""))
	    		throw new InputMismatchException("Missing input, try again.");
	    	if (Double.valueOf(balance) < 0) 
	    		throw new NumberFormatException("Input mismatch, try again.");
	    	double amount = Double.valueOf(balance);
	    	
	    	
	    	//checking account
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW))
	    		if (db.deposit(new Checking(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.appendText(amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    	//savings account
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW))
	    		if (db.deposit(new Savings(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.appendText(amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    	//moneymarket account
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW))
	    		if (db.deposit(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount))
	    			deposit_withdraw_output.appendText(amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    
	
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.appendText("Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.appendText(ime.getMessage() +"\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.appendText("Missing input, try again.\n");
    	}
    	finally {
    		//clear all inputs on the GUI
    		fnameDW.setText("");
    		lnameDW.setText("");
    		balanceDW.setText("");
    		if (AccountTypeDW.getSelectedToggle() != null)
    			AccountTypeDW.getSelectedToggle().setSelected(false);
    		
    	}
    }
    
    /**
     * Event handler to withdraw frp, an account.
     * Gets all the user input from the GUI then deposits into the appropriate account. Prints the output into the textarea and 
     * handles all possible errors.
     * @param event being handled
     */
    @FXML
    void withdraw(ActionEvent event) {
    	try {
    		//get user input
    		String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	if (first_name.equals("") || last_name.equals("") )
	    		throw new InputMismatchException("Missing input, try again.");
	    	String balance = balanceDW.getText();
	    	if (balance.equals(""))
	    		throw new InputMismatchException("Missing input, try again.");
	    	if (Double.valueOf(balance) < 0) 
	    		throw new NumberFormatException("Input mismatch, try again.");
	    	double amount = Double.valueOf(balance);
	    	
	    	//checking account
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW)) {
	    		if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.appendText(amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.appendText("Insufficient funds.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    	}
	    	//savings account
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW)) {
	    		if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.appendText(amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.appendText("Insufficient funds.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW)) {
	    		if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 0)
	    			deposit_withdraw_output.appendText(amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 1)
	    			deposit_withdraw_output.appendText("Insufficient funds.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == -1)
	    			deposit_withdraw_output.appendText("Account does not exist.\n");
	    	}
	
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.appendText("Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.appendText(ime.getMessage() +"\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.appendText("Missing input, try again.\n");
    	}
    	finally {
    		//clear all inputs on the GUI
    		fnameDW.setText("");
    		lnameDW.setText("");
    		balanceDW.setText("");
    		if (AccountTypeDW.getSelectedToggle() != null)
    			AccountTypeDW.getSelectedToggle().setSelected(false);
    		
    	}

    }
    

    /**
     * importDB event  handler method reads a .txt file containing a list of accounts and
     * adds them to the database.
     * @param event being fired
     */
    @FXML
    void importDB(ActionEvent event) throws FileNotFoundException, IOException {
    	try {
	    	FileChooser fc = new FileChooser();
	    	fc.setTitle("Select database to import.");
	    	fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
	    	Window window = tabPane.getScene().getWindow();
	    	File selectedFile = fc.showOpenDialog(window);
	    	if (selectedFile != null) {
	    		Scanner fileReader = new Scanner(selectedFile);
	    		if (!(fileReader.hasNextLine())) {
	    			databaseOutput.appendText(selectedFile.getName() + " is empty.\n");
	    			return;
	    		}
	    		while (fileReader.hasNextLine()) {
	    			String line = fileReader.nextLine();
	    			String[] parse = line.split(",");
	    			if (parse.length != 6) {
	    				databaseOutput.appendText("Invalid data.\n");
	    				return;
	    			}
	    			else {
		    			String fname = parse[1];
		    			String lname = parse[2];
		    			double balance = Double.valueOf(parse[3]);
		    			Date date = parseDate(parse[4]);
		    			if (parse[0].equals("C")) {
		    				boolean directDeposit;
		    				if (parse[5].toUpperCase().equals("TRUE"))
		    					directDeposit = true;
		    				else if (parse[5].toUpperCase().equals("FALSE"));
		    					directDeposit = false;
		    				if (db.add(new Checking(new Profile(fname, lname), balance, date, directDeposit))) 
		    					databaseOutput.appendText("Account opened and added to the database.\n");
		    				else
		    					databaseOutput.appendText("Account is already in the database.\n");
		    			}
		    			else if (parse[0].equals("S")) {
		    				boolean loyal;
		    				if (parse[5].toUpperCase().equals("TRUE"))
		    					loyal = true;
		    				else if (parse[5].toUpperCase().equals("FALSE"));
		    					loyal = false;
		    				if (db.add(new Savings(new Profile(fname, lname), balance, date, loyal)))
		    					databaseOutput.appendText("Account opened and added to the database.\n");
		    				else
		    					databaseOutput.appendText("Account is already in the database.\n");
		    			}
		    			else if (parse[0].equals("M")) {
		    				int withdrawals = Integer.valueOf(parse[5]);
		    				if (db.add(new MoneyMarket(new Profile(fname, lname), balance, date, withdrawals)))
		    					databaseOutput.appendText("Account opened and added to the database.\n");
		    				else
		    					databaseOutput.appendText("Account is already in the database.\n");
		    			}
	    			}
	    		}
	    		fileReader.close();
	    	}
	    	databaseOutput.appendText(selectedFile.getName() + " was successfully imported.\n");
    	}
    	catch (NullPointerException npe) {
    		databaseOutput.appendText("No file was selected, try again.\n");
    	}
	    	

    		
    }
    /**
     * export database event handler to print all the accounts into a text file specified by the user. 
     * Prints the accounts in the format accountType,fname,lname,balance,dateOpened,unique value for each account
     * @param event being fired
     */
    @FXML
    void exportDB(ActionEvent event) throws IOException{
    	try {
	    	String accountType, fname, lname, balance, dateOpened, uniqueField;
	    	FileChooser fc = new FileChooser();
	    	fc.setTitle("Select text file to export database into.");
	    	fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
	    	Window window = tabPane.getScene().getWindow();
	    	File selectedFile = fc.showSaveDialog(window);
	    	//file writer to the text file
	    	FileWriter myWriter = new FileWriter(selectedFile);
	    	//make sure a file was selected
	    	if (selectedFile != null) {
	    		Account[] accounts = db.getAccounts();
	    		//loop through the array of accounts in the database
	    		for (Account ac : accounts) {
	    			if (ac == null) {
	    				break;
	    			}
	    			//get account type and its unique field
	    			if (ac instanceof Checking) {
	    				accountType = "C";
	    				uniqueField = String.valueOf(((Checking) ac).getDirectDeposit());
	    			}
	    			else if (ac instanceof Savings) {
	    				accountType = "S";
	    				uniqueField = String.valueOf(((Savings) ac).isLoyal());
	    			}
	    			else if (ac instanceof MoneyMarket) {
	    				accountType = "M";
	    				uniqueField = String.valueOf(((MoneyMarket) ac).getWithdrawals());
	    			}
	    			else
	    				throw new InputMismatchException();
	    			//get fname, lname, balance, and date opened
	    			fname = ac.getHolder().getFname();
	    			lname = ac.getHolder().getLname();
	    			balance = String.valueOf(ac.getBalance());
	    			dateOpened  = ac.getDateOpen().toString();
	    			//write to file
	    			myWriter.write(accountType + "," + fname + "," + lname + "," + balance + "," + dateOpened + "," + uniqueField + "\n");
	    		}
	    	}
	    	databaseOutput.appendText("Database was successfully exported into " + selectedFile.getName() + ".\n");
	    	myWriter.close();
    	}
    	catch (IOException ioe) {
    		databaseOutput.appendText("Unable to find file.\n");		
    	}
    	catch (NullPointerException npe) {
    		databaseOutput.appendText("No file was selected, try again.\n");
    	}
    }

    /**
     * Event handler to print account statements by last name into textarea.
     * Checks if database is empty, otherwise calls the printByLastName().
     * @param event being handled
     */
    @FXML
    void showPN(ActionEvent event) {
    	if (db.getSize() == 0)
    		databaseOutput.appendText("Database is empty.");
    	else
    		databaseOutput.appendText(db.printByLastName());
    }
    
    /**
     * Event handler to print account statement by date opened into textarea.
     * Checks if database is empty, otherwise calls the printByDateOpen().
     * @param event being handled
     */
    @FXML
    void showPD(ActionEvent event) {
    	if (db.getSize() == 0)
    		databaseOutput.appendText("Database is empty.");
    	else
    		databaseOutput.appendText(db.printByDateOpen());
    }
    
    /**
     * Event handler to print accounts into textarea.
     * Checks if database is empty, otherwise calls the printAccounts().
     * @param event being handled
     */
    @FXML
    void printAccounts(ActionEvent event) {
    	if (db.getSize() == 0)
    		databaseOutput.appendText("Database is empty.\n");
    	else
    		databaseOutput.appendText(db.printAccounts());
    }
    
    /**
     * Event handler to clear the text area in the Deposit/Withdraw tab
     * @param event being fired
     */
    @FXML
    void clearDW(ActionEvent event) {
    	deposit_withdraw_output.setText("");
    }
    
    /**
     * Event handler to clear the text area in the Close Account tab
     * @param event being fired
     */
    @FXML
    void clearClose(ActionEvent event) {
    	closeOutput.setText("");
    }
    
    /**
     * Event handler to clear the text area in the Open Account tab
     * @param event being fired
     */
    @FXML
    void clearOpen(ActionEvent event) {
    	openOutput.setText("");
    }
    
    /**
     * Event handler to clear the text area in the Account Database tab
     * @param event being fired
     */
    @FXML
    void clearDatabase(ActionEvent event) {
    	databaseOutput.setText("");
    }
    
    /**
     * Helper method to help parse String of a date in the format "mm/dd/yyyy" into a Date object.
     * @param input is the String object of the date
     * @return Date instance of the input
     */
    private Date parseDate(String input) {
		String[] dateTokens = input.split("/");
		if(dateTokens == null || dateTokens.length != 3)
			return null;
		
		Date inputedDate = new Date(Integer.valueOf(dateTokens[2]), Integer.valueOf(dateTokens[0]),
				Integer.valueOf(dateTokens[1]));
		return inputedDate;
	}

}


