import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

public class RestaurantUI {
	private CustomTextField commentField;
	private DropdownMenu rating;
	private DBConnection db;
	private BorderPane layout;
	private Popup errDialog, confDialog;
	private boolean leftPaneExtended = true;
	//Rating rating;
	
	//ATTACH EXPAND CARROT TO CENTERED CONTENT
	public RestaurantUI(DBConnection db, Popup error, Popup confirm){
		layout = new BorderPane();
		this.db = db;
		errDialog = error;
		confDialog = confirm;
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
	}
	
	public void buildPage(Restaurant rst, User user, List<Review> reviews){
		GridPane centerContent = new GridPane();
		ColumnConstraints mainContent = new ColumnConstraints();
		mainContent.setPercentWidth(5);
		ColumnConstraints leftPaneControl = new ColumnConstraints();
		leftPaneControl.setPercentWidth(95);		
		centerContent.getColumnConstraints().addAll(mainContent, leftPaneControl);	
		
		VBox restaurantInfo = new VBox(5);
		Label name = new Label("Name: " + rst.name);
		Label address = new Label("Address: " + rst.address);
		Label phone = new Label("Phone: " + rst.getPhone());
		restaurantInfo.getChildren().addAll(name, address, phone);
		
		//HBox sidePaneHeader = new HBox(50);
		Label sidePaneTitle = new Label("Your thoughts:");
		sidePaneTitle.getStyleClass().add("h3");
		
		IconNode extendLeftIcon = new IconNode(GoogleMaterialDesignIcons.SKIP_NEXT);
		extendLeftIcon.setIconSize(20);
		extendLeftIcon.setFill(Color.BLACK);

		IconNode contractLeftIcon = new IconNode(GoogleMaterialDesignIcons.SKIP_PREVIOUS);
		contractLeftIcon.setIconSize(20);
		contractLeftIcon.setFill(Color.BLACK);
        //Button contractLeft = new Button("", contractLeftIcon);
        Button extendLeft = new Button("", contractLeftIcon);
        extendLeft.setStyle("-fx-background-color: transparent !important");
		//sidePaneHeader.getChildren().addAll(sidePaneTitle, extendLeft);
		
		VBox reviewsWrapper = new VBox(12);
		VBox reviewFieldContainer = new VBox(3);
		rating = new DropdownMenu("Rating", 5, null, 1, 5, true);
		commentField = new CustomTextField("Your comments.", 100, 120);
		commentField.setCharLimit(260);
		HBox ratingSubmit = new HBox(5);
		Button addReviewBtn = new Button("Add Review");
		addReviewBtn.setDefaultButton(true);
		ratingSubmit.getChildren().addAll(rating, addReviewBtn);
		reviewFieldContainer.getChildren().addAll(sidePaneTitle, commentField, ratingSubmit);

		if(user == null || user.getStatus().equals("offline")){
			reviewFieldContainer.managedProperty().bind(reviewFieldContainer.visibleProperty());
			reviewFieldContainer.setVisible(false);
		}
		
		GridPane sidePane = new GridPane();

		Label reviewsTitle = new Label("User Reviews: ");
		reviewsTitle.getStyleClass().add("h4");

		double rating = 0;
		int numRating = 0;
		
		ScrollPane reviewsContainer = new ScrollPane();
		reviewsContainer.setStyle("-fx-background-color: #34495e !important");
		reviewsContainer.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.getStyleClass().add("scroll-fill");
		reviewsContainer.setMaxWidth(360);
        
		extendLeft.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				System.out.println("clicked expand carat.");
				if(leftPaneExtended){
					leftPaneExtended = false;
					extendLeft.setGraphic(extendLeftIcon);
					reviewsContainer.setMaxWidth(120);
				}
				else{
					leftPaneExtended = true;
					extendLeft.setGraphic(contractLeftIcon);
					reviewsContainer.setMaxWidth(360);
				}
			}
		});
		
		centerContent.add(extendLeft, 0, 0);
		centerContent.add(restaurantInfo, 1, 0);
		layout.setCenter(centerContent);
		
		VBox userReviews = new VBox(12);
		for(Review review : reviews){
			Label author = new Label("Author: " + review.getUserName() + " on " + review.getCreationDate());
			author.getStyleClass().add("p");
			Label comments = new Label(review.getComments());
			comments.getStyleClass().add("p");
			Label userRating = new Label(String.valueOf(review.getRating()) + " stars.");
			rating += review.getRating();
			numRating += 1;
			userRating.getStyleClass().add("p");
			Rectangle profilePic = new Rectangle(60, 60);
			HBox hGrid = new HBox(10);
			hGrid.getChildren().add(profilePic);
			VBox vGrid = new VBox(3);
			vGrid.getChildren().addAll(userRating, comments, author);
			hGrid.getChildren().add(vGrid);

			IconNode likeIcon = new IconNode(GoogleMaterialDesignIcons.THUMB_UP);
	        likeIcon.setIconSize(16);
	        likeIcon.setFill(Color.WHITE);
	        Button likeReviewBtn = new Button("", likeIcon);
	        
	        IconNode dislikeIcon = new IconNode(GoogleMaterialDesignIcons.THUMB_DOWN);
	        dislikeIcon.setIconSize(16);
	        dislikeIcon.setFill(Color.WHITE);
	        Button dislikeReviewBtn = new Button("", dislikeIcon);
	        
	        IconNode deleteIcon = new IconNode(GoogleMaterialDesignIcons.DELETE_FOREVER);
	        deleteIcon.setIconSize(16);
	        deleteIcon.setFill(Color.FIREBRICK);
			Button deleteReviewBtn = new Button("", deleteIcon);
			
			IconNode editIcon = new IconNode(GoogleMaterialDesignIcons.EDIT);
			editIcon.setIconSize(16);
			editIcon.setFill(Color.WHITE);
			Button editReviewBtn = new Button("", editIcon);

			if(user != null){
				if(review.getUserId() == user.getId()){
					hGrid.getChildren().addAll(deleteReviewBtn, editReviewBtn);
				}
				VBox repCont = new VBox(5);
				repCont.getChildren().addAll(likeReviewBtn, dislikeReviewBtn);
				hGrid.getChildren().add(0, repCont);
			}
			deleteReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent e) {
		        	confDialog.createDialog("Delete review.", "Are you sure you want to delete this review? It cannot be restored once deleted.");
		    		if(confDialog.userConfirmation().get() == ButtonType.OK){
			        	db.deleteReview(review);
			    		if(reviews.size() == 1){
			    			reviews.clear();
			    		}
			    		else{
				    		reviews.remove(review);
			    		}
			    		buildPage(rst, user, reviews);
		    		}
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
		reviewsWrapper.getChildren().addAll(reviewFieldContainer, reviewsTitle, userReviews);
		reviewsContainer.setContent(reviewsWrapper);
		layout.setLeft(reviewsContainer);
		Label currentRating = new Label("Rating: " + Math.round((rating/numRating)*100)/100 + " stars.");
		name.getStyleClass().add("h4");
		address.getStyleClass().add("h4");
		phone.getStyleClass().add("h4");
		currentRating.getStyleClass().add("h3");
		restaurantInfo.getChildren().add(currentRating);
		restaurantInfo.setFocusTraversable(true);
	}

	public double getRating(){
		return Double.parseDouble(rating.getValue());
	}
	
	public BorderPane getLayout(){
		return layout;
	}
	
}
