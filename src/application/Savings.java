package application;

/**
 *	Data type Savings account.
 *	Encapsulates the loyal account holder data field and the methods to calculate the appropriate monthly
 *	interest and fees. Includes a toString and equals method.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class Savings extends Account {
	private boolean isLoyal;
	private final double ANNUAL_INTEREST_RATE = 0.0025;
	private final double MONTHLY_FEE = 5;
	private final double WAIVE_AMOUNT = 300;
	private final double PROMO_INTEREST_RATE = 0.0035;
	
	
	/**
	 * Default constructor, calls super class and initializes isLoyal to false
	 */
	public Savings() {
		super();
		this.isLoyal = false;
	}
	
	/**
	 * Param Savings constructor, initializes Savings object to param values
	 * @param holder holds profile of the customer
	 * @param balance on the account
	 * @param dateOpen date the account was opened
	 * @param isLoyal checks if customer is loyal
	 */
	public Savings(Profile holder, double balance, Date dateOpen, boolean isLoyal) {
		super(holder, balance, dateOpen);
		this.isLoyal = isLoyal;
	}
	/**
	 * Method from Account class to retrieve the monthly interest rate
	 * @return monthly interest rate
	 */
	public double monthlyInterest() {
		if (isLoyal) {
			return (this.getBalance() * PROMO_INTEREST_RATE) / 12;
		}
		else {
			return (this.getBalance() * ANNUAL_INTEREST_RATE) / 12;
		}
	}
	/**
	 * Method to retrieve the monthly fee depending on the account type
	 * @return monthly fee
	 */
	public double monthlyFee() {
		if (this.getBalance() >= WAIVE_AMOUNT) {
			return 0.0;
		}
		else {
			return MONTHLY_FEE;
		}
	}
	
	/**
	 *	toString method returns a String representation of a Savings object
	 *	@return String in the format "*Savings*first_name last_name *$balance* mm/dd/yyyy *special savings account*
	 */
	@Override
	public String toString() {
		if (isLoyal) {
			return "*Savings" + super.toString() + "*special Savings account*";
		}
		else {
			return "*Savings" + super.toString();
		}
	}
	
	/**
	 * equals method to check if two savings accounts are equal
	 * @return true if equal, false if not equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Savings) {
			Savings equalsObj  = (Savings) obj;
			if (this.getHolder().equals(equalsObj.getHolder()))
				return true;
			else
				return false;
		}
		else 
			return false;
	}
}
