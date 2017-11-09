import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Review {
	private String comments;
	private User user;
	private Restaurant restaurant;
	private Date date;
	private double rating;
	
	@SuppressWarnings("deprecation")
	public Review(double rating, String comments, User byUser, Restaurant forRestaurant){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date dateNow = new Date();
		date = new Date(dateFormat.format(dateNow));
		this.rating = rating;
		user = byUser;
		restaurant = forRestaurant;
		if(comments != null){
			this.comments = comments;
		}
	}
	
	public String getComments(){
		return comments;
	}
	
	public double getRating(){
		return rating;
	}
	
	public int getRestaurantId(){
		return restaurant.getId();
	}
	
	public int getUserId(){
		return user.getId();
	}
	
	public String getRestaurantName(){
		return restaurant.getName();
	}
	
	public String getUserName(){
		return user.getUsername();
	}
	
	public Date getCreationDate(){
		return date;
	}
	
	public void setCreationDate(Date creation){
		date = creation;
	}
	public void updateReview(double rating, String text){
		if(text != null){
			comments = text;
		}
		if(rating > 0){
			this.rating = rating;
		}
	}
	
	@Override
	public String toString(){
		return "Rating " + rating + "/5 .Comments: " + comments;
	}
}
