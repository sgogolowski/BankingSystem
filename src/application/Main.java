package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


public class Main extends Application {
	
	protected AccountDatabase db = new AccountDatabase();
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("TransactionManager.fxml"));
			Scene scene = new Scene(root, 500, 410);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Transaction Manager");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
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
    
    @FXML
    void disableSM(ActionEvent event) {
    	DirectDepositCheckbox.setDisable(false);
    	LoyalCheckbox.setDisable(true);
    	LoyalCheckbox.setSelected(false);
    	Withdrawals.setDisable(true);
    	Withdrawals.setText("");
    }

    @FXML
    void disableCM(ActionEvent event) {
    	LoyalCheckbox.setDisable(false);
    	DirectDepositCheckbox.setDisable(true);
    	DirectDepositCheckbox.setSelected(false);
    	Withdrawals.setDisable(true);
    	Withdrawals.setText("");
    }

    @FXML
    void disableCS(ActionEvent event) {
    	Withdrawals.setDisable(false);
    	DirectDepositCheckbox.setDisable(true);
    	DirectDepositCheckbox.setSelected(false);
    	LoyalCheckbox.setDisable(true);
    	LoyalCheckbox.setSelected(false);
    }

    @FXML
    void openAccount(ActionEvent event) {

    	try {
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
	    	if (AccountTypeOpen.getSelectedToggle().equals(checkingRadioOpen)) {
	    		boolean direct_deposit = DirectDepositCheckbox.isSelected();
	    		if (db.add(new Checking(new Profile(first_name, last_name), balance_amount, date, direct_deposit)))
	    			openOutput.setText(openOutput.getText() + "Account opened and added to the database.\n");
	    		else
	    			openOutput.setText(openOutput.getText() + "Account is already in the database.\n");
	    		System.out.println(db.printAccounts());
	    	}
	    	else if (AccountTypeOpen.getSelectedToggle().equals(savingsRadioOpen)) {
	    		boolean loyal = LoyalCheckbox.isSelected();
	    		if (db.add(new Savings(new Profile(first_name, last_name), balance_amount, date, loyal)))
	    			openOutput.setText(openOutput.getText() + "Account opened and added to the database.\n");
	    		else
	    			openOutput.setText(openOutput.getText() + "Account is already in the database.\n");
	    	}
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
    	catch (NumberFormatException nfe) {
    		openOutput.setText(openOutput.getText() + "Input mismatch.\n");		
    	}
    	catch (InputMismatchException ime) {
    		openOutput.setText(openOutput.getText() + ime.getMessage() +"\n");
    	}
    	catch (NullPointerException npe) {
    		openOutput.setText(openOutput.getText() + "Input mismatch.\n");
    	}
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
     * closeAccount event handler method.
     * Extracts all the user input from the scene controls and attempt to remove
     * the Account from the database. Afterwards, a dialog box appears with the
     * result of the attempted account removal.
     * @param event being fired
     */
    @FXML
    void closeAccount(ActionEvent event) throws IOException {
    	try {
	    	String first_name = fnameClose.getText();
	    	String last_name = lnameClose.getText();
	    	if (AccountTypeClose.getSelectedToggle().equals(checkingRadioClose)) {
	    		if (db.remove(new Checking(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	else if (AccountTypeClose.getSelectedToggle().equals(savingsRadioClose)) {
	    		if (db.remove(new Savings(new Profile(first_name, last_name), 0, null, true)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	else if (AccountTypeClose.getSelectedToggle().equals(moneymarketRadioClose)) {
	    		if (db.remove(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0)))
	    			closeOutput.setText(closeOutput.getText() + "Account closed and removed from the database.\n");
	    		else
	    			closeOutput.setText(closeOutput.getText() + "Account does not exist.\n");
	    	}
	    	
    	}
    	catch (NullPointerException npe) {
    		closeOutput.setText(closeOutput.getText() + "Input mismatch.\n");
    	}
    	finally {
    		fnameClose.setText("");
    		lnameClose.setText("");
    		AccountTypeClose.getSelectedToggle().setSelected(false);
    	}
    	
    }

    @FXML
    void deposit(ActionEvent event) throws IOException, NullPointerException {
    	try {
	    	double amount = Double.valueOf(balanceDW.getText());
	    	if (amount < 0)
	    		throw new InputMismatchException("Input data type mismatch.");
	    	String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW))
	    		if (db.deposit(new Checking(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW))
	    		if (db.deposit(new Savings(new Profile(first_name, last_name), 0, null, true), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW))
	    		if (db.deposit(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount))
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " deposited to account.\n");
	    		else
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	
	
    	}
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Missing input.\n");
    	}
    	finally{
    		fnameDW.setText("");
    		lnameDW.setText("");
    		balanceDW.setText("");
    		AccountTypeDW.getSelectedToggle().setSelected(false);
    	}
    }

    @FXML
    void withdraw(ActionEvent event) {
    	try {
	    	double amount = Double.valueOf(balanceDW.getText());
	    	if (amount < 0)
	    		throw new InputMismatchException("Input data type mismatch.");
	    	String first_name = fnameDW.getText();
	    	String last_name = lnameDW.getText();
	    	
	    	if (AccountTypeDW.getSelectedToggle().equals(checkingRadioDW)) {
	    		if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new Checking(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	else if (AccountTypeDW.getSelectedToggle().equals(savingsRadioDW)) {
	    		if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new Savings(new Profile(first_name, last_name), 0, null, true), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	else if (AccountTypeDW.getSelectedToggle().equals(moneymarketRadioDW)) {
	    		if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 0)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + amount + " withdrawn from account.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == 1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Insufficient funds.\n");
	    		else if (db.withdrawal(new MoneyMarket(new Profile(first_name, last_name), 0, null, 0), amount) == -1)
	    			deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Account does not exist.\n");
	    	}
	    	else
	    		System.out.println("fail.\n");
	    	
	
    	}
    	catch (NumberFormatException nfe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (InputMismatchException ime) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Input data type mismatch.\n");
    	}
    	catch (NullPointerException npe) {
    		deposit_withdraw_output.setText(deposit_withdraw_output.getText() + "Missing input.\n");
    	}
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
	    	//Window window = event.getSource().getScene().getWindow();
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
	    				db.add(new Checking(new Profile(fname, lname), balance, date, directDeposit));
	    			}
	    			else if (parse[0].equals("S")) {
	    				boolean loyal;
	    				if (parse[5].toUpperCase().equals("TRUE"))
	    					loyal = true;
	    				else if (parse[5].toUpperCase().equals("FALSE"));
	    					loyal = false;
	    				db.add(new Savings(new Profile(fname, lname), balance, date, loyal));
	    			}
	    			else if (parse[0].equals("M")) {
	    				int withdrawals = Integer.valueOf(parse[5]);
	    				db.add(new MoneyMarket(new Profile(fname, lname), balance, date, withdrawals));
	    			}
	    		}
	    		fileReader.close();
	    	}
	    	databaseOutput.setText("");
	    	databaseOutput.setText(selectedFile.getName() + " was successfully imported.\n");
    	}
    	catch (NullPointerException npe) {
    		databaseOutput.setText("");
    		databaseOutput.setText("No file was selected, try again.\n");
    	}
	    	

    		
    }

    @FXML
    void exportDB(ActionEvent event) {

    }

    @FXML
    void showPN(ActionEvent event) {

    }

    @FXML
    void showPD(ActionEvent event) {

    }
    
    @FXML
    void printAccounts(ActionEvent event) {
    	databaseOutput.setText("");
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
    
    private Date parseDate(String input) {
		String[] dateTokens = input.split("/");
		if(dateTokens == null || dateTokens.length != 3)
			return null;
		
		Date inputedDate = new Date(Integer.valueOf(dateTokens[2]), Integer.valueOf(dateTokens[0]),
				Integer.valueOf(dateTokens[1]));
		return inputedDate;
	}

}
