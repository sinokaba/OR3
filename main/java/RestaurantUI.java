import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.controlsfx.control.Rating;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

public class RestaurantUI {
	private TextArea commentField;
	private Rating ratingField = new Rating();
	private DBConnection db;
	private BorderPane layout;
	private Popup errDialog, confDialog;
	private Alert reviewDialog;
	
	public RestaurantUI(DBConnection db, Window win){
		layout = new BorderPane();
		this.db = db;
		errDialog = new Popup("err", win);
		confDialog = new Popup("conf", win);
		createReviewDialog("Post your review.", "Your comments.", win);
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
	}
	
	public void buildPage(Restaurant rst, User user, List<Review> reviews){
		reviewDialog.setTitle("Review for " + rst.name);
		double overallRating = rst.getRating();
		
		GridPane headerContent = new GridPane();
		
		ColumnConstraints mainContent = new ColumnConstraints();
		mainContent.setPercentWidth(70);
		ColumnConstraints leftPaneControl = new ColumnConstraints();
		leftPaneControl.setPercentWidth(29);		
		headerContent.getColumnConstraints().addAll(mainContent, leftPaneControl);	
		
		Label name = new Label(rst.name);
		name.getStyleClass().add("h2");
		headerContent.add(name, 0, 0);
		Label phone = new Label("Phone: " + rst.getPhone());
		phone.getStyleClass().add("h4");
		headerContent.add(phone, 1, 0);
		
		GridPane restaurantBody = new GridPane();
		VBox restaurantInfo = new VBox(2);
		Label address = new Label("Address: " + rst.getAddress());
		address.getStyleClass().add("h4");
		Label hours = new Label("Hours: 9am-10pm");
		hours.getStyleClass().add("h4");
		Label priceRange = new Label("Price Range: $" + rst.getPriceRange());
		priceRange.getStyleClass().add("h4");
		restaurantInfo.getChildren().addAll(address, hours, priceRange);
		restaurantBody.add(restaurantInfo, 0, 0);
		Label restaurantPics = new Label("Pictures");
		restaurantPics.getStyleClass().add("h3");		
		restaurantBody.add(restaurantPics, 0, 2);
		Label restaurantMenu = new Label("Menu");
		restaurantMenu.getStyleClass().add("h3");
		restaurantBody.add(restaurantMenu, 5, 2);
		VBox menuItemCont = new VBox(5);		
		for(int i = 0; i < 6; i++){
			Label item = new Label("Item" + String.valueOf(i) + " - $$");
			item.getStyleClass().add("p");
			menuItemCont.getChildren().add(item);
		}
		restaurantBody.add(menuItemCont, 5, 3);
		Label restaurantDirections = new Label("Location");
		restaurantDirections.getStyleClass().add("h3");
		restaurantBody.add(restaurantDirections, 5, 6);
		
		//placeholder images
		/*
		VBox picturesContainer = new VBox(10);
		for(int i = 0; i < 2; i++){
			Rectangle r = new Rectangle(260, 140);
			picturesContainer.getChildren().add(r);
		}
		restaurantBody.add(picturesContainer, 0, 4, 4, 10);
		*/
		HBox reviewButtonCont = new HBox();
		IconNode postIcon = new IconNode(GoogleMaterialDesignIcons.BORDER_COLOR);
		postIcon.setIconSize(24);
		postIcon.setFill(Color.BLACK);
		Button addReviewBtn = new Button("Write A Review", postIcon);
		reviewButtonCont.getChildren().add(addReviewBtn);
		reviewButtonCont.setAlignment(Pos.CENTER);
		
		Rating ratingStars = new Rating();
		ratingStars.setPartialRating(true);
		ratingStars.setMax(5);
		ratingStars.setRating(overallRating);
		ratingStars.setDisable(true);
		Label ratingLabel = new Label(String.valueOf(overallRating));
		ratingLabel.getStyleClass().add("h2");
		
		Label reviewsTitle = new Label("Reviews(" + String.valueOf(rst.numReviews) +")");
		reviewsTitle.getStyleClass().add("h3");
		
		ScrollPane reviewsContainer = new ScrollPane();
		reviewsContainer.setStyle("-fx-background-color: #34495e !important");
		reviewsContainer.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.getStyleClass().add("scroll-fill");
		//golden ratio 1040 - 1040*0.618, the review section of the page
		reviewsContainer.setPrefWidth(397);
		Insets topPadding = new Insets(10);
		reviewsContainer.setPadding(topPadding);
		
		BorderPane restaurantContent = new BorderPane();
		restaurantContent.setTop(headerContent);
		restaurantContent.setCenter(restaurantBody);
		layout.setCenter(restaurantContent);
		
		VBox userReviews = new VBox(12);
		for(Review review : reviews){
			Label author = new Label("Author: " + review.getUserName() + " on " + review.getCreationDate());
			author.getStyleClass().add("p");
			Group commentContainer = new Group();
			String userComments = review.getComments();
			if(userComments.length() >= 100){
				//Hyperlink more = new Hyperlink("...");
				//commentContainer.getChildren().add(more);
				userComments = userComments.substring(0, 100) + "...";
			}
			//wordutils wraps around the comment string, and starts a new line if the comment exceeds a certain length
			Label comments = new Label(WordUtils.wrap(userComments, 45));
			comments.getStyleClass().add("p");
			commentContainer.getChildren().add(comments);
			
			//creating 5 star rating for each user's review
			final Rating userRating = new Rating();
			userRating.setPartialRating(true);
			userRating.setMax(5);
			userRating.setRating(review.getRating());
			userRating.setDisable(true);
			userRating.getStyleClass().addAll("stars-small", "user-rating");
			
			//keeping track of the number of reviews and their rating
			//used circle shape as a temporary measure for a user profile picture
			HBox hGrid = new HBox(10);
			VBox vGrid = new VBox(2);
			vGrid.getChildren().addAll(userRating, commentContainer, author);
			Circle profilePic = new Circle(20);
			hGrid.getChildren().addAll(profilePic, vGrid);
			
			//All the icons used for each review
			IconNode likeIcon = new IconNode(GoogleMaterialDesignIcons.THUMB_UP);
	        likeIcon.setIconSize(16);
	        likeIcon.setFill(Color.WHITE);
	        Hyperlink likeReviewBtn = new Hyperlink("", likeIcon);
	        
	        IconNode dislikeIcon = new IconNode(GoogleMaterialDesignIcons.THUMB_DOWN);
	        dislikeIcon.setIconSize(16);
	        dislikeIcon.setFill(Color.WHITE);
	        Hyperlink dislikeReviewBtn = new Hyperlink("", dislikeIcon);
	        
	        IconNode deleteIcon = new IconNode(GoogleMaterialDesignIcons.DELETE_FOREVER);
	        deleteIcon.setIconSize(20);
	        deleteIcon.setFill(Color.WHITE);
	        Hyperlink deleteReviewBtn = new Hyperlink("", deleteIcon);
			
			IconNode editIcon = new IconNode(GoogleMaterialDesignIcons.EDIT);
			editIcon.setIconSize(20);
			editIcon.setFill(Color.WHITE);
			Hyperlink editReviewBtn = new Hyperlink("", editIcon);

			IconNode reportIcon = new IconNode(GoogleMaterialDesignIcons.FLAG);
			reportIcon.setIconSize(20);
			reportIcon.setFill(Color.WHITE);
			Hyperlink reportReviewBtn = new Hyperlink("", reportIcon);
			
			IconNode favIcon = new IconNode(GoogleMaterialDesignIcons.FAVORITE);
			favIcon.setIconSize(20);
			favIcon.setFill(Color.WHITE);
			Hyperlink favReviewBtn = new Hyperlink("", favIcon);
			
			IconNode replyIcon = new IconNode(GoogleMaterialDesignIcons.REPLY);
			replyIcon.setIconSize(20);
			replyIcon.setFill(Color.WHITE);
			Hyperlink replyBtn = new Hyperlink("", replyIcon);
			
			if(user != null){
				//only show certain functions when user is logged in
				System.out.println("review by: " + review.getUserId() + " user = " + user.getId());
				if(review.getUserId() == user.getId()){
					HBox userActionsCont = new HBox(3);
					userActionsCont.getChildren().addAll(likeReviewBtn, dislikeReviewBtn, favReviewBtn, replyBtn, deleteReviewBtn, editReviewBtn);
					vGrid.getChildren().add(userActionsCont);
				}
				else{
					HBox userActionsCont = new HBox(3);
					userActionsCont.getChildren().addAll(likeReviewBtn, dislikeReviewBtn, favReviewBtn, replyBtn, reportReviewBtn);
					vGrid.getChildren().add(userActionsCont);
				}
			}
			deleteReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent e) {
		        	//popup dialog created to asks for confirmation whenever user deletes a review
		        	confDialog.createDialog("Delete review.", "Are you sure you want to delete this review? It cannot be restored once deleted.");
		    		if(confDialog.userConfirmation().get() == ButtonType.OK){
			        	db.deleteReview(review);
			    		if(reviews.size() == 1){
			    			reviews.clear();
			    		}
			    		else{
			    			rst.removeReview(user, review);
				    		reviews.remove(review);
			    		}
			    		buildPage(rst, user, reviews);
		    		}
		        }		
			});
			editReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e){
					commentField.setText(review.getComments());
					ratingField.setRating(review.getRating());
					ButtonType userAction = reviewDialog.showAndWait().get();
	        		if(userAction == ButtonType.OK && !(commentField.getText().trim().length() <= 0 || getRating() == -1)){
			    		//review posted by user is validated and added to db
	        			Double rating = getRating();
			    		//rst.addRating(rating);
			    		Review updatedReview = new Review(rating, commentField.getText(), user, rst);
			    		db.updateReview(review, updatedReview);
			    		rst.updateReview(user, review, updatedReview);
			    		review.setComments(commentField.getText());
			    		review.setRating(rating);
				    	commentField.clear();
				    	ratingField.setRating(-1);
				    	//reload page
			    		buildPage(rst, user, reviews);
	        		}
			    	commentField.clear();
			    	ratingField.setRating(-1);
				}
			});
			userReviews.getChildren().add(hGrid);
		}
		addReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	if(!(user == null || user.getStatus().equals("offline"))){
	        		ButtonType userAction = reviewDialog.showAndWait().get();
	        		if(userAction == ButtonType.OK && !(commentField.getText().trim().length() <= 0 || getRating() == -1)){
			    		//review posted by user is validated and added to db
	        			Double rating = getRating();
			    		Review newReview = new Review(rating, commentField.getText(), user, rst);
			    		rst.addReview(user, newReview);
			    		db.insertReview(newReview);
			    		reviews.add(newReview);
				    	commentField.clear();
				    	ratingField.setRating(-1);
				    	//reload page
			    		buildPage(rst, user, reviews);
	        		}
			    	commentField.clear();
			    	ratingField.setRating(-1);
	        	}
	        	else{
	        		errDialog.showAlert("Write a review.", "You are not logged in. Please log in to write your review.");
	        	}
	        }		
		});
		VBox reviewHeaderCont = new VBox(10);
		reviewHeaderCont.setPrefWidth(360);
		reviewHeaderCont.setAlignment(Pos.CENTER);
		reviewHeaderCont.getChildren().addAll(ratingStars, ratingLabel, reviewButtonCont);
		
		VBox reviewsWrapper = new VBox(12);
		reviewsWrapper.getChildren().addAll(reviewHeaderCont, reviewsTitle, userReviews);
		reviewsContainer.setContent(reviewsWrapper);
		layout.setLeft(reviewsContainer);
		
		//the restaurant info section is focused on page load, not review section
		restaurantInfo.setFocusTraversable(true);
	}
	
	public void createReviewDialog(String title, String placeholder, Window win){
		reviewDialog = new Alert(Alert.AlertType.CONFIRMATION);
		reviewDialog.initOwner(win);
		reviewDialog.setHeaderText("Your review.");
		DialogPane reviewD = reviewDialog.getDialogPane();
		reviewD.setGraphic(null);
		reviewD.getStyleClass().add("review-dialog");
		
		commentField = new TextArea();
		commentField.setPromptText("Your comments.");
		commentField.setEditable(true);
		commentField.setWrapText(true);
		commentField.setMaxWidth(480);
		commentField.setMaxHeight(270);

		ratingField.setPartialRating(true);
		ratingField.setMax(5);
		ratingField.setRating(-1);
		//ratingField.getStyleClass().add("rating");
		
		VBox contentContainer = new VBox(15);
		GridPane.setVgrow(commentField, Priority.ALWAYS);
		GridPane.setHgrow(commentField, Priority.ALWAYS);
		contentContainer.getChildren().addAll(ratingField, commentField);
		reviewD.setContent(contentContainer);
		
		//button filter which checks if all the fields have been filled
		final Button btnOk = (Button) reviewDialog.getDialogPane().lookupButton(ButtonType.OK);
		btnOk.addEventFilter(ActionEvent.ACTION, event -> {
		        if(commentField.getText().trim().length() <= 0 || getRating() == -1){
		            //The conditions are not fulfilled so we consume the event to prevent the dialog from closing
		            event.consume();
	    			errDialog.showAlert("Incomplete review!", "Error! You must fill out all the fields!");		            
		        }
		    }
		);
	}
	
	public double getRating(){
		return ratingField.getRating();
	}
	
	public BorderPane getLayout(){
		return layout;
	}
	
}
