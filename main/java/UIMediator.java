
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UIMediator extends Application{

	final int WINDOW_HEIGHT = 650;
	final int WINDOW_WIDTH = 900;
	private Scene scene;
	private Stage primaryStage;
	private AppWindow window;	
	private	HomepageUI homeView;
	private RegistrationUI regView;
	private LoginUI loginView;
	private RestaurantRegistrationUI restaurantRegView;
	private RestaurantUI rstrntView;
	private searchResultsUI searchView;
	
	private DBConnection db;
	private int formFieldWidth = 258;
	private int formFieldHeight = 38;
	private String titleBase = "OR3 - ";
	private String defaultTitleEx = "Rate, Review, Dine";
	final private GoogleMapsService mapsApi;

	final private Pattern GOOD_LOCATION_INPUT = 
			Pattern.compile("^[a-zA-Z0-9]*.{2,}$");
	private boolean loggedIn = false;
	private User currentUser = null;
	private Restaurant currentRstrnt = null;
	Popup err, confirm;
	ValidateForm formValidation;
	
	public UIMediator(){
		mapsApi = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		window = new AppWindow();
		homeView = new HomepageUI();
		regView = new RegistrationUI();
		loginView = new LoginUI();
		restaurantRegView = new RestaurantRegistrationUI();
		rstrntView = new RestaurantUI();
		searchView = new searchResultsUI();
		//db = new DBConnection(dbURL, dbUsername, dbPassword, mapsApi);
		db = new DBConnection(mapsApi);
	}
	
	
	public void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage) throws Exception{
		primaryStage = stage;
		homePage();
		scene = new AppScene(window.root, WINDOW_WIDTH, WINDOW_HEIGHT);
		primaryStage.getIcons().add(new Image("/images/brand.png"));
		primaryStage.setTitle(titleBase+defaultTitleEx);
		err = new Popup("err", window.layout.getScene().getWindow());
		confirm = new Popup("conf", window.layout.getScene().getWindow());
		formValidation = new ValidateForm(window.layout, db, err);
		window.layout.getStyleClass().add("bgColor");
		primaryStage.setScene(scene);
		window.homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	homePage();	     
            }
        });
		window.userStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if(loggedIn){
            		confirm.createDialog("Logout confirmation.", "Are you sure you want to log out?");
            		Optional<ButtonType> result = confirm.userConfirmation();
            		if(result.get() == ButtonType.OK){
		            	System.out.println("logging off...");
		            	loggedIn = false;
		            	currentUser.loggedOff();
		            	window.userLogout();
		            	titleBase = "OR3 - ";
		            	homePage();
            		}
            	}
            	else{
		    		window.layout.getStyleClass().remove("bgImage");
            		loginPage();
            	}
            }
        });
		window.userMenuActions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if(oldValue != null && oldValue.equals(newValue)){
		        	newValue = oldValue;
		        }
		    	if(newValue.toLowerCase().equals("add restaurant")){
		        	//window.userMenuActions.getSelectionModel().clearSelection();
		    		window.userMenuActions.getSelectionModel().clearAndSelect(0);
		    		window.layout.getStyleClass().remove("bgImage");
		    		restaurantRegistrationpage();
		        }
		    }
		});
		primaryStage.show();
	}
	
	public void homePage(){
		if(!window.layout.getStyleClass().contains("bgImage")){
			window.layout.getStyleClass().add("bgImage");
		}
		primaryStage.setTitle(titleBase + defaultTitleEx);
		homeView.buildStage(window, loggedIn, db, mapsApi);
		//String[] testWords = {"hmm", "hell", "heyo", "da", "dark", "app"};
		/*
	    AutoCompletionBinding<String> locAutocomplete = TextFields.bindAutoCompletion(homeView.locationSearchField, sr -> { 
			return mapsApi.getPlacesSuggestions(sr.getUserText()); 
		}); 
	    locAutocomplete.setHideOnEscape(true);
	    locAutocomplete.setPrefWidth(269);
	    locAutocomplete.setDelay(1);
	    
	    AutoCompletionBinding<String> rstAutocomplete = TextFields.bindAutoCompletion(homeView.restaurantSearchField, sr -> {
			return db.getRestaurantSuggestions(sr.getUserText());
		});
	    rstAutocomplete.setPrefWidth(349);
	    rstAutocomplete.setHideOnEscape(true);
	    rstAutocomplete.setDelay(70);
	    */
		//if the login button is clicked the view is switched to the login page
		homeView.loginBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		window.layout.getStyleClass().remove("bgImage");
	        	loginPage();
	        }
	    });
		//if the sign up button is clicked the view is switched to the sign up page
		homeView.signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		window.layout.getStyleClass().remove("bgImage");
	    		userRegistrationPage();
	        }
	    });
		homeView.searchBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				String searchTerm = homeView.restaurantSearchField.getText().trim();
				String specifiedLoc = homeView.locationSearchField.getText().trim();
				if(searchTerm.length() > 0 || specifiedLoc.length() > 3){
		    		window.layout.getStyleClass().remove("bgImage");
					if(specifiedLoc.length() <= 3){
						specifiedLoc = null;
					}
					if(searchTerm.length() > 0){
						if(homeView.searchDropdown.getSelectionModel().getSelectedItem().equals("Name")){
				    		Restaurant dbQueryRes = db.getRestaurantFromDB(searchTerm, specifiedLoc);
							if(dbQueryRes != null){
								currentRstrnt = dbQueryRes;
					    		restaurantPage(currentRstrnt);
							}
							else{
								searchResultsPage(db.getRestaurantSuggestions(searchTerm, specifiedLoc), searchTerm, specifiedLoc);
							}
						}
					}
					else{
						searchResultsPage(db.getRestaurantSuggestions(searchTerm, specifiedLoc), searchTerm, specifiedLoc);						
					}
				}
				else{
					err.showAlert("Fields not completed.", "Error! The main search field must not be empty!");
				}
			}
		});
	}
	
	public void userRegistrationPage(){
		primaryStage.setTitle(titleBase + "Create an account.");
		regView.buildStage(window, formFieldWidth, formFieldHeight);
		regView.backBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	homePage();
	        }
	    });
		regView.registerBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String username = regView.getUsername();
	        	String email = regView.getEmail();
	        	String pw = regView.getPassword();
	        	String pwV = regView.getPasswordV();
	        	String zip = regView.getZipcode();
	        	if(formValidation.validUserRegistration(pw, pwV, username, email, zip)){
            		currentUser = new User(username, pw, regView.getBirthday(), email, zip, 0);
            		db.insertUser(currentUser);
	        		loginUser(username);
	        	}
	        }
	    });
	}
	
	public void searchResultsPage(List<String> result, String searchKeyword, String location){
		primaryStage.setTitle("OR3 - Searched for " + searchKeyword + " at " + location);
		searchView.buildStage(window, searchKeyword, location, result, db, UIMediator.this);
		
	}
	
	public void restaurantRegistrationpage(){
		primaryStage.setTitle("OR3 - Add restaurant.");
		restaurantRegView.buildStage(window, formFieldWidth, formFieldHeight, mapsApi);
		//AutoCompletionBinding<String> locAutocomplete = TextFields.bindAutoCompletion(restaurantRegView.addressField, sr -> { 
			//return mapsApi.getPlacesSuggestions(sr.getUserText()); 
		//}); 
		//locAutocomplete.setPrefWidth(formFieldWidth-1);
		restaurantRegView.registerBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String name = restaurantRegView.nameField.getText();
	        	String phone = restaurantRegView.phoneField.getText();
	        	String addrs = restaurantRegView.addressField.getText();
	        	String zip = restaurantRegView.zipcodeField.getText();
	    		if(formValidation.validRestaurantReg(name, addrs, zip, phone)){
	    			currentRstrnt = new Restaurant(name, phone);
	    			currentRstrnt.setAddress(addrs, zip);
	    			db.insertRestaurant(currentRstrnt);
	    			restaurantPage(currentRstrnt);
	    		}
	        }
		});
	}
	
	public void restaurantPage(Restaurant rstnt){
		rstrntView.buildStage(rstnt, window, currentUser, db.getRestaurantReviewsFromDB(rstnt), db);
		rstrntView.addReviewBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		String comments = rstrntView.commentField.getText();
	    		System.out.println("rating txt: " + comments);
	    		//System.out.println(rstrntView.rating.getSelectionModel().getSelectedIndex());
	    		Double rating = rstrntView.getRating();
	    		rstnt.addRating(rating);
	    		//double rt = rstnt.getRating();
	    		//System.out.println("rating: " + rt);
	    		//rstrntView.rating.setRating(rt);
	    		Review review = new Review(rating, comments, currentUser, rstnt);
	    		db.insertReview(review);
	    		//rstrntView.load(currentUser, db);
	    		restaurantPage(rstnt);
	        }		
		});
	}
	
	public void loginPage(){
		primaryStage.setTitle(titleBase + "Login to your account.");
		loginView.buildStage(window, formFieldWidth, formFieldHeight);
		loginView.backBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	homePage();
	        }
	    });
		loginView.loginBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String enteredUsername = loginView.getUsernameEntered();
	        	String enteredPassword = loginView.getPasswordEntered();
	        	//refactor this later
	        	if(enteredUsername.trim().length() <= 0 || enteredPassword.trim().length() <= 0){
	        		err.showAlert("Error form.", "Please fill out all the fields.");
	        	}
	        	else{
		        	currentUser = db.getUserFromDB(enteredUsername, enteredPassword);
		        	if(currentUser != null){
		        		titleBase = "OR3(" + enteredUsername + ") -";
		        		loginUser(enteredUsername);
		        	}
		        	else{
		        		err.showAlert("Error form.", "User not found with that password.");	        		
		        	}
	        	}
	        }
	    });
	}
	
	public void loginUser(String name){
		currentUser.loggedIn();
		loggedIn = true;
		String userCreds = name;
		if(currentUser.getPrivilege() == 1){
			userCreds = name + "(Admin)";
		}
		window.userLogin(userCreds);
		homePage();		
	}
	
	public boolean checkLocationInput(String oldUserInput, String newUserInput) {
		if(oldUserInput.equals(newUserInput)){
			return false;
		}
        Matcher matcher = GOOD_LOCATION_INPUT.matcher(newUserInput);
        return matcher.find();
	}

}
