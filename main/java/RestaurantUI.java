
import java.util.List;

import javax.swing.SpringLayout.Constraints;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class RestaurantUI {
	
	Button addReviewBtn;
	CustomTextField commentField;
	DropdownMenu rating;
	AppWindow window;
	List<Review> reviews;
	Restaurant rst;
	//Rating rating;
	
	public void buildStage(Restaurant res, AppWindow win, User user, List<Review> reviews, DBConnection db){
		window = win;
		this.reviews = reviews;
		rst = res;
		addReviewBtn = new Button("Add Review");
		addReviewBtn.setDefaultButton(true);
		load(user, db);
	}
	
	public double getRating(){
		return Double.parseDouble(rating.getValue());
	}
	
	public void load(User user, DBConnection db){
		window.resetLayout();
		ColumnConstraints constraints = new ColumnConstraints();
		constraints.setPercentWidth(100/2);
		window.layout.getColumnConstraints().add(constraints);
		VBox restaurantInfo = new VBox(5);
		Label name = new Label("Name: " + rst.name);
		Label address = new Label("Address: " + rst.address);
		Label phone = new Label("Phone: " + rst.getPhone());

		restaurantInfo.getChildren().add(name);
		restaurantInfo.getChildren().add(address);
		restaurantInfo.getChildren().add(phone);
		
		VBox wrapper = new VBox(3);
		rating = new DropdownMenu("Rating", 5, null, 1, 5, true);
		commentField = new CustomTextField("Your comments.", 70, 200);
		commentField.setCharLimit(500);
		wrapper.getChildren().add(commentField);
		HBox ratingSubmit = new HBox(5);
		ratingSubmit.getChildren().add(rating);
		ratingSubmit.getChildren().add(addReviewBtn);
		wrapper.getChildren().add(ratingSubmit);
		
		window.layout.add(restaurantInfo, 0, 0);
		window.layout.add(wrapper, 1, 0);
		if(user == null || user.getStatus().equals("offline")){
			rating.setVisible(false);
			commentField.setVisible(false);
			addReviewBtn.setVisible(false);
		}
		Label reviewsTitle = new Label("Reviews: ");
		reviewsTitle.getStyleClass().add("field");
		window.layout.add(reviewsTitle, 1, 1);
		int index = 2;
		double rating = 0;
		int numRating = 0;
		for(Review review : reviews){
			Label author = new Label("Author: " + review.getUserName() + " on " + review.getCreationDate());
			author.getStyleClass().add("normalText");
			Label comments = new Label(review.getComments());
			comments.getStyleClass().add("normalText");
			Label userRating = new Label(String.valueOf(review.getRating()) + " stars.");
			rating += review.getRating();
			numRating += 1;
			userRating.getStyleClass().add("normalText");
			Rectangle profilePic = new Rectangle(75, 75);
			HBox hGrid = new HBox(10);
			hGrid.getChildren().add(profilePic);
			VBox vGrid = new VBox(3);
			vGrid.getChildren().add(userRating);
			vGrid.getChildren().add(comments);
			vGrid.getChildren().add(author);
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
		    		load(user, db);
		        }		
			});
			window.layout.add(hGrid, 1, index);
			index += 1;
		}
		
		Label currentRating = new Label("Rating: " + Math.round(rating*100)/100 + " stars.");
		name.getStyleClass().add("field");
		address.getStyleClass().add("field");
		phone.getStyleClass().add("field");
		currentRating.getStyleClass().add("field");
		restaurantInfo.getChildren().add(currentRating);
	}

}
