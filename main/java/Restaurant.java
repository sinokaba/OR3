
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
	String name, phone, address, city, state, zip;
	private int restaurantId;
	int numReviews = 0;
	double totalRating = 0;
	private int minPrice = 0;
	private int maxPrice = 0;
	private ArrayList<String> tags;
	HashMap<User, Review> reviews = new HashMap<User, Review>();

	public Restaurant(String name, String phone){
		this.name = name;
		this.phone = phone;
		tags = new ArrayList<String>();
	}
	
	public void addLocation(String state, String city, String zip){
		this.state = state;
		this.city = city;
		this.zip = zip;
	}
	
	public void setNumReviews(int num){
		numReviews = num;
	}
	
	public void setTotalRating(double totalRating){
		this.totalRating = totalRating;
	}
	
	public void resetReviews(){
		numReviews = 0;
		totalRating = 0;
	}
	
	public void setId(int id){
		System.out.println("setting id of restaurant to: " + id);
		restaurantId = id;
	}
	
	public void setAddress(String address, String zip){
		this.address = address;
		if(zip != null){
			this.zip =zip;
		}
		//this.zip = zip;
	}
	
	public void addRating(double rating){
		totalRating += rating;
		numReviews += 1;
	}
	
	public double getRating(){
		if(totalRating == 0){
			return 0;
		}
		//DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
		return Math.round((totalRating/numReviews)*100.0)/100.0;
	}
	
	public void addReview(User user, Review review){
		numReviews += 1;
		totalRating += review.getRating();
		reviews.put(user, review);
	}
	
	public void updateReview(User user, Review old, Review updated){
		reviews.replace(user, old, updated);
		totalRating += (-old.getRating() + updated.getRating());
	}
	
	public void removeReview(User user, Review review){
		reviews.remove(user, review);
		numReviews -= 1;
		totalRating -= review.getRating();
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
	
	public String getLocLevels(){
		return city + ", " + state + " " + zip;
	}
	
	public void setPriceRange(int min, int max){
		minPrice = min;
		maxPrice = max;
	}
	
	public String getPriceRange(){
		return String.valueOf(minPrice) + " - " + String.valueOf(maxPrice);
	}
	
	public String getPhone(){
		if(phone.length() == 10){
			return phone.substring(0,3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
		}
		return phone;
	}
	
	public String getzip(){
		return zip;
	}
	
	public int getId(){
		System.out.println("rst id: " + restaurantId);
		return restaurantId;
	}
	
	public void addTag(String tag){
		if(tags.size() < 3){
			tags.add(tag);
		}
	}
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
}
