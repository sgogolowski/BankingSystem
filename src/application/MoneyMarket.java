package application;

/**
 *	Data type MoneyMarket account.
 *	Encapsulates the withdrawals data field and the methods to calculate the appropriate monthly
 *	interest and fees. Includes a toString and equals method.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class MoneyMarket extends Account {
	private int withdrawals;
	private final double ANNUAL_INTEREST_RATE = 0.0065;
	private final double MONTHLY_FEE = 12;
	private final double WAIVE_AMOUNT = 2500;
	private final int WITHDRAWALS_WAIVER = 6;
	
	/**
	 * Default constructor, calls super class and initializes withdrawals to 0
	 */
	public MoneyMarket() {
		super();
		this.withdrawals = 0;
	}
	
	/**
	 * Param Savings constructor, initializes MoneyMarket object to param values
	 * @param holder holds profile of the customer
	 * @param balance on the account
	 * @param dateOpen date the account was opened
	 * @param isLoyal checks if customer is loyal
	 */
	public MoneyMarket(Profile holder, double balance, Date dateOpen, int withdrawals) {
		super(holder, balance, dateOpen);
		this.withdrawals = withdrawals;
	}
	
	/**
	 * Accessor method for the number of withdrawals in money market.
	 * @return number of withdrawals.
	 */
	public int getWithdrawals() {
		return this.withdrawals;
	}
	
	/**
	 * Method to retrieve the monthly interest depending on the account type
	 * @return monthly interest rate
	 */
	@Override
	public double monthlyInterest() {
		return (this.getBalance() * ANNUAL_INTEREST_RATE) / 12;
	}
	/**
	 * Method to retrieve the monthly fee depending on the account type
	 * @return monthly fee
	 */
	@Override
	public double monthlyFee() {
		if (this.getBalance() >= WAIVE_AMOUNT && withdrawals <= WITHDRAWALS_WAIVER) {
			return 0.0;
		}
		else {
			return MONTHLY_FEE;
		}
	}
	
	/**
	 * Method to increment withdrawals by 1 each time a withdrawal is made
	 */
	public void incrementWithdrawals() {
		this.withdrawals ++;
	}
	
	/**
	 *	toString method returns a String representation of a Savings object
	 *	@return String in the format "*Money Market*first_name last_name *$balance* mm/dd/yyyy *x withdrawal(s)*
	 */
	@Override
	public String toString() {
		if (withdrawals == 1)
			return String.format("*Money Market%s *%d withdrawal*",super.toString(), withdrawals);
		else
			return String.format("*Money Market%s *%d withdrawals*",super.toString(), withdrawals);
		
		}
	
	/**
	 * equals method to check if two money market accounts are equal
	 * @return true if equal, false if not equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof MoneyMarket) {
			MoneyMarket equalsObj  = (MoneyMarket) obj;
			if (this.getHolder().equals(equalsObj.getHolder()))
				return true;
			else
				return false;
		}
		else 
			return false;
	}
	}
