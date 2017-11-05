
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class RestaurantUI {
	
	Button addReviewBtn;
	CustomTextField commentField;
	DropdownMenu rating;
	AppWindow window;
	//Rating rating;
	
	public void buildStage(Restaurant res, AppWindow win, boolean loggedIn, List<Review> reviews){
		window = win;
		window.resetLayout();
		Label name = new Label("Name: " + res.name);
		Label address = new Label("Addres: " + res.address);
		name.getStyleClass().add("field");
		address.getStyleClass().add("field");

		window.layout.add(name, 1, 2);
		window.layout.add(address, 1, 3);
		
		int index = 4;
		for(Review review : reviews){
			Label author = new Label("Author: " + review.getUserName() + " on 11/4/2017");
			author.getStyleClass().add("field");
			Label comments = new Label(review.getComments());
			comments.getStyleClass().add("field");
			Label rating = new Label(String.valueOf(review.getRating()) + " stars.");
			rating.getStyleClass().add("field");
			Rectangle profilePic = new Rectangle(75, 75);
			HBox hGrid = new HBox(10);
			hGrid.getChildren().add(profilePic);
			VBox vGrid = new VBox(10);
			vGrid.getChildren().add(rating);
			vGrid.getChildren().add(comments);
			vGrid.getChildren().add(author);
			hGrid.getChildren().add(vGrid);
			window.layout.add(hGrid, 1, index);
			index += 1;
		}
		HBox wrapper = new HBox(10);
		rating = new DropdownMenu("Rating", 5, null, 1, 5, true);
		commentField = new CustomTextField("Your comments.", 70, 250);
		commentField.setCharLimit(500);
		wrapper.getChildren().add(rating);
		wrapper.getChildren().add(commentField);
		addReviewBtn = new Button("Add Review");
		addReviewBtn.setDefaultButton(true);
		window.layout.add(wrapper, 1, index);
		window.layout.add(addReviewBtn, 1, index + 1);
		
		if(!loggedIn){
			rating.setVisible(false);
			commentField.setVisible(false);
			addReviewBtn.setVisible(false);
		}
		window.updateElementCount(index);
	}
	
	public double getRating(){
		return Double.parseDouble(rating.getValue());
	}
	
	public void postReview(Review review){
		Label author = new Label("Author: " + review.getUserName() + " on 11/4/2017");
		author.getStyleClass().add("field");
		Label comments = new Label(review.getComments());
		comments.getStyleClass().add("field");
		Label rating = new Label(String.valueOf(review.getRating()) + " stars.");
		rating.getStyleClass().add("field");
		Rectangle profilePic = new Rectangle(75, 75);
		HBox hGrid = new HBox(10);
		hGrid.getChildren().add(profilePic);
		VBox vGrid = new VBox(10);
		vGrid.getChildren().add(rating);
		vGrid.getChildren().add(comments);
		vGrid.getChildren().add(author);
		hGrid.getChildren().add(vGrid);
		window.layout.add(hGrid, 1, window.getNumElements()-1);
		//window.updateElementCount(window.getNumElements()+1);
	}
}
