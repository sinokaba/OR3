
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UIController extends Application{
	
	private BorderPane root;
	private Stage primaryStage;
	private AppScene currentScene;
	
	private LoginUI loginPage;
	private HomeUI homePage;
	private UserRegistrationUI userRegPage;
	private SearchResultUI searchResultPage;
	private RestaurantRegUI rstRegPage;
	private RestaurantUI rstPage;
	private UserAccountUI userPage;
	private Navbar nav;
	
	private DBConnection db;
	private GoogleMapsService mapsApi;
	private GoogleMap map;
	private Restaurant currentRst;
	private User currentUser;
	private ValidateForm formValidation;
	private Popup errDialog, confirmDialog;
	private PasswordEncryption pwEncrypt;
	private EmailGenerator emailGen;
	
	private final int FIELD_WIDTH = 258;
	private final int FIELD_HEIGHT = 38;
	final private Pattern GOOD_LOCATION_INPUT = 
			Pattern.compile("^[a-zA-Z0-9]*.{2,}$");
	
	public UIController(){
		root = new BorderPane();
		currentScene = new AppScene(root);
		nav = new Navbar();
		root.setTop(nav.createWindowMenu());
		
		mapsApi = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		map = new GoogleMap();
		db = new DBConnection(mapsApi);		
		pwEncrypt = new PasswordEncryption();
		emailGen = new EmailGenerator();
		
		errDialog = new Popup("err", currentScene.getWindow());
		formValidation = new ValidateForm(db, errDialog);
		confirmDialog = new Popup("conf", currentScene.getWindow());
		
		rstPage = new RestaurantUI(db, currentScene.getWindow(), map);
		loginPage = new LoginUI(FIELD_WIDTH, FIELD_HEIGHT, currentScene.getWindow(), formValidation, db, this);
		homePage = new HomeUI(false, db, mapsApi);
		userRegPage = new UserRegistrationUI(FIELD_WIDTH, FIELD_HEIGHT);
		searchResultPage = new SearchResultUI(db, this, map);
		rstRegPage = new RestaurantRegUI(mapsApi, FIELD_WIDTH, FIELD_HEIGHT);
		userPage = new UserAccountUI(db, currentScene.getWindow(), confirmDialog, this);
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("/images/brand.png"));
		nav.logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	confirmDialog.createDialog("Logout", "Are you sure you want to log out?");
            	if(confirmDialog.userConfirmation().get() == ButtonType.OK){
            		logoutUser();
            	}
            }
		});
		nav.userMenuActions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if(oldValue != null && oldValue.equals(newValue)){
		        	newValue = oldValue;
		        }
		    	if(newValue.toLowerCase().equals("add restaurant")){
		        	nav.userMenuActions.getSelectionModel().clearSelection();
		    		nav.userMenuActions.getSelectionModel().clearAndSelect(0);
		    		restaurantRegView();
		        }
		    	else if(newValue.toLowerCase().equals("account")){
		        	nav.userMenuActions.getSelectionModel().clearSelection();
		    		nav.userMenuActions.getSelectionModel().clearAndSelect(0);
		    		userAcctView(currentUser);
		    	}
		    }
		});
		nav.signupBtn.setOnAction(e -> {
			clearAllFields();
			userRegView();
		});
		nav.homeBtn.setOnAction(e -> {
			clearAllFields();
			homeView();
		});
		nav.loginBtn.setOnAction(e -> {
			clearAllFields();
			loginView();
		});
		
		homeView();
		primaryStage.setScene(currentScene);
		primaryStage.show();
	}
	
	public void homeView(){
		homePage.clearFields();
		primaryStage.setTitle("Welcome - OR3");
		root.setCenter(homePage.layout);
		System.out.print("num kids: " + root.getChildren().size());
		homePage.searchBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				System.out.println("searching...");
				String searchTerm = homePage.restaurantSearchField.getText().trim();
				String specifiedLoc = homePage.locationSearchField.getText().trim();
				if(searchTerm.length() > 0 || specifiedLoc.length() > 3){
					if(specifiedLoc.length() <= 2){
						specifiedLoc = null;
					}
					if(searchTerm.length() > 0){
						//if(homePage.searchDropdown.getSelectionModel().getSelectedItem().equals("Name")){
			    		Restaurant dbQueryRes = db.getRestaurantFromDB(searchTerm, specifiedLoc);
						if(dbQueryRes != null){
							currentRst = dbQueryRes;
				    		restaurantView(currentRst);
						}
						else{
							List<String> searchRes = new ArrayList<String>();
							if(specifiedLoc == null){
								searchRes = db.getRestaurantSuggestions(searchTerm, false);
							}
							else{
								searchRes = db.getRestaurantSuggestions(searchTerm, specifiedLoc, false);
							}
							searchResultView(searchRes, searchTerm, specifiedLoc);
						}
						
					}
					else{
						searchResultView(db.getRestaurantSuggestions(searchTerm, specifiedLoc, false), searchTerm, specifiedLoc);						
					}
					homePage.clearFields();
				}
				else{
					errDialog.showAlert("Fields not completed.", "Error! The main search field must not be empty!");
				}
			}
		});
	}
	
	public void loginView(){
		loginPage.buildPage(emailGen, pwEncrypt);
		primaryStage.setTitle("Login to your account - OR3");
		root.setCenter(loginPage.getLayout());
		loginPage.backBtn.setOnAction(e -> homeView());	
	}
	
	public void userRegView(){
		userRegPage.buildPage();
		primaryStage.setTitle("Create an account - OR3");
		root.setCenter(userRegPage.layout);
		userRegPage.backBtn.setOnAction(e -> homeView());
		userRegPage.registerBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String username = userRegPage.getUsername();
	        	String email = userRegPage.getEmail();
	        	String pw = userRegPage.getPassword();
	        	String pwV = userRegPage.getPasswordV();
	        	String zip = userRegPage.getZipcode();
	        	if(formValidation.validUserRegistration(pw, pwV, username, email, zip, userRegPage.layout)){
            		@SuppressWarnings("static-access")
					byte[] salt = pwEncrypt.getSalt();
            		
	        		String hashedPw = pwEncrypt.getSecurePassword(pw, salt);
            		User newUser = new User(username, hashedPw, userRegPage.getBirthday(), email);
            		newUser.setLocation(null, null, zip);
            		db.insertUser(newUser, salt);
            		confirmDialog.showAlert("Thanks for creating an account!", "Successfully created your account!");
	        		loginView();
	        	}
	        }
	    });
	}

	public void userAcctView(User user){
		userPage.buildStage(user);
		root.setCenter(userPage.getLayout());
	
		userPage.deleteAccount.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				confirmDialog.createDialog("Delete account", "Are you sure you want to delete your account? This cannot be undone.");
				if(confirmDialog.userConfirmation().get() == ButtonType.OK){
					db.deleteUser(currentUser);
					logoutUser();
				}
			}
		});
		
	}
	
	public void restaurantRegView(){
		primaryStage.setTitle("OR3 - Add restaurant.");
		rstRegPage.buildPage();
		root.setCenter(rstRegPage.layout);
		rstRegPage.backBtn.setOnAction(e -> homeView());
		rstRegPage.registerBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String name = rstRegPage.nameField.getText();
	        	String phone = rstRegPage.phoneField.getText();
	        	String addrs = rstRegPage.addressField.getText();
	        	String zip = rstRegPage.zipcodeField.getText();
	    		if(formValidation.validRestaurantReg(name, addrs, zip, phone, rstRegPage.layout)){
	    			currentRst = new Restaurant(name, phone);
	    			currentRst.setAddress(addrs, zip);
	    			db.insertRestaurant(currentRst);
	    			restaurantView(currentRst);
	    			rstRegPage.clearFields();
	    			confirmDialog.showAlert("Success!", "Restaurant " + name + " added successfully!");
	    		}
	        }
	    });
	}
	
	public void restaurantView(Restaurant rst){
		rstPage.buildPage(rst, currentUser, db.getRestaurantReviewsFromDB(rst));
		root.setCenter(rstPage.getLayout());
	}
	
	public void searchResultView(List<String> res, String searchTerm, String specifiedLoc){
		searchResultPage.build(specifiedLoc, searchTerm, res);
		root.setCenter(searchResultPage.layout);
	}
	
	public void loginUser(User user){
		currentUser = user;
		user.loggedIn();
		String userCreds = user.getUsername();
		if(user.getPrivilege() == 1){
			userCreds = user.getUsername() + "(Admin)";
		}
		nav.userLogin(userCreds);
		homeView();		
	}
	
	public void logoutUser(){
		currentUser.loggedOff();
		currentUser = null;
		nav.userLogout();
		homeView();			
	}
	
	public void clearAllFields(){
		homePage.clearFields();
		loginPage.clearFields();
		userRegPage.clearFields();
		rstRegPage.clearFields();
	}

	
	public boolean checkLocationInput(String oldUserInput, String newUserInput) {
		if(oldUserInput.equals(newUserInput)){
			return false;
		}
        //Matcher matcher = GOOD_LOCATION_INPUT.matcher(newUserInput);
        //return matcher.find();
		return true;
	}
}
