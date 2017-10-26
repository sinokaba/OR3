package main.java;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UIMediator extends Application{

	final int WINDOW_HEIGHT = 600;
	final int WINDOW_WIDTH = 800;
	//private StartUI startPage;
	private Stage primaryStage;
	private Scene scene;
	
	private AppWindow window;	
	private	HomepageUI homeView;
	private RegistrationUI regView;
	private LoginUI loginView;
	private RestaurantRegistrationUI restaurantRegView;
	private RestaurantUI resView;
	private DBConnection db;
	
	private int formFieldWidth = 258;
	private int formFieldHeight = 38;
	final private String dbURL = "jdbc:mysql://localhost:3306/2102_or3?autoReconnect=true&useSSL=false";
	final private String dbUsername = "root";
	final private String dbPassword = "allanK0_ph";

	private boolean loggedIn = false;
	private User currentUser;
	
	private Restaurant currentRes;
	Popup err, confirm;
	ValidateForm formValidation;
	
	public UIMediator(){
		window = new AppWindow();
		homeView = new HomepageUI();
		regView = new RegistrationUI();
		loginView = new LoginUI();
		restaurantRegView = new RestaurantRegistrationUI();
		resView = new RestaurantUI();
		db = new DBConnection(dbURL, dbUsername, dbPassword);
	}
	
	
	public void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage) throws Exception{
		loadHomePage();
		scene = new AppScene(window.root, WINDOW_WIDTH, WINDOW_HEIGHT);
		err = new Popup("err", window.layout.getScene().getWindow());
		confirm = new Popup("conf", window.layout.getScene().getWindow());
		formValidation = new ValidateForm(window.layout, db, err);
		stage.setScene(scene);
		
		window.homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	loadHomePage();	     
            }
        });
		window.logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	loggedIn = false;
            	currentUser.loggedOff();
            	window.userLogout();
            	loadHomePage();
            }
        });
		stage.show();
	}
	
	public void loadHomePage(){
		homeView.buildStage(window, loggedIn);
		//if the login button is clicked the view is switched to the login page
		homeView.loginBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	loadLoginPage();
	        }
	    });
		//if the sign up button is clicked the view is switched to the sign up page
		homeView.signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	loadRegistrationPage();
	        }
	    });
		if(loggedIn){
			homeView.addRestaurantBtn.setOnAction(new EventHandler<ActionEvent>(){
		        @Override
		        public void handle(ActionEvent e) {
		        	loadAddRestaurantPage();
		        }
			});
		}
	}
	
	public void loadRegistrationPage(){
		regView.buildStage(window, formFieldWidth, formFieldHeight);
		regView.backBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		loadHomePage();
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
	
	public void loadAddRestaurantPage(){
		restaurantRegView.buildStage(window, formFieldWidth, formFieldHeight);
		restaurantRegView.registerBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String name = restaurantRegView.nameField.getText();
	        	String phone = restaurantRegView.phoneField.getText();
	        	String addrs = restaurantRegView.addressField.getText();
	        	String zip = restaurantRegView.zipcodeField.getText();
	    		if(formValidation.validRestaurantReg(name, addrs, zip, phone)){
	    			currentRes = new Restaurant(name, phone);
	    			currentRes.addAddress(addrs, zip);
	    			db.insertRestaurant(currentRes);
	    			loadRestaurantPage();
	    		}
	        }
		});
	}
	
	public void loadRestaurantPage(){
		resView.buildStage(currentRes, window);
		resView.addRatingBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		String rating = resView.ratingField.getText();
	    		currentRes.addRating(Integer.parseInt(rating));
	    		loadRestaurantPage();
	        }		
		});
	}
	public void loadLoginPage(){
		loginView.buildStage(window, formFieldWidth, formFieldHeight);
		loginView.backBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	    		loadHomePage();
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
	        	currentUser = db.verifyUser(enteredUsername, enteredPassword);
	        	if(currentUser != null){
	        		loginUser(enteredUsername);
	        	}
	        	else{
	        		err.showAlert("Error form.", "User not found with that password.");	        		
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
		loadHomePage();		
	}

}
