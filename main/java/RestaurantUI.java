import java.util.List;

import org.controlsfx.control.Rating;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	private DropdownMenu ratingField;
	private DBConnection db;
	private BorderPane layout;
	private Popup errDialog, confDialog;
	private Alert reviewDialog;
	private boolean leftPaneExtended = true;
	//Rating rating;
	
	//ATTACH EXPAND CARROT TO CENTERED CONTENT
	public RestaurantUI(DBConnection db, Window win){
		layout = new BorderPane();
		this.db = db;
		errDialog = new Popup("err", win);
		confDialog = new Popup("conf", win);
		reviewDialog = new Alert(Alert.AlertType.CONFIRMATION);
		reviewDialog.initOwner(win);
		//reviewDialog.setTitle("Your review.");
		reviewDialog.setHeaderText(null);
		reviewDialog.initStyle(StageStyle.UTILITY);
		addReviewField("Post your review.", "Your comments.");
		final Button btOk = (Button) reviewDialog.getDialogPane().lookupButton(ButtonType.OK);
		btOk.addEventFilter(
		    ActionEvent.ACTION, 
		    event -> {
		        // Check whether some conditions are fulfilled
		        if(commentField.getText().trim().length() <= 0 || getRating() == -1){
		            // The conditions are not fulfilled so we consume the event
		            // to prevent the dialog to close
		        	//commentField.requestFocus();
		            event.consume();
	    			errDialog.showAlert("Incomplete review!", "Error! You must fill out all the fields!");		            
		        }
		    }
		);
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
	}
	
	public void buildPage(Restaurant rst, User user, List<Review> reviews){
		reviewDialog.setTitle("Review for " + rst.name);
		
		GridPane headerContent = new GridPane();
		ColumnConstraints mainContent = new ColumnConstraints();
		mainContent.setPercentWidth(65);
		ColumnConstraints leftPaneControl = new ColumnConstraints();
		leftPaneControl.setPercentWidth(35);		
		headerContent.getColumnConstraints().addAll(mainContent, leftPaneControl);	
		
		BorderPane restaurantContent = new BorderPane();
		Label name = new Label(rst.name);
		name.getStyleClass().add("h1");
		
		VBox restaurantInfo = new VBox(2);
		Label address = new Label("Address: " + rst.address);
		Label phone = new Label("Phone: " + rst.getPhone());
		restaurantInfo.getChildren().addAll(address, phone);
		
		//HBox sidePaneHeader = new HBox(50);
		//Label sidePaneTitle = new Label("Your thoughts:");
		//sidePaneTitle.getStyleClass().add("h3");
			
		VBox reviewsWrapper = new VBox(12);
        /*
		VBox reviewFieldContainer = new VBox(3);
		rating = new DropdownMenu("Rating", 5, null, 1, 5, true);

		commentField = new TextArea("Your comments.");
		commentField.setMaxSize(240, 135);
		//commentField.setCharLimit(260);
		HBox ratingSubmit = new HBox(5);
		*/
		HBox reviewButtonCont = new HBox(10);
		IconNode postIcon = new IconNode(GoogleMaterialDesignIcons.BORDER_COLOR);
		postIcon.setIconSize(32);
		postIcon.setFill(Color.BLACK);
		Button addReviewBtn = new Button("Write A Review", postIcon);
		addReviewBtn.getStyleClass().add("main-button");
		reviewButtonCont.getChildren().add(addReviewBtn);
		reviewButtonCont.setAlignment(Pos.CENTER);
		//addReviewBtn.setDefaultButton(true);
		
		//ratingSubmit.getChildren().addAll(rating, addReviewBtn);
		//reviewFieldContainer.getChildren().addAll(sidePaneTitle, commentField, ratingSubmit);
		
		/*
		if(user == null || user.getStatus().equals("offline")){
			reviewFieldContainer.managedProperty().bind(reviewFieldContainer.visibleProperty());
			reviewFieldContainer.setVisible(false);
		}
		*/
		
		//GridPane sidePane = new GridPane();

		Label reviewsTitle = new Label("Reviews");
		reviewsTitle.getStyleClass().add("h3");

		double rating = 0;
		int numRating = 0;
		
		ScrollPane reviewsContainer = new ScrollPane();
		reviewsContainer.setStyle("-fx-background-color: #34495e !important");
		reviewsContainer.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		reviewsContainer.getStyleClass().add("scroll-fill");
		reviewsContainer.setMaxWidth(360);
		Insets topPadding = new Insets(10);
		reviewsContainer.setPadding(topPadding);
		
		headerContent.add(name, 0, 0);
		restaurantContent.setTop(headerContent);
		restaurantContent.setCenter(restaurantInfo);
		layout.setCenter(restaurantContent);
		
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
			Circle profilePic = new Circle(20);
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
	        	if(!(user == null || user.getStatus().equals("offline"))){
	        		ButtonType userAction = reviewDialog.showAndWait().get();
	        		//System.out.println("user actions: " + userAction);
	        		if(userAction == ButtonType.OK && !(commentField.getText().trim().length() <= 0 || getRating() == -1)){
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
				    	commentField.clear();
				    	ratingField.clear();
			    		buildPage(rst, user, reviews);
	        		}
			    	commentField.clear();
			    	ratingField.clear();
	        	}
	        	else{
	        		errDialog.showAlert("Write a review.", "You are not logged in. Please log in to write your review.");
	        	}
	        }		
		});
		reviewsWrapper.getChildren().addAll(reviewButtonCont, reviewsTitle, userReviews);
		reviewsContainer.setContent(reviewsWrapper);
		layout.setLeft(reviewsContainer);
		final Rating ratingStars = new Rating();
		ratingStars.setPartialRating(true);
		ratingStars.setMax(5);
		ratingStars.setRating(Math.round((rating/numRating)*100.0)/100.0);
		ratingStars.setDisable(true);
		//Label currentRating = new Label("Rating: " + Math.round((rating/numRating)*100)/100 + " stars.");
		address.getStyleClass().add("h4");
		phone.getStyleClass().add("h4");
		//currentRating.getStyleClass().add("h3");
		restaurantInfo.getChildren().add(ratingStars);
		restaurantInfo.setFocusTraversable(true);
	}

	public double getRating(){
		if(ratingField.getValue().isEmpty() || ratingField.getValue().equals("Rating")){
			return -1;
		}
		return Double.parseDouble(ratingField.getValue());
	}
	
	public BorderPane getLayout(){
		return layout;
	}
	
	public void addReviewField(String title, String placeholder){
		commentField = new TextArea();
		commentField.setPromptText("Your comments.");
		commentField.setEditable(true);
		commentField.setWrapText(true);
		commentField.setMaxWidth(400);
		commentField.setMaxHeight(225);

		ratingField = new DropdownMenu("Rating", 5, null, 1, 5, true);
		
		Label label = new Label("Write a review.");
		label.getStyleClass().add("h3");

		GridPane expContent = new GridPane();
		GridPane.setVgrow(commentField, Priority.ALWAYS);
		GridPane.setHgrow(commentField, Priority.ALWAYS);
		expContent.add(label, 0, 0);
		expContent.add(ratingField, 1, 0);
		expContent.add(commentField, 0, 1);
		reviewDialog.getDialogPane().setContent(expContent);
		//alert.initStyle(StageStyle.UTILITY);
	}
	
}
