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
	String name, password;
	boolean admin;
	int age = 0;
	long creationDate;
	HashMap<Long, Review> reviews = new HashMap<Long, Review>();
	
	public User(String name, String pw, boolean admin){
		this.name = name;
		this.password = pw;
		this.admin = admin;
		ZonedDateTime startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		this.creationDate = startOfToday.toEpochSecond() * 1000;
	}
	
	public void formatBirthdate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try{
			System.out.println("Entered date: " + date);
			Date d = formatter.parse(date);
		    Calendar c = Calendar.getInstance();
		    c.setTime(d);
		    long time = c.getTimeInMillis();
			System.out.println("utc: " + time);
		}
		catch(Exception ex){
			System.out.println("Error! Exception message: " + ex);
		}
		
	}
	
	public void getAccountCreationDate(){
		Date time = new Date(this.creationDate);
		System.out.println(time);
	}
	public void addAge(int age){
		this.age = age;
	}
	
	public void printInfo(){
		System.out.println("Admin username: " + this.name);
		System.out.println("Admin age: " + this.age);
	}
	
	public void addReview(long restaurantId, Review review){
		// review = new Review(restaurantId, rating);
		System.out.println(restaurantId);
		//System.out.println(review.restaurantId + " " + review.rating);
		this.reviews.put(restaurantId, review);
	}
	
	public void getReviews(long restaurantId){
		System.out.println(this.reviews.get(restaurantId));
	}
}
