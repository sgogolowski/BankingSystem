package application;
/**
 *  AccountDatabase is an array-based container class that holds an array of all different instances of accounts.
 *  Array has an intiial capacity of 5 and increases by 5 each time it is full. Contains methods to add accounts,
 *  remove accounts, and print all accounts.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class AccountDatabase {
	
	private Account[] accounts;
	private int size;
	private final int INITIAL_SIZE = 5;
	
	/**
	 * Default AccountDatabase constructor initializes an Account array with size of 5
	 */
	public AccountDatabase() {
		this.accounts = new Account[INITIAL_SIZE];
		this.size = 0;
	}
	
	/**
	 * Parameterized AccountDatabase constructor initializes an AccountDatabase object with
	 * parameterized values
	 */
	public AccountDatabase(Account[] accounts, int size) {
		this.accounts = accounts;
		this.size = size;
	}
	
	/**
	 * size accessor method
	 * @return number of accounts in account array
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * find helper method to find the index of an account in the accounts array.
	 * @param account is the account to look for in the accounts array
	 * @return the index of the account in the accounts array
	 */
	private int find(Account account) {
		for (int i = 0; i < this.size; i ++) {
			if(accounts[i].equals(account))
				return i;
		}
		return -1;
	}
	
	/**
	 * grow helper method to increase the accounts array by 5 each time it gets full.
	 * Creates a new array with the new size and copies the accounts from the old array
	 * into the new array.
	 */
	private void grow() {
		int oldSize = this.accounts.length;
		Account[] newAccounts = new Account[this.accounts.length + INITIAL_SIZE];
		
		for (int i = 0; i < oldSize; i ++) {
			newAccounts[i] = accounts[i];
		}
		
		accounts = newAccounts;
		System.gc();
		
	}
	
	/**
	 * Method to add an account to the accounts array.
	 * Checks if the account already exists in the array and if the array is full and
	 * needs to grow
	 * @param account is the the account that wants to be added
	 * @return true if account was successfully added, otherwise false
	 */
	public boolean add(Account account) {
		//if array is empty then you don't have to check if account already exists
		if (this.size != 0) {
			if (find(account) != -1)
				return false;
		}
		//check if array is full
		if (this.size == this.accounts.length)
			grow();
		//add accounts and increment size
		accounts[size] = account;
		this.size ++;
		return true;
	}
	
	/**
	 * Method to remove an account from the accounts array
	 * Checks if array is empty first then checks if the account to remove is at the
	 * end of the array. Proceed to loop through the accounts array to search for the
	 * account to remove. If found, replace the location of the removed account with
	 * the account at the end of the array.
	 * @param account is the account to remove from the array
	 * @return true if account was successfully removed, false if the account does not exist.
	 */
	public boolean remove(Account account) {
		if (this.size == 0)
			return false;
		else {
			if (accounts[this.size - 1].equals(account)) {
				accounts[this.size - 1] = null;
				this.size --;
				return true;
			}
			else {
				int index = find(account);
				if (index == -1)
					return false;
				accounts[index] = accounts[this.size - 1];
				accounts[this.size - 1] = null;
				this.size --;
				return true;
			}
			
		}	
		
	}
	
	/**
	 * Method to deposit money into an account.
	 * Checks the database for the existing account and credits the account by the amount, otherwise
	 * reports that account was not found and does not exist.
	 * @param account to deposit into
	 * @param amount of money to deposit
	 * @return true if successfully credited account, otherwise false if the account does not exist.
	 */
	public boolean deposit(Account account, double amount) {
		if (find(account) != -1) {
			int index = find(account);
			if (accounts[index].getHolder().equals(account.getHolder())) {
				accounts[index].credit(amount);
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Method to withdraw money from an account.
	 * Checks the database for the existing account, checks if sufficient funds exist, and then
	 * debits the account by the amount.
	 * @param account to withdraw from
	 * @param amount of money to withdraw
	 * @return 1: insufficient funds; 0: withdraw successfull; -1 account does not exist.
	 */
	public int withdrawal(Account account, double amount) {
		if (find(account) == -1)
			return -1;
		int index = find(account);
		if (accounts[index].getBalance() < amount)
			return 1;
		else {
			accounts[index].debit(amount);
			if (accounts[index] instanceof MoneyMarket) {
				MoneyMarket ma = (MoneyMarket) accounts[index];
				ma.incrementWithdrawals();
			}
			return 0;
		}
	}
	
	/**
	 * Helper method to perform a selection sort of the array of accounts by ascending dates.
	 * 
	 */
	private void sortByDateOpen() {
		for (int i = 0; i < this.size - 1; i ++) 
			for (int j = i + 1; j < this.size; j ++) {
				if (accounts[i].getDateOpen().compareTo(accounts[j].getDateOpen()) > 0) {
					Account temp = accounts[i];
					accounts[i] = accounts[j];
					accounts[j] = temp;
				}
			}
	}
	
	/**
	 * Helper method to perform a selection sort of the array of accounts by ascending last names.
	 */
	private void sortByLastName() {
		for (int i = 0; i < this.size - 1; i ++) 
			for (int j = i + 1; j < this.size; j ++) {
				if (accounts[i].getHolder().getLname().compareTo(accounts[j].getHolder().getLname()) > 0) {
					Account temp = accounts[i];
					accounts[i] = accounts[j];
					accounts[j] = temp;
				}
			}
	}
	
	/**
	 * Method to print statements by date open.
	 * Includes: interest, fees, and new balance.
	 */
	public String printByDateOpen() {
		String output;
		output = String.format("\n--Printing statements by date opened--\n");
		if (this.size >= 2)
			sortByDateOpen();
		for (int i = 0; i < this.size; i ++) {
			output += ("\n" + accounts[i]);
			double interest = accounts[i].monthlyInterest();
			double fee = accounts[i].monthlyFee();
			accounts[i].debit(fee);
			accounts[i].credit(interest);
			output += String.format("\n-interest: $ %,.2f\n-fee: $ %,.2f\n-new balance: $ %,.2f\n",
					interest, fee, accounts[i].getBalance());
		}
		output += ("--end of printing--\n");
		return output;
		
	}
	
	/**
	 * Method to print statements by last name.
	 * Includes: interest, fees, and new balance.
	 */
	public String printByLastName() {
		String output;
		output = String.format("\n--Printing statements by last name--\n");
		if (this.size >= 2)
			sortByLastName();
		for (int i = 0; i < this.size; i ++) {
			output += ("\n" + accounts[i]);
			double interest = accounts[i].monthlyInterest();
			double fee = accounts[i].monthlyFee();
			accounts[i].debit(fee);
			accounts[i].credit(interest);
			output += String.format("\n-interest: $ %,.2f\n-fee: $ %,.2f\n-new balance: $ %,.2f\n",
					interest, fee, accounts[i].getBalance());
		}
		output += ("--end of printing--\n");
		return output;
		
	}
	
	/**
	 * Method to print the list of accounts.
	 */
	public String printAccounts() {
		String output;
		output = ("--Listing accounts in the database--");
		for (int i = 0; i < this.size; i ++) {
			output += ("\n" + accounts[i]);
		}
		output += ("\n--end of listing--");
		return output;
	}
}
