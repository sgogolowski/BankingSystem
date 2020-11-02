package application;
/**
 *  Personal implementation of the Date class.
 *  Contains the year, month, and day.
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class Date implements Comparable<Date> {
	private final int JAN = 1, FEB = 2, MAR = 3, APR = 4, MAY = 5, JUN = 6, JUL = 7, AUG = 8, SEP = 9, OCT = 10, NOV = 11, DEC = 12;
	private final int MAX_YEAR = 2020, MIN_YEAR = 1, MIN_DAY = 1, MAX_DAYS_1 = 31, MAX_DAYS_2 = 30, MAX_DAYS_LEAP_YEAR = 29, MAX_DAYS_FEB = 28;
	private int year;
	private int month;
	private int day;
	
	/**
	 * Parameterized Date constructor to initialize Date object with parameterized values
	 */
	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	/**
	 * year accessor method
	 * @return year of the Date object
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * year mutator method
	 * @param year is the new year of the Date object
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * month accessor method
	 * @return month of the Date object
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * month mutator method
	 * @param month is the new month of the Date object
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * day accessor method
	 * @return day of the the Date object
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * day mutator method
	 * @param day is the new day of the Date object
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Method to check if a Date object is valid.
	 * Checks if month is between 1-12, day is between 1-31 depending on the month, 
	 * and if the year is no more than the current.
	 * @return true if Date is valid, otherwise false
	 */
	public boolean isValid() {
		if (this.month > DEC || this.month < JAN)
			return false;
		if (this.year > MAX_YEAR || this.year < MIN_YEAR)
			return  false;
		if (this.day < MIN_DAY)
			return false;
		switch (this.month) {
			case JAN:
			case MAR:
			case MAY:
			case JUL:
			case AUG:
			case OCT:
			case DEC:
				if (this.day > MAX_DAYS_1) 
					return false;
				return true;
			case APR: 
			case JUN:
			case SEP:
			case NOV:
				if (this.day > MAX_DAYS_2)
					return false;
				return true;
			case FEB:
				if (this.year % 4 == 0)
					if (this.day > MAX_DAYS_LEAP_YEAR)
						return false;
					else
						return true;
				if (this.day > MAX_DAYS_FEB)
					return false;
				return true;
			default: 
				return false;
				
		}
	}
	
	/**
	 * compareTo method to compare two Date objects.
	 * Checks each date by year, month, and day.
	 * @return 1 if date is less, -1 if date is greater, or 0 if they are equal
	 */
	@Override
	public int compareTo(Date date) {
		if (this.year < date.getYear())
			return -1;
		else if (this.year > date.getYear())
			return 1;
		else {
			if (this.month < date.getMonth()) 
				return -1;
			else if (this.month > date.getMonth())
				return 1;
			else {
				if (this.day < date.getDay())
					return -1;
				else if (this.day > date.getDay())
					return 1;
				else
					return 0;
			}
		}
	
	}
	
	/**
	 * toString method returns a String representation of the Date object
	 * @return String in the format mm/dd//yyyy 
	 */
	@Override
	public String toString() {
			return month + "/" + day + "/" + year;		
	}
	
	
}
