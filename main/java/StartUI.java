package main.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartUI extends Application{
	/**
	* The Global variables associated with the user interface
	*/
	int numGridChildren = 0;				//keeps count of the number of elements in the gridPane
	int gridWidth = 800;					//sets the initial width of the window
	int gridHeight = 600;					//sets the initial height of the window
	double inputFieldWidth = gridWidth*.3;	//sets the size of the input field in relation to window size
	final static Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	final static Pattern VALID_PASSWORD_REGEX = 
			Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,}$");
	final static Pattern VALID_ZIPCODE_REGEX =
			Pattern.compile("^(?=.*[0-9]).{5,5}$");			
	final static DBConnection db =
			new DBConnection("jdbc:mysql://localhost:3306/2102_or3?autoReconnect=true&useSSL=false", "root", "allanK0_ph");
	//^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,}$
	/**
	* This method creates the primary stage and the scene of the UI.
	* It uses a grid to structure everything, sets the title of the window, and its background image
	* 
	* @param takes one parameter of class Stage
	* @return no return value
	*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("OR3 - Review, Rate, Dine.");
		
		GridPane grid = new GridPane();
        
        Image userIcon = new Image(getClass().getResourceAsStream("/static/images/user.png"));
        ImageView userView = new ImageView(userIcon);
        userView.setFitWidth(44);
        userView.setFitHeight(44);
               
        Image brandIcon = new Image(getClass().getResourceAsStream("/static/images/brand.png"));
        ImageView brandView = new ImageView(brandIcon);
        brandView.setFitWidth(44);
        brandView.setFitHeight(44);
        
        Button homeBtn = new Button("OR3", brandView);
        homeBtn.getStyleClass().add("menuButton");
        Button userBtn = new Button("", userView);
        userBtn.getStyleClass().add("menuButton");
        
        /*
        MenuButton userBtn = new MenuButton("", userView);
        userBtn.getStyleClass().add("menuButton");
        userBtn.getItems().addAll(new MenuItem("Account details"), new MenuItem("Messages"), new MenuItem("Logout"));
        */
        
        ToolBar leftBar = new ToolBar();
        leftBar.getItems().addAll(homeBtn);
        ToolBar rightBar = new ToolBar();
        rightBar.getItems().addAll(userBtn);
        
        leftBar.getStyleClass().add("menu-bar");
        rightBar.getStyleClass().add("menu-bar");
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        
        BorderPane root = new BorderPane();
        root.setTop(menubars);
		Label sceneTitle = new Label("OR3");
		sceneTitle.getStyleClass().add("title");
		
		grid.add(sceneTitle, 1, 0);
		sceneTitle.setAlignment(Pos.TOP_CENTER);
		
		numGridChildren += 1;
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		startPage(grid, primaryStage);
		
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	System.out.println("go back home.");
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;            	
        		startPage(grid, primaryStage);		     
            }
        	
        });
		//old image used
        //https://mir-s3-cdn-cf.behance.net/project_modules/1400/4df1ee33482977.56acb21f80216.jpg
		String backgroundUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/8712449/gbb_food.jpg";
        root.setStyle("-fx-background-image: url('"+backgroundUrl+"');");
        root.setCenter(grid);
        Scene scene = new Scene(root, gridWidth, gridHeight);	
		
        //links the css for styling the different views		
        scene.getStylesheets().addAll(getClass().getResource(	
                "/static/css/home.css"
        ).toExternalForm());
        
		primaryStage.setScene(scene);
		
		primaryStage.show();	
	}
	
	/**
	* This method creates the layout for the window that loads when the user initially starts the application
	* Contains 1 search bar, the signup and login button, and edits the title of the window
	* 
	* @param takes 2 input arguments, an instance of the GridpPane javafx class, and the stage
	* @return no return value
	*/
	public void startPage(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Welcome!");	
		//pane is empty, acts as buffer for layout other elements
	    Pane buffer = new Pane();					
		grid.add(buffer, 2, 0);
		
		ObservableList<String> searchChoices = 
			    FXCollections.observableArrayList(
			        "Name",
			        "Type",
			        "Food",
			        "Keyword"			        
			    );
		DropdownMenu searchDropdown = new DropdownMenu(null, 4, searchChoices, 0, 0, true);
		searchDropdown.addClass("mainSearchDropdown");
		
		CustomTextField restaurantSearchField = new CustomTextField("of Restaurant...", 42, 315);

        searchDropdown.setOnAction((e) -> {
        	String currentSelectedItem = searchDropdown.getSelectionModel().getSelectedItem().toString();
            if(currentSelectedItem.equals("Name") || currentSelectedItem.equals("Type")){
            	restaurantSearchField.setPromptText("of Restaurant...");
            }
            else if(currentSelectedItem.equals("Food")){
            	restaurantSearchField.setPromptText("called or category...");
            }
            else{
            	restaurantSearchField.setPromptText("for whatever I feel like...");            	
            }
            
        });
        
		CustomTextField locationSearchField = new CustomTextField("US state, city, or zipcode.", 42, 210);
		
		Button goBtn = new Button("Search");
		goBtn.getStyleClass().add("searchButton");
		
		//hbox lays out its children in a single row, for formatting
		HBox searchWrap = new HBox(3);				
		searchWrap.getChildren().add(searchDropdown);
		searchWrap.getChildren().add(restaurantSearchField);
		searchWrap.getChildren().add(locationSearchField);
		searchWrap.getChildren().add(goBtn);
		grid.add(searchWrap, 1, 1);
				
		Button loginBtn = new Button("Login");
		Button signUpBtn = new Button("Sign Up");
		
		HBox hbBtn = new HBox(30);		
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(loginBtn);
		hbBtn.getChildren().add(signUpBtn);
		
		grid.add(hbBtn, 1, 6);
		
		numGridChildren += 3;
		
		//if the login button is clicked the view is switched to the login page
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	//gets rid of all the elements in grid except for title
        		grid.getChildren().remove(1, numGridChildren);
        			//resets number of children to 1
	        		numGridChildren = 1;						
	            	login(grid, stage);
            }
        });
		
		//if the sign up button is clicked the view is switched to the sign up page
		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	//gets rid of all the elements in grid except for title
        		grid.getChildren().remove(1, numGridChildren);	
        		//resets the number of children to 1
        		numGridChildren = 1;							
            	signUp(grid, stage);
            }
        });
	}
	
	/**
	* This method creates the layout for the login page
	* Contains 2 input fields, name and password, and 2 buttons, back and login
	* 
	* @param takes 2 input arguments, an instance of the GridpPane javafx class, and the stage
	* @return no return value
	*/
	public void login(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Login");
		
		Label userName = new Label("Username");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		CustomTextField userTextField = new CustomTextField("Enter your Username.", 38, 258);
		userTextField.addCharLimit(16);
		grid.add(userTextField, 1, 2);
		
		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPromptText("Enter your password.");
		pwBox.getStyleClass().add("formField");
		grid.add(pwBox, 1, 3);

		Button loginBtn = new Button("Login");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginBtn);
		grid.add(hbBtn, 1, 7);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		grid.add(hbBackBtn, 0, 7);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
        grid.add(actionTarget, 1, 9, 7, 1);

        //increments numGridChildren based on number of children added in this method
		numGridChildren += 7;								
		
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
        	//if login button is clicked several messages pop up depending on the input
        	//will add verification and real functionality in the future
            @Override
            public void handle(ActionEvent e) {
            	actionTarget.setFill(Color.FIREBRICK);
                String enteredName = userTextField.getText();
                if(enteredName.equals("")){
                	actionTarget.setText("Please enter a valid username!");
                }
                else if(pwBox.getText().equals("")){
                	actionTarget.setText("Please enter a valid password.");
                }
                else{
                	actionTarget.setText("Sign in button pressed");                	
                }
                System.out.println(pwBox);
            }
        });
        
        backBtn.setOnAction(new EventHandler<ActionEvent>(){
        	//if back button is clicked the screen will return to start page
        	@Override
        	public void handle(ActionEvent e){
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
        		startPage(grid, stage);
        	}
        });		
	}
	
	/**
	* This method creates the layout for the sign up page
	* Contains 3 input fields, name, email and password, and 2 buttons, back and sign up
	* Makes use of the same elements as the previous 2 classes so will not be explicitly mentioned here
	* Will probably create a separate class in the future for the ui, since so many attributes and functionality are the same
	* 
	* @param takes 2 input arguments, an instance of the GridpPane javafx class, and the stage
	* @return no return value
	*/
	public void signUp(GridPane grid, Stage stage){
		int fieldW = 258;
		int fieldH = 38;
		
		stage.setTitle("OR3 - Create Account");
		
		Label userName = new Label("Username:");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		CustomTextField userTextField = new CustomTextField("Your unique username.", fieldH, fieldW);
		userTextField.addCharLimit(16);
		grid.add(userTextField, 1, 2);
		
		Label email = new Label("Email:");
		email.getStyleClass().add("field");
		grid.add(email, 0, 3);
		CustomTextField emailTextField = new CustomTextField("Enter your email address.", fieldH, fieldW);
		grid.add(emailTextField, 1, 3);
		
		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 4);
		PasswordField pwBox = new PasswordField();
		pwBox.setPromptText("Your unique password.");
		pwBox.getStyleClass().add("formField");
		grid.add(pwBox, 1, 4);
		
		Label pwVerify = new Label("Password:");
		pwVerify.getStyleClass().add("field");
		grid.add(pwVerify, 0, 5);
		PasswordField pwBoxV = new PasswordField();
		pwBoxV.setPromptText("Verify and re-enter your password.");
		pwBoxV.getStyleClass().add("formField");
		grid.add(pwBoxV, 1, 5);

		Label zipcode = new Label("Zipcode:");
		zipcode.getStyleClass().add("field");
		grid.add(zipcode, 0, 6);
		CustomTextField zipTextField = new CustomTextField("Enter your 5 digit zipcode.", fieldH, fieldW);
		zipTextField.addCharLimit(5);
		grid.add(zipTextField, 1, 6);	
	
		HBox birthdayWrapper = new HBox(10);
		
		Label birthday = new Label("Birthday:");
		birthday.getStyleClass().add("field");
		grid.add(birthday, 0, 7);
		
		DropdownMenu monthsDropdown = new DropdownMenu("Month", 9, null, 1, 12, true);
		DropdownMenu daysDropdown = new DropdownMenu("Day", 9, null, 1, 31, true);
		DropdownMenu yearsDropdown = new DropdownMenu("Year", 9, null, 1920, 2017, false);
		
		birthdayWrapper.getChildren().add(monthsDropdown);
		birthdayWrapper.getChildren().add(daysDropdown);
		birthdayWrapper.getChildren().add(yearsDropdown);
		
		grid.add(birthdayWrapper, 1, 7);
		
		Button signUpBtn = new Button("Sign up!");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(signUpBtn);
		grid.add(hbBtn, 1, 8);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		grid.add(hbBackBtn, 0, 8);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
        grid.add(actionTarget, 1, 10, 4, 1);
        
        numGridChildren += 15;
        
        signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	String email = emailTextField.getText();
            	String name = userTextField.getText();
            	String pw = pwBox.getText();
            	String pw2 = pwBoxV.getText();
            	String zip = zipTextField.getText();
            	boolean valid = validateForm(pw, pw2, name, email, zip, grid);
            	if(valid){
            		actionTarget.setFill(Color.LIMEGREEN);
            		actionTarget.setText("We gucci");
            		String bDay = yearsDropdown.getValue()
            					+ "-" + monthsDropdown.getValue()
            					+ "-" + daysDropdown.getValue();
            		User newUser = new User(name, pw, bDay, email, zip, 0);
            		db.insertUser(newUser);
            	}
            	else{
            		actionTarget.setFill(Color.FIREBRICK);
            		actionTarget.setText("Invalid Credentials Entered.");
            	}
            }
        });
        
        backBtn.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
        		startPage(grid, stage);
        	}
        });
	}

	public static boolean validateForm(String pw, String pwRe, String name, String email, String zip, GridPane grid){
    	//Tooltip tp = new Tooltip("Invalid password. Must be at least 6 chars long and have 1 number and uppercase letter.");;
    	if(!noEmptyFields(grid)){
    		return false;
    	}
		if(!validPassword(pw)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Invalid password. Password must have 6 or more characters, and have at least 1 capital letter and 1 digit.");;    
    	    return false;
    	}         
    	else if(!pw.equals(pwRe)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Passwords don't match.");;    
    	    return false;    		
    	}
    	else if(db.rowExists("users", "username", name)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Username: " + name + ", already taken. Please choose a different one.");;    
    	    return false;       		
    	}
    	else if(!validEmail(email)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Email address: " + email + ", is invalid.");;
    	    return false;
    	}
    	else if(db.rowExists("users", "email", email)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Email address: " + email + ", already associated with another account.");;
    	    return false;    		
    	}
    	else if(!validZipcode(zip)){
    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	            "Form Error!", "Invalid Zipcode. Zipcode entered must be within the USA.");;    
	    	return false;
		}
    	return true;    
	}
	
	public static boolean noEmptyFields(GridPane grid){
		for (Node child : grid.getChildren()) {
    	    if(child instanceof TextField){
    	    	String userInput = ((TextField)child).getText();
    	    	if(userInput.trim().length() <= 0){
    	    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	    	            "Form Error!", "All form fields must be completed.");;     	    		
                	return false;
    	    	}
    	    	else if(userInput.contains(" ")){
    	    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
    	    	            "Form Error!", "No white spaces please.");;    
    	    	    return false;            	    		
    	    	}
    	    }
    	    else if(child instanceof HBox){
    	    	for(Node n : ((HBox)child).getChildren()){
    	    		if(n instanceof ComboBox){
            	    	String selectedVal = ((ComboBox<String>)n).getSelectionModel().getSelectedItem().toString();
            	    	if(selectedVal.equals("Month") || selectedVal.equals("Day") || selectedVal.equals("Year")){
            	    		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), 
            	    	            "Form Error!", "Please enter your birthday.");;     	    		
            	    		return false;
            	    	}   
    	    		}
    	    	}
    	    }
    	}
		return true;
	}
	
	public static boolean validEmail(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.find();
	}

	public static boolean validPassword(String pw) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pw);
        return matcher.find();
	}
	
	public static boolean validZipcode(String zipcode){
		Matcher matcher = VALID_ZIPCODE_REGEX.matcher(zipcode);
		return matcher.find();
	}
	
	private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.initOwner(owner);
	    alert.show();
	}
	/**
	* This method starts the application, the main method
	*/
	public static void main(String[] args){
		launch(args);
		db.disconnect();
	}
	
}
