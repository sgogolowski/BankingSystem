/**
 * Controller class for Home.fxml.
 * Contains all the scene objects and appropriate event handler methods.
 * @authors Szymon Gogolowski, Julio Collado
 */
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class HomeController extends Main implements Initializable {

	private String[] commands = {"Open Account", "Close Account", "Withdraw", "Deposit", "Print Statements by Last Name", "Print Statements by Date Opened", "Print Accounts", "Export Database"};
	
	@FXML
    private ComboBox<String> commandComboBox = new ComboBox<String>();
   
    @FXML
    private Button importDB;

  
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		commandComboBox.setItems(FXCollections.observableArrayList(commands));
		commandComboBox.getSelectionModel().clearSelection();
		
	}

    /**
     * showCommand event handler method.
     * Checks the radio button selected and changes the scene based on 
     * the command inputed by the user or handles the event such as import or export database.
     * @param event being fired
     */
    @FXML
    void showCommand(ActionEvent event) throws IOException {
    	
    	if (commandComboBox.getValue().equals("Open Account")) {
    		showOpen(event);
    	}
    	else if (commandComboBox.getValue().equals("Close Account")) {
    		showClose(event);
    	}
    	else if (commandComboBox.getValue().equals("Withdraw")) {
    		showWithdraw(event);
    	}
    	else if (commandComboBox.getValue().equals("Deposit")) {
    		showDeposit(event);
    	}
    	else if (commandComboBox.getValue().equals("Print Statements by Last Name")) {
    		showPN(event);
    	}
    	else if (commandComboBox.getValue().equals("Print Statements by Date Opened")) {
    		showPD(event);
    	}
    	else if (commandComboBox.getValue().equals("Print Accounts")) {
    		showPA(event);
    	}
    	else if (commandComboBox.getValue().equals("Export Database")) {
    		exportDB(event);
    	}
    	
    }
    
    /**
     * showOpen event handler method.
     * Changes the scene to the 'Open.fxml' scene which contains a GUI for opening an account.
     * @param event being fired
     */
    @FXML
    void showOpen(ActionEvent event) throws IOException {
    	Parent open = FXMLLoader.load(getClass().getResource("Open.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }
    
    /** 
     * showClose event handler method.
     * Changes the scene to the 'Close.fxml' scene which contains a GUI for closing an account.
     * @param event being fired
     */
    @FXML
    void showClose(ActionEvent event) throws IOException {
    	Parent open = FXMLLoader.load(getClass().getResource("Close.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }

    /**
     * showDeposit event handler method.
     * Changes the scene to the 'Deposit.fxml' scene which contains a GUI for depositing money
     * into an account.
     * @param event being fired
     */
    @FXML
    void showDeposit(ActionEvent event) throws IOException{
    	Parent open = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }

    /**
     * showWithdraw event handler method.
     * Changes the scene to the 'Withdraw.fxml' scene which contains a GUI for withdrawing money
     * from an account.
     * @param event being fired
     */
    @FXML
    void showWithdraw(ActionEvent event) {

    }
    
    /**
     * showPN event handler method.
     * Changes the scene to the 'PrintByLastName.fxml" scene which contains a GUI that prints all
     * the statements of the accounts by ascending order of last name.
     * @param event being fired
     */
    @FXML
    void showPN(ActionEvent event) {

    }
    
    /**
     * showPD event handler method.
     * Changes the scene to the 'PrintByDateOpen.fxml' scene which contains a GUI that prints all
     * the statements of the accounts by ascending order of date opened.
     * @param event being fired
     */
    @FXML
    void showPD(ActionEvent event) {

    }
    
    /**
     * showPA event handler method.
     * Changes the scene to the 'PrintAccounts.fxml' scene which contains a GUI that prints all
     * the accounts in the database.
     * @param event being fired
     */
    @FXML
    void showPA(ActionEvent event) throws IOException{
    	Parent open = FXMLLoader.load(getClass().getResource("PrintAccounts.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    }
    
    /**
     * importDB event  handler method reads a .txt file containing a list of accounts and
     * adds them to the database.
     * @param event being fired
     */
    @FXML
    void importDB(ActionEvent event) throws FileNotFoundException, IOException {
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Select database to import.");
    	fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
    	Window window = ((Node) event.getSource()).getScene().getWindow();
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
    	Dialog<String> info = new Dialog<String>();
    
    	info.setTitle("Database imported.");
    	ButtonType home = new ButtonType("OK", ButtonData.OK_DONE);
    	info.getDialogPane().getButtonTypes().add(home);
    	info.setContentText(selectedFile.getName() + " was successfully imported.");
    	info.showAndWait();
    	Parent open = FXMLLoader.load(getClass().getResource("Home.fxml"));
    	Scene openPage = new Scene(open);
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(openPage);
    	stage.show();
    	

    		
    }

    @FXML
    void exportDB(ActionEvent event) {
   
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

