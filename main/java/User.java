package main.java;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class User {
	/**
	* Variables associated with this class declared
	*/
	String name, password;
	boolean admin;
	int age = 0;
	long creationDate, birthdate;
	HashMap<Long, Review> reviews = new HashMap<Long, Review>();

	/**
	* The User class constructor
	* 
	* @param takes 4 inputs, 3 string variables name, pw, birthdate, and a boolean value that specifies admin privileges
	* @return no return value
	*/
	public User(String name, String pw, String birthdate, boolean admin){
		this.name = name;
		this.password = pw;
		this.admin = admin;
		ZonedDateTime startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		this.creationDate = startOfToday.toEpochSecond() * 1000;
		formatBirthdate(birthdate);
	}
	
	/**
	* This method transforms the birthdate of the user to milliseconds to better store it
	*
	* @param takes an input string date of the form "Month-day-year", which is the bday of the user
	* @return will not return anything
	*/
	public void formatBirthdate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try{
			System.out.println("Entered date: " + date);
			Date d = formatter.parse(date);
		    Calendar c = Calendar.getInstance();
		    c.setTime(d);
		    this.birthdate = c.getTimeInMillis();
			//System.out.println("utc: " + time);
		}
		catch(Exception ex){
			System.out.println("Error! Exception message: " + ex);
		}
	}
	
	/**
	* This method prints the time in milliseconds when the user's account was created
	*
	* @param takes no input
	* @return not return anything
	*/	
	public void getAccountCreationDate(){
		Date time = new Date(this.creationDate);
		System.out.println(time);
	}
	
	/**
	* This method prints the current information of user, mainly for testing purposes
	*
	* @param takes no input
	* @return will not return anything
	*/
	public void printInfo(){
		System.out.println("Admin username: " + this.name);
		System.out.println("Admin age: " + this.age);
	}
	
	/**
	* This method adds a review that the user made to the user's list of reviews
	*
	* @param takes 2 arguments, a restaurant id of type long, and a review of class review
	* @return will not return anything
	*/
	public void addReview(long restaurantId, Review review){
		// review = new Review(restaurantId, rating);
		System.out.println(restaurantId);
		//System.out.println(review.restaurantId + " " + review.rating);
		this.reviews.put(restaurantId, review);
	}
	
	/**
	* This method prints the review that the user made that is associated witht he specified restaurant
	*
	* @param takes one input argument restaurantId of the type long
	* @return will not return anything
	*/
	public void getReviews(long restaurantId){
		System.out.println(this.reviews.get(restaurantId));
	}
	
	public void addRestaurantLike(){
		
	}
	
	public void changeEmail(){
		
	}
	
	public void addEmail(){
		
	}
}
