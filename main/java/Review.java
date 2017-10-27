
public class Review {
	String review;
	double rating;
	
	public Review(double rating){
		this.rating = rating;
	}
	
	public void addComments(String text){
		review = text;
	}
	
	public String getComments(){
		return review;
	}
	
	public double getRating(){
		return rating;
	}
	
	public void updateReview(double rating, String text){
		if(text != null){
			review = text;
		}
		this.rating = rating;
	}
	
	@Override
	public String toString(){
		return "Rating " + rating + "/5 .Comments: " + review;
	}
}
