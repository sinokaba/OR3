import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.controlsfx.control.Rating;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Window;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class UserAccountUI {
	private User user;
	private BorderPane layout;
	private DBConnection db;
	private Popup confirm;
	private Window win;
	Button deleteAccount;
	
	public UserAccountUI(DBConnection db, Window win, Popup conf){
		layout = new BorderPane();
		this.db = db;
		confirm = conf;
		this.win = win;
	}
	
	public void buildStage(User user){
		VBox leftPanelCont = new VBox(10);
		Label mainTitle = new Label(user.getUsername());
		mainTitle.getStyleClass().add("h1");
		Label reviewsTitle = new Label("Your Reviews");
		reviewsTitle.getStyleClass().add("h4");
		leftPanelCont.getChildren().addAll(mainTitle, reviewsTitle);
		ScrollPane userReviews = new ScrollPane();
		userReviews.setStyle("-fx-background-color: #34495e !important");
		userReviews.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		userReviews.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		userReviews.getStyleClass().add("scroll-fill");
		//golden ratio 1040*0.618, the review section of the page
		userReviews.setPrefWidth(643);
		Insets topPadding = new Insets(10);
		userReviews.setPadding(topPadding);
		userReviews.setContent(leftPanelCont);
		layout.setLeft(userReviews);
		
		List<Review> reviewList = db.getUserReviews(user);
		VBox userReviewsList = new VBox(6);
		if(reviewList != null){
			for(Review review : reviewList){
				Label rstName = new Label(review.getRestaurantName());
				rstName.getStyleClass().add("h4");
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
				vGrid.getChildren().addAll(rstName, userRating, commentContainer, author);
				hGrid.getChildren().addAll(vGrid);
				
				userReviewsList.getChildren().add(hGrid);
			}
		}
		leftPanelCont.getChildren().add(userReviewsList);
		
		
		VBox rightPane = new VBox(20);
		deleteAccount = new Button("Delete Account");
		deleteAccount.getStyleClass().addAll("main-button", "search-button");
		Button deleteRst = new Button("Delete Restaurant");
		deleteRst.getStyleClass().addAll("main-button", "search-button");
		Button deleteUser = new Button("Delete User");
		rightPane.getChildren().add(deleteAccount);
		if(user.getPrivilege() == 1){
			rightPane.getChildren().addAll(deleteRst, deleteUser);
		}
		layout.setCenter(rightPane);
		
		Alert deleteUserDialog = new Alert(Alert.AlertType.CONFIRMATION);
		deleteUserDialog.initOwner(win);
		deleteUserDialog.setHeaderText("Delete a naughty user.");
		DialogPane delUserD = deleteUserDialog.getDialogPane();
		delUserD.setGraphic(null);
		delUserD.getStyleClass().add("review-dialog");
		
		CustomTextField usernameField = new CustomTextField("username", 20, 100);
		
		VBox contentContainer = new VBox(15);
		GridPane.setVgrow(usernameField, Priority.ALWAYS);
		GridPane.setHgrow(usernameField, Priority.ALWAYS);
		contentContainer.getChildren().add(usernameField);
		delUserD.setContent(contentContainer);
		deleteUser.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				if(deleteUserDialog.showAndWait().get() == ButtonType.OK){
					String username = usernameField.getText();
					if(username.trim().length() >= 0){
						if(db.rowExists("users", "username", username)){
							db.deleteUser(username);
							confirm.showAlert("Success!", "Sucessfully deleted user");
						}
						else{
							confirm.showAlert("Fail!", "Failed to delete user. user does not exist.");
						}
						usernameField.clear();
					}
				}
			}
		});
		
	}
	
	public BorderPane getLayout(){
		return layout;
	}
}
