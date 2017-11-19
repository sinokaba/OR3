import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class RestaurantUI {
	Button addReviewBtn;
	CustomTextField commentField;
	DropdownMenu rating;
	DBConnection db;
	BorderPane layout;
	//Rating rating;
	
	public RestaurantUI(DBConnection db){
		layout = new BorderPane();
		this.db = db;
	}
	
	public void buildPage(Restaurant rst, User user, List<Review> reviews){
		//ColumnConstraints constraints = new ColumnConstraints();
		//constraints.setPercentWidth(100/2);
		//layout.getColumnConstraints().add(constraints);
		VBox restaurantInfo = new VBox(5);
		Label name = new Label("Name: " + rst.name);
		Label address = new Label("Address: " + rst.address);
		Label phone = new Label("Phone: " + rst.getPhone());
		restaurantInfo.getChildren().addAll(name, address, phone);
		
		VBox reviewsWrapper = new VBox(12);
		VBox reviewFieldwrapper = new VBox(3);
		rating = new DropdownMenu("Rating", 5, null, 1, 5, true);
		commentField = new CustomTextField("Your comments.", 70, 200);
		commentField.setCharLimit(500);
		reviewFieldwrapper.getChildren().add(commentField);
		HBox ratingSubmit = new HBox(5);
		addReviewBtn = new Button("Add Review");
		addReviewBtn.setDefaultButton(true);
		ratingSubmit.getChildren().addAll(rating, addReviewBtn);
		reviewFieldwrapper.getChildren().add(ratingSubmit);

		layout.setTop(restaurantInfo);
		if(user == null || user.getStatus().equals("offline")){
			rating.setVisible(false);
			commentField.setVisible(false);
			addReviewBtn.setVisible(false);
		}
		Label reviewsTitle = new Label("Reviews: ");
		reviewsTitle.getStyleClass().add("h4");

		double rating = 0;
		int numRating = 0;
		ScrollPane reviewsContainer = new ScrollPane();
		reviewsContainer.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.getStyleClass().add("search");
		VBox userReviews = new VBox(5);
		for(Review review : reviews){
			Label author = new Label("Author: " + review.getUserName() + " on " + review.getCreationDate());
			author.getStyleClass().add("p");
			Label comments = new Label(review.getComments());
			comments.getStyleClass().add("p");
			Label userRating = new Label(String.valueOf(review.getRating()) + " stars.");
			rating += review.getRating();
			numRating += 1;
			userRating.getStyleClass().add("p");
			Rectangle profilePic = new Rectangle(75, 75);
			HBox hGrid = new HBox(10);
			hGrid.getChildren().add(profilePic);
			VBox vGrid = new VBox(3);
			vGrid.getChildren().addAll(userRating, comments, author);
			hGrid.getChildren().add(vGrid);
			Button deleteReviewBtn = new Button("delete");
			//Button editReviewBtn = new Button("edit");
			Button likeReviewBtn = new Button("Like");
			if(user != null){
				if(review.getUserId() == user.getId()){
					hGrid.getChildren().add(deleteReviewBtn);
					//hGrid.getChildren().add(editReviewBtn);
				}
				else{
					hGrid.getChildren().add(likeReviewBtn);
				}
			}
			deleteReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent e) {
		    		db.deleteReview(review);
		    		if(reviews.size() == 1){
		    			reviews.clear();
		    		}
		    		else{
			    		reviews.remove(review);
		    		}
		    		buildPage(rst, user, reviews);
		        }		
			});
			userReviews.getChildren().add(hGrid);
		}
		addReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		String comments = commentField.getText();
	    		System.out.println("rating txt: " + comments);
	    		//System.out.println(rstrntView.rating.getSelectionModel().getSelectedIndex());
	    		Double rating = getRating();
	    		rst.addRating(rating);
	    		//double rt = rstnt.getRating();
	    		//System.out.println("rating: " + rt);
	    		//rstrntView.rating.setRating(rt);
	    		Review review = new Review(rating, comments, user, rst);
	    		db.insertReview(review);
	    		//rstrntView.load(currentUser, db);
	    		//restaurantScene(rst);
	    		reviews.add(review);
	    		buildPage(rst, user, reviews);
	        }		
		});
		reviewsWrapper.getChildren().addAll(reviewFieldwrapper, reviewsTitle, userReviews);
		reviewsContainer.setContent(reviewsWrapper);
		layout.setRight(reviewsContainer);
		Label currentRating = new Label("Rating: " + Math.round((rating/numRating)*100)/100 + " stars.");
		name.getStyleClass().add("h4");
		address.getStyleClass().add("h4");
		phone.getStyleClass().add("h4");
		currentRating.getStyleClass().add("h3");
		restaurantInfo.getChildren().add(currentRating);
	}

	public double getRating(){
		return Double.parseDouble(rating.getValue());
	}
	
}
