
public class Review {
	private String comments;
	private User user;
	private Restaurant restaurant;
	private double rating;
	
	public Review(double rating, String comments, User byUser, Restaurant forRestaurant){
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
