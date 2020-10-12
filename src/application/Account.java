package application;

/**
 *  Abstract Account class.
 *  Defines the common features of all account types such as debit and credit.
 *  Includes a toString and equals method.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public abstract class Account {

	private Profile holder;
	private double balance;
	private Date dateOpen;
	
	/**
	 * Default Account constructor. Initializes holder to null, balance to 0 and dateOpen to null.
	 */
	public Account() {
		holder = null;
		balance = 0.0;
		dateOpen = null;
	}
	
	/**
	 * Parameterized Account constructor initializes Account object to parameterized values
	 * @param holder the Profile of account holder
	 * @param balance on the account
	 * @param dateOpen date account was opened
	 */
	public Account(Profile holder, double balance, Date dateOpen) {
		this.holder = holder;
		this.balance = balance;
		this.dateOpen = dateOpen;
	}
	
	/**
	 * holder accessor method
	 * @return reference to Profile instance, holder
	 */
	public Profile getHolder() {
		return holder;
	}

	/**
	 * holder mutator method
	 * @param holder new reference value of holder
	 */
	public void setHolder(Profile holder) {
		this.holder = holder;
	}

	/**
	 * balance accessor method
	 * @return balance on the account
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * balance mutator method
	 * @param balance is the amount of money on the account
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * dateOpen accessor method
	 * @return reference to Date instance, dateOpen
	 */
	public Date getDateOpen() {
		return dateOpen;
	}

	/**
	 * dateOpen mutator method
	 * @param dateOpen new reference value of dateOpen
	 */
	public void setDateOpen(Date dateOpen) {
		this.dateOpen = dateOpen;
	}
	
	/**
	 * Method to debit money to the account's balance
	 * @param amount to debit to the balance
	 */
	public void debit(double amount) {
		this.balance -= amount;
	}
	
	/**
	 * Method to credit money from the account's balance
	 * @param amount to credit from the account
	 */
	public void credit(double amount) {
		this.balance += amount;
	}
	
	/**
	 * Abstract method to retrieve the monthly interest depending on the account type
	 * @return monthly interest rate
	 */
	public abstract double monthlyInterest();
	
	/**
	 * Abstract method to retrieve the monthly fee depending on the account type
	 * @return monthly fee
	 */
	public abstract double monthlyFee();
	
	/**
	 *	toString method returns a String representation of an Account object
	 *	@return String in the format "* first_name last_name *$balance* mm/dd/yyyy
	 */
	@Override
	public String toString() {
		String output = String.format("*%s* $%,.2f*%s", holder, balance, dateOpen);
		return output;
	}
	
	/**
	 * equals method to compare 2 instances of an Account object.
	 * Checks if accounts have the same Profile, balance, and dateOpen
	 * @return true if accounts are equals, otherwise false
	 */
	@Override
	public boolean equals(Object account) {
		if (account instanceof Account) {
			Account equalsAccount = (Account) account;
			if (equalsAccount.holder.equals(this.holder))
				if (equalsAccount.balance == this.balance)
					if (equalsAccount.dateOpen.compareTo(this.dateOpen) == 0)
						return true;
					else
						return false;
				else
					return false;
			else
				return false;
		}
		else
			return false;
	}
}
