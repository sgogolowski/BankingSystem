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
    private TextField month, year, fnameClose, balanceDW, day, Withdrawals, balanceOpen, lnameDW, fnameOpen, lnameOpen, lnameClose, fnameDW;

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
    	Withdrawals.setDisable(true);
    	Withdrawals.setText("");
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
    	Withdrawals.setDisable(true);
    	Withdrawals.setText("");
    }
    
    /**
     * Method to enables the withdrawals textfield and disable the the loyal customer checkbox and direct deposit checkbox.
     * @param event being handled
     */
    @FXML
    void disableCS(ActionEvent event) {
    	Withdrawals.setDisable(false);
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
	    	double balance_amount = Double.valueOf(balanceOpen.getText());
	    	if (balance_amount < 0)
	    		throw new NumberFormatException("Input mismatch, try again.");
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
	    			openOutput.setText(openOutput.getText() + "Account opened and added to the database.\n");
	    		else
	    			openOutput.setText(openOutput.getText() + "Account is already in the database.\n");
	    		System.out.println(db.printAccounts());
	    	}
	    	//savings account
	    	else if (AccountTypeOpen.getSelectedToggle().equals(savingsRadioOpen)) {
	    		boolean loyal = LoyalCheckbox.isSelected();
	    		if (db.add(new Savings(new Profile(first_name, last_name), balance_amount, date, loyal)))
	    			openOutput.setText(openOutput.getText() + "Account opened and added to the database.\n");
	    		else
	    			openOutput.setText(openOutput.getText() + "Account is already in the database.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeOpen.getSelectedToggle().equals(moneymarketRadioOpen)) {
	    		int num_withdrawals = Integer.valueOf(Withdrawals.getText());
	    		if (num_withdrawals < 0)
	    			throw new InputMismatchException();
	    		if (db.add(new MoneyMarket(new Profile(first_name, last_name), balance_amount, date, num_withdrawals)))
	    			openOutput.setText(openOutput.getText() + "Account opened and added to the database.\n");
	    		else
	    			openOutput.setText(openOutput.getText() + "Account is already in the database.\n");
	    	}
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		openOutput.setText(openOutput.getText() + "Input mismatch.\n");		
    	}
    	catch (InputMismatchException ime) {
    		openOutput.setText(openOutput.getText() + ime.getMessage() +"\n");
    	}
    	catch (NullPointerException npe) {
    		openOutput.setText(openOutput.getText() + "Input mismatch.\n");
    	}
    	//clear all inputs on the GUI
    	finally {
    		fnameOpen.setText("");
    		lnameOpen.setText("");
    		balanceOpen.setText("");
    		month.setText("");
    		day.setText("");
    		year.setText("");
    		AccountTypeOpen.getSelectedToggle().setSelected(false);
    		DirectDepositCheckbox.setDisable(false);
    		LoyalCheckbox.setDisable(false);
    		Withdrawals.setDisable(false);
    		DirectDepositCheckbox.setSelected(false);
    		LoyalCheckbox.setSelected(false);
    		Withdrawals.setText("");
    		
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
	    	//checking account
	    	if (AccountTypeClose.getSelectedToggle().equals(checkingRadioClose)) {
	    		if (db.remove(new Checking(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	//savings account
	    	else if (AccountTypeClose.getSelectedToggle().equals(savingsRadioClose)) {
	    		if (db.remove(new Savings(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeClose.getSelectedToggle().equals(moneymarketRadioClose)) {
	    		if (db.remove(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	
    	}
    	//error-handling for missing inputs
    	catch (NullPointerException npe) {
    		closeOutput.setText(closeOutput.getText() + "Missing input, try again.\n");
    	}
    	//clear user input
    	finally {		
    		fnameClose.setText("");
    		lnameClose.setText("");
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
	    	double amount = Double.valueOf(balanceDW.getText());
	    	if (amount < 0)
	    		throw new InputMismatchException("Input data type mismatch.");
	    	String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	
	    	//checking account
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW))
	    		if (db.deposit(new Checking(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	//savings account
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW))
	    		if (db.deposit(new Savings(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	//moneymarket account
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW))
	    		if (db.deposit(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	
	
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Missing input.\n");
    	}
    	//clear user input
    	finally{
    		fnameDW.setText("");
    		lnameDW.setText("");
    		balanceDW.setText("");
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
	    	double amount = Double.valueOf(balanceDW.getText());
	    	if (amount < 0)
	    		throw new InputMismatchException("Input data type mismatch.");
	    	String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	//checking account
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW)) {
	    		if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	//savings account
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW)) {
	    		if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	//moneymarket account
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW)) {
	    		if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	
	
    	}
    	//error-handling
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Missing input.\n");
    	}
    	//clear user input
    	finally{
    		fnameDW.setText("");
    		lnameDW.setText("");
    		balanceDW.setText("");
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
	    		while (fileReader.hasNextLine()) {
	    			String line = fileReader.nextLine();
	    			String[] parse = line.split(",");
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
	    					databaseOutput.setText(databaseOutput.getText() + "Account opened and added to the database.\n");
	    				else
	    					databaseOutput.setText(databaseOutput.getText() + "Account is already in the database.\n");
	    			}
	    			else if (parse[0].equals("S")) {
	    				boolean loyal;
	    				if (parse[5].toUpperCase().equals("TRUE"))
	    					loyal = true;
	    				else if (parse[5].toUpperCase().equals("FALSE"));
	    					loyal = false;
	    				if (db.add(new Savings(new Profile(fname, lname), balance, date, loyal)))
	    					databaseOutput.setText(databaseOutput.getText() + "Account opened and added to the database.\n");
	    				else
	    					databaseOutput.setText(databaseOutput.getText() + "Account is already in the database.\n");
	    			}
	    			else if (parse[0].equals("M")) {
	    				int withdrawals = Integer.valueOf(parse[5]);
	    				if (db.add(new MoneyMarket(new Profile(fname, lname), balance, date, withdrawals)))
	    					databaseOutput.setText(databaseOutput.getText() + "Account opened and added to the database.\n");
	    				else
	    					databaseOutput.setText(databaseOutput.getText() + "Account is already in the database.\n");
	    			}
	    		}
	    		fileReader.close();
	    	}
	    	databaseOutput.setText(databaseOutput.getText() + selectedFile.getName() + " was successfully imported.\n");
    	}
    	catch (NullPointerException npe) {
    		databaseOutput.setText("No file was selected, try again.\n");
    	}
	    	

    		
    }

    @FXML
    void exportDB(ActionEvent event) throws IOException{
    	try {
	    	String accountType, fname, lname, balance, dateOpened, uniqueField;
	    	FileChooser fc = new FileChooser();
	    	fc.setTitle("Select text file to export database into.");
	    	fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
	    	Window window = tabPane.getScene().getWindow();
	    	File selectedFile = fc.showSaveDialog(window);
	    	FileWriter myWriter = new FileWriter(selectedFile);
	    	int counter = 1;
	    	if (selectedFile != null) {
	    		Account[] accounts = db.getAccounts();
	    		
	    		for (Account ac : accounts) {
	    			if (ac == null) {
	    				break;
	    			}
	    			System.out.println(counter);
	    			System.out.println(ac.getClass());
	    			if (ac instanceof Checking) {
	    				//System.out.println("1");
	    				accountType = "C";
	    				uniqueField = String.valueOf(((Checking) ac).getDirectDeposit());
	    			}
	    			else if (ac instanceof Savings) {
	    				//System.out.println("2");
	    				accountType = "S";
	    				uniqueField = String.valueOf(((Savings) ac).isLoyal());
	    			}
	    			else if (ac instanceof MoneyMarket) {
	    				//System.out.println("3");
	    				accountType = "M";
	    				uniqueField = String.valueOf(((MoneyMarket) ac).getWithdrawals());
	    			}
	    			else
	    				throw new InputMismatchException();
	    			fname = ac.getHolder().getFname();
	    			lname = ac.getHolder().getLname();
	    			balance = String.valueOf(ac.getBalance());
	    			dateOpened  = ac.getDateOpen().toString();
	    			
	    			myWriter.write(accountType + "," + fname + "," + lname + "," + balance + "," + dateOpened + "," + uniqueField + "\n");
	    			counter++;
	    		}
	    	}
	    	databaseOutput.setText(databaseOutput.getText() + "Database was successfully exported into " + selectedFile.getName() + ".\n");
	    	myWriter.close();
    	}
    	catch (IOException ioe) {
    		databaseOutput.setText(databaseOutput.getText() + "Unable to find file.\n");		
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
    		databaseOutput.setText("Database is empty.");
    	else
    		databaseOutput.setText(db.printByLastName());
    }
    
    /**
     * Event handler to print account statement by date opened into textarea.
     * Checks if database is empty, otherwise calls the printByDateOpen().
     * @param event being handled
     */
    @FXML
    void showPD(ActionEvent event) {
    	if (db.getSize() == 0)
    		databaseOutput.setText("Database is empty.");
    	else
    		databaseOutput.setText(db.printByDateOpen());
    }
    
    /**
     * Event handler to print accounts into textarea.
     * Checks if database is empty, otherwise calls the printAccounts().
     * @param event being handled
     */
    @FXML
    void printAccounts(ActionEvent event) {
    	if (db.getSize() == 0)
    		databaseOutput.setText("Database is empty.");
    	else
    		databaseOutput.setText(db.printAccounts());
    }
    
    @FXML
    void clearDW(ActionEvent event) {
    	deposit_withdraw_output.setText("");
    }
    
    @FXML
    void clearClose(ActionEvent event) {
    	closeOutput.setText("");
    }
    
    @FXML
    void clearOpen(ActionEvent event) {
    	openOutput.setText("");
    }
    
    @FXML
    void clearDatabase(ActionEvent event) {
    	databaseOutput.setText("");
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


