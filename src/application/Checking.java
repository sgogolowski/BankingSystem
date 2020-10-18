package application;

/**
 *	Data type Checking account.
 *	Encapsulates the direct deposit data field and the methods to calculate the appropriate monthly
 *	interest and fees. Includes a toString and equals method.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class Checking extends Account{
	
	private boolean directDeposit;
	private final double ANNUAL_INTEREST_RATE = 0.0005;
	private final double MONTHLY_FEE = 25;
	private final double WAIVE_AMOUNT = 1500;
	
	/**
	 * Default constructor, calls super class  and initializes directDeposit to false
	 */
	public Checking() {
		super();
		this.directDeposit = false;
	}
	
	/**
	 * Param Savings constructor, initializes Checking object to param values
	 * @param holder holds profile of the customer
	 * @param balance on the account
	 * @param dateOpen date the account was opened
	 * @param directDeposit checks if a direct deposit was made or not
	 */
	public Checking(Profile holder, double balance, Date dateOpen, boolean directDeposit) {
		super(holder, balance, dateOpen);
		this.directDeposit = directDeposit;
	}
	
	/**
	 * Method from Account class to retrieve the monthly interest rate
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
		if (this.getBalance() >= WAIVE_AMOUNT || directDeposit)
			return 0.0;
		else
			return MONTHLY_FEE;
	}
	/**
	 *	toString method returns a String representation of a Checking object
	 *	@return String in the format "*Checking*first_name last_name *$balance* mm/dd/yyyy *direct deposit account*
	 */
	@Override
	public String toString() {
		if (directDeposit)
			return "*Checking" + super.toString() + "*direct deposit account*";
		else
			return "*Checking" + super.toString();
	}
	
	/**
	 * equals method to check if two checking accounts are equal
	 * @return true if equal, false if not equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Checking) {
			Checking equalsObj  = (Checking) obj;
			if (this.getHolder().equals(equalsObj.getHolder()))
				return true;
			else
				return false;
		}
		else 
			return false;
	}
}