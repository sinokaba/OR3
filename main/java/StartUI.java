package main.java;

import javafx.application.Application;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class StartUI extends Application{
	/**
	* The Global variables associated with the user interface
	*/
	int numGridChildren = 0;				//keeps count of the number of elements in the gridPane
	int gridWidth = 800;					//sets the initial width of the window
	int gridHeight = 600;					//sets the initial height of the window
	double inputFieldWidth = gridWidth*.3;	//sets the size of the input field in relation to window size

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

		Label sceneTitle = new Label("OR3");
		//System.out.println(sceneTitle.getFont().getFamilies());
		
		sceneTitle.setFont(Font.font("Lucida Handwriting", FontWeight.EXTRA_BOLD, 50));
		sceneTitle.getStyleClass().add("title");
		grid.add(sceneTitle, 1, 0);
		sceneTitle.setAlignment(Pos.TOP_CENTER);
		
		numGridChildren += 1;
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		startPage(grid, primaryStage);		
		//old image used
        //https://mir-s3-cdn-cf.behance.net/project_modules/1400/4df1ee33482977.56acb21f80216.jpg
		String backgroundUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/8712449/gbb_food.jpg";
        grid.setStyle("-fx-background-image: url('"+backgroundUrl+"');");
		Scene scene = new Scene(grid, gridWidth, gridHeight);	
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
	
		//pane is empty, acts as buffer for layout other elements
	    Pane buffer = new Pane();					
		grid.add(buffer, 2, 0);
		
		stage.setTitle("OR3 - Welcome!");
		
		TextField searchField = new TextField();
		searchField.setPrefWidth(gridWidth*.6);
		searchField.setPrefHeight(35);

		Button goBtn = new Button("Search");
		goBtn.getStyleClass().add("searchButton");
		goBtn.setMinHeight(18);
		goBtn.setMinWidth(30);
		
		//hbox lays out its children in a single row, for formatting
		HBox searchWrap = new HBox(5);				
		searchWrap.getChildren().add(searchField);
		searchWrap.getChildren().add(goBtn);
		grid.add(searchWrap, 1, 1);
				
		Button loginBtn = new Button("Login");
		HBox hbBtn = new HBox(30);
		loginBtn.setMinHeight(40);
		loginBtn.setMinWidth(85);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(loginBtn);
	
		Button signUpBtn = new Button("Sign Up");
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
		
		Label userName = new Label("User Name/Email:");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		TextField userTextField = new TextField();
		userTextField.setPrefWidth(inputFieldWidth);
		userTextField.setPromptText("Enter your Username or email...");
		grid.add(userTextField, 1, 2);
		
		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPromptText("Enter your password...");
		pwBox.setPrefWidth(inputFieldWidth);
		grid.add(pwBox, 1, 3);

		Button loginBtn = new Button("Login");
		HBox hbBtn = new HBox(10);
		loginBtn.setMinHeight(35);
		loginBtn.setMinWidth(50);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginBtn);
		grid.add(hbBtn, 1, 7);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		backBtn.setMinHeight(35);
		backBtn.setMinWidth(50);
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
		stage.setTitle("OR3 - Create Account");
		
		Label userName = new Label("User Name:");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		TextField userTextField = new TextField();
		userTextField.setPromptText("Choose a unique username...");
		userTextField.setPrefWidth(inputFieldWidth);
		grid.add(userTextField, 1, 2);
		
		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPromptText("Choose a unique password...");
		pwBox.setPrefWidth(inputFieldWidth);
		grid.add(pwBox, 1, 3);

		Label email = new Label("Email:");
		email.getStyleClass().add("field");
		grid.add(email, 0, 4);
		TextField emailTextField = new TextField();
		emailTextField.setPromptText("Enter your email address...");
		emailTextField.setPrefWidth(inputFieldWidth);
		grid.add(emailTextField, 1, 4);

		Label zipcode = new Label("Zipcode:");
		zipcode.getStyleClass().add("field");
		grid.add(zipcode, 0, 5);
		TextField zipTextField = new TextField();
		zipTextField.setPromptText("Enter your zipcode...");
		zipTextField.setPrefWidth(60);
		grid.add(zipTextField, 1, 5);	
		
	
		HBox birthdayWrapper = new HBox(10);
		
		Label birthday = new Label("Birthday:");
		birthday.getStyleClass().add("field");
		grid.add(birthday, 0, 6);
		
		/*
		TextField month = new TextField();
		month.setPromptText("Month");
		month.setPrefWidth(75);
				
		TextField day = new TextField();
		day.setPromptText("Day");
		day.setPrefWidth(75);
		
		TextField year = new TextField();
		year.setPromptText("Year");
		year.setPrefWidth(75);
		*/
		
		ObservableList<String> months = 
			    FXCollections.observableArrayList(
			        "01",
			        "02",
			        "03",
			        "04",
			        "05",
			        "06",
			        "07",
			        "08",
			        "09",
			        "10",
			        "11",
			        "12"			        
			    );
		ObservableList<String> years = FXCollections.observableArrayList();
		for(int i = 2012; i > 1910; i--){
			years.add(String.valueOf(i));
		}
		
		ObservableList<String> days = FXCollections.observableArrayList();
		for(int i = 1; i < 32; i++){
			days.add(String.valueOf(i));
		}
		final ComboBox monthsDropdown = new ComboBox(months);
		monthsDropdown.setValue("Month");
		monthsDropdown.setVisibleRowCount(9);
        
		final ComboBox daysDropdown = new ComboBox(days);
		daysDropdown.setValue("Day");
		daysDropdown.setVisibleRowCount(9);
		
		final ComboBox yearsDropdown = new ComboBox(years);
		yearsDropdown.setValue("Year");
		yearsDropdown.setVisibleRowCount(9);
        		
        /*
		birthdayWrapper.getChildren().add(month);
		birthdayWrapper.getChildren().add(day);
		birthdayWrapper.getChildren().add(year);
		grid.add(birthdayWrapper, 1, 6);
		*/
		
		birthdayWrapper.getChildren().add(monthsDropdown);
		birthdayWrapper.getChildren().add(daysDropdown);
		birthdayWrapper.getChildren().add(yearsDropdown);
		
		grid.add(birthdayWrapper, 1, 6);
		
		Button signUpBtn = new Button("Sign up!");
		HBox hbBtn = new HBox(10);
		signUpBtn.setMinHeight(35);
		signUpBtn.setMinWidth(100);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(signUpBtn);
		grid.add(hbBtn, 1, 7);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		backBtn.setMinHeight(35);
		backBtn.setMinWidth(50);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		grid.add(hbBackBtn, 0, 7);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
        grid.add(actionTarget, 1, 9, 7, 1);
        
        numGridChildren += 13;
        
        signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	boolean formComplete = true;
            	for (Node child : grid.getChildren()) {
            	    Integer column = GridPane.getColumnIndex(child);
            	    Integer row = GridPane.getRowIndex(child);
            	    if(child instanceof TextField){
            	    	if(((TextField)child).getText().equals("")){
            	    		formComplete = false;
                        	break;
            	    	}
            	    }
            	    else if(child instanceof HBox){
            	    	for(Node n : ((HBox)child).getChildren()){
            	    		if(n instanceof ComboBox){
	                	    	String selectedVal = ((ComboBox)n).getSelectionModel().getSelectedItem().toString();
	                	    	if(selectedVal.equals("Month") || selectedVal.equals("Day") || selectedVal.equals("Year")){
	                	    		formComplete = false;
	                	    		break;
	                	    	}
	                	    	else{
	                	    		System.out.println("cb selected value: " + selectedVal);
	                	    	}          
            	    		}
            	    	}
            	    }
            	}
            	if(formComplete){
            		actionTarget.setFill(Color.LIMEGREEN);
                	actionTarget.setText("We gucci.");                	            		
            	}
            	else{
                	actionTarget.setFill(Color.FIREBRICK);
                	actionTarget.setText("Please complete the form.");
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
	
	/**
	* This method starts the application, the main method
	*/
	public static void main(String[] args){
		launch(args);
	}
	
}
