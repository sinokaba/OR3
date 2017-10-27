<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

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
	private String name, password, email, zipcode, birthday;
	private String status = "offline";
	private int age, privilege;
	private HashMap<Long, Review> reviews = new HashMap<Long, Review>();

	/**
	* The User class constructor
	* 
	* @param takes 4 inputs, 3 string variables name, pw, birthdate, and a boolean value that specifies admin privileges
	* @return no return value
	*/
	public User(String username, String pw, String birthdate, String email, String zipcode, int privilege){
		this.name = username;
		this.email = email;
		this.password = pw;
		this.privilege = privilege;
		this.zipcode = zipcode;
		this.birthday = birthdate; 
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
	
	public void updateEmail(String newEmail){
		email = newEmail;
	}
	
	public void updatePassword(String newPass){
		password = newPass;
	}
	
	public String getUsername(){
		return name;
	}
	
	public String getZipcode(){
		return zipcode;
	}
	
	public String getBday(){
		return birthday;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void loggedIn(){
		status = "online";
		System.out.println("Logged in.");
	}
	
	public void loggedOff(){
		status = "offline";
	}
	
	public int getPrivilege(){
		return privilege;
	}
	
	public String getStatus(){
		return status;
	}
}
