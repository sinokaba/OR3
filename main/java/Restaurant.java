
import java.text.DecimalFormat;
import java.util.HashMap;

public class Restaurant {
	String name, phone, address, city, state, zip;
	double rating = 0;
	int ratingCount = 0;
	int restaurantId;
	HashMap<User, Review> reviews = new HashMap<User, Review>();

	public Restaurant(String name, String phone){
		this.name = name;
		this.phone = phone;
	}
	
	public void setId(int id){
		System.out.println("setting id of restaurant to: " + id);
		restaurantId = id;
	}
	
	public void setAddress(String address, String zip){
		this.address = address;
		this.zip = zip;
	}
	
	public void addRating(double rating){
		this.rating += rating;
		ratingCount += 1;
	}
	
	public double getRating(){
		if(ratingCount == 0){
			return 0;
		}
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
		return  Double.valueOf(oneDigit.format(rating/ratingCount));
	}
	
	public void setReview(User user, Review review){
		reviews.put(user, review);
	}
	
	public Review getUserReview(User user){
		return reviews.get(user);
	}
	
	public String getName(){
		return name;
	}

	public String getAddress(){
		return address;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getzip(){
		return zip;
	}
	
	public int getId(){
		System.out.println("rst id: " + restaurantId);
		return restaurantId;
	}
	
}
