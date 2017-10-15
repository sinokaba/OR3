package main.java;

import javafx.application.Application;
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
	int numGridChildren = 0;
	int gridWidth = 800;
	int gridHeight = 600;
	double inputFieldWidth = gridWidth*.3;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("OR3 - Review, Rate, Dine.");
		
		GridPane grid = new GridPane();

		Label sceneTitle = new Label("OR3");
		System.out.println(sceneTitle.getFont().getFamilies());
		
		sceneTitle.setFont(Font.font("Lucida Handwriting", FontWeight.EXTRA_BOLD, 50));
		sceneTitle.getStyleClass().add("title");
		grid.add(sceneTitle, 1, 0);
		sceneTitle.setAlignment(Pos.TOP_CENTER);
		
		numGridChildren += 1;
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		startPage(grid, primaryStage);
        //https://mir-s3-cdn-cf.behance.net/project_modules/1400/4df1ee33482977.56acb21f80216.jpg
		String backgroundUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/8712449/gbb_food.jpg";
        grid.setStyle("-fx-background-image: url('"+backgroundUrl+"');");
		Scene scene = new Scene(grid, gridWidth, gridHeight);
        scene.getStylesheets().addAll(getClass().getResource(
                "/static/css/home.css"
        ).toExternalForm());
		primaryStage.setScene(scene);
		
		primaryStage.show();	
	}
	
	public void startPage(GridPane grid, Stage stage){
		
	    Pane spring = new Pane(); //empty, acts as buffer
		grid.add(spring, 2, 0);
		
		stage.setTitle("OR3 - Welcome!");
		
		TextField searchField = new TextField();
		searchField.setPrefWidth(gridWidth*.6);
		searchField.setPrefHeight(35);

		Button goBtn = new Button("Search");
		goBtn.getStyleClass().add("searchButton");
		goBtn.setMinHeight(18);
		goBtn.setMinWidth(30);
		
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
		/**
		Button goBtn = new Button("Search");
		HBox hbGoBtn = new HBox(10);
		goBtn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 13pt");
		goBtn.setMinHeight(50);
		goBtn.setMinWidth(80);
		hbGoBtn.setAlignment(Pos.CENTER_LEFT);
		hbGoBtn.getChildren().add(goBtn);
		grid.add(hbGoBtn, 1, 2);
		**/
		
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
            	login(grid, stage);
            }
        });

		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
            	signUp(grid, stage);
            }
        });
	}
	
	public void login(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Login");
		
		Label userName = new Label("User Name or Email:");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		TextField userTextField = new TextField();
		userTextField.setPrefWidth(inputFieldWidth);
		grid.add(userTextField, 1, 2);
		
		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPrefWidth(inputFieldWidth);
		grid.add(pwBox, 1, 3);

		Button btn = new Button("Login");
		HBox hbBtn = new HBox(10);
		btn.setMinHeight(35);
		btn.setMinWidth(50);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
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

		numGridChildren += 7;
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	 
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
        	@Override
        	public void handle(ActionEvent e){
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
        		startPage(grid, stage);
        	}
        });		
	}
	
	public void viewRestaurants(){
		
	}
	
	public void signUp(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Create Account");
		
		Label userName = new Label("User Name:");
		userName.getStyleClass().add("field");
		grid.add(userName, 0, 2);
		TextField userTextField = new TextField();
		userTextField.setPrefWidth(inputFieldWidth);
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		pw.getStyleClass().add("field");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPrefWidth(inputFieldWidth);
		grid.add(pwBox, 1, 3);

		Label email = new Label("Email:");
		email.getStyleClass().add("field");
		grid.add(email, 0, 4);
		TextField emailTextField = new TextField();
		emailTextField.setPrefWidth(inputFieldWidth);
		grid.add(emailTextField, 1, 4);
		
		Button btn = new Button("Sign up");
		HBox hbBtn = new HBox(10);
		btn.setMinHeight(35);
		btn.setMinWidth(50);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
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
        
        numGridChildren += 9;
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	 
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
        	@Override
        	public void handle(ActionEvent e){
        		grid.getChildren().remove(1, numGridChildren);
        		numGridChildren = 1;
        		startPage(grid, stage);
        	}
        });
	}
	public static void main(String[] args){
		launch(args);
	}
	
}
