package main.java;

public class Review {
	String review;
	double rating;
	long restaurantId;
	
	public Review(long id, double rating){
		this.rating = rating;
		this.restaurantId = id;
	}
	
	public void addComments(String text){
		this.review = text;
	}
	
	public void updateReview(double rating, String text){
		if(text != null){
			this.review = text;
		}
		this.rating = rating;
	}
	
	@Override
	public String toString(){
		return this.restaurantId + " " + this.rating + "/5 .Comments: " + this.review;
	}
}
