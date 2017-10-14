package main.java;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class StartUI extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("OR3 - Review, Rate, Dine.");
		
		int gridWidth = 550;
		int gridHeight = 400;
		GridPane grid = new GridPane();
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		startPage(grid, primaryStage);
		
        grid.setStyle("-fx-background-color: #3498db");
		Scene scene = new Scene(grid, gridWidth, gridHeight);
		primaryStage.setScene(scene);
		
		primaryStage.show();	
	}
	
	public void startPage(GridPane grid, Stage stage){
		
	    Pane spring = new Pane(); //empty, acts as buffer
		grid.add(spring, 2, 0);
		
		stage.setTitle("OR3 - Welcome!");
		Label scenetitle = new Label("OR3");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 26));
		grid.add(scenetitle, 1, 0);
		scenetitle.setAlignment(Pos.TOP_CENTER);
		

		TextField searchField = new TextField();
		searchField.setPrefWidth(285);
		searchField.setPrefHeight(35);

		Button goBtn = new Button("Search");
		goBtn.setStyle("-fx-background-color: #c0392b;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 12pt");
		goBtn.setMinHeight(18);
		goBtn.setMinWidth(30);
		
		HBox searchWrap = new HBox(5);
		searchWrap.getChildren().add(searchField);
		searchWrap.getChildren().add(goBtn);
		grid.add(searchWrap, 1, 1);
		
		Button loginBtn = new Button("Login");
		HBox hbBtn = new HBox(10);
		loginBtn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 13.5pt");
		loginBtn.setMinHeight(40);
		loginBtn.setMinWidth(85);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(loginBtn);
	
		Button signUpBtn = new Button("Sign Up");
		signUpBtn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 13.5pt");
		hbBtn.getChildren().add(signUpBtn);
		
		grid.add(hbBtn, 1, 4);
		
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
        		grid.getChildren().clear();
            	login(grid, stage);
            }
        });

		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
        		grid.getChildren().clear();
            	signUp(grid, stage);
            }
        });
	}
	
	public void login(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Login");
		Label scenetitle = new Label("OR3");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 26));
		grid.add(scenetitle, 1, 0);
		scenetitle.setAlignment(Pos.TOP_CENTER);
		
		Label userName = new Label("User Name or Email:");
		userName.setStyle("-fx-font-size: 12pt");
		grid.add(userName, 0, 2);
		TextField userTextField = new TextField();
		userTextField.setPrefWidth(185);
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		pw.setStyle("-fx-font-size: 12pt");
		grid.add(pw, 0, 3);
		PasswordField pwBox = new PasswordField();
		pwBox.setPrefWidth(185);
		grid.add(pwBox, 1, 3);

		Button btn = new Button("Login");
		HBox hbBtn = new HBox(10);
		btn.setMinHeight(35);
		btn.setMinWidth(50);
		btn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 12pt");
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 5);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		backBtn.setMinHeight(35);
		backBtn.setMinWidth(50);
		backBtn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 12pt");
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		grid.add(hbBackBtn, 0, 5);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
        grid.add(actionTarget, 1, 7, 7, 1);

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
        		grid.getChildren().clear();
        		startPage(grid, stage);
        	}
        });		
	}
	
	public void viewRestaurants(){
		
	}
	
	public void signUp(GridPane grid, Stage stage){
		stage.setTitle("OR3 - Create Account");
		Label scenetitle = new Label("OR3");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 26));
		grid.add(scenetitle, 1, 0);
		scenetitle.setAlignment(Pos.TOP_CENTER);
		
		Label userName = new Label("User Name:");
		grid.add(userName, 0, 2);
		userName.setStyle("-fx-font-size: 12pt");
		TextField userTextField = new TextField();
		userTextField.setPrefWidth(185);
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 3);
		pw.setStyle("-fx-font-size: 12pt");
		PasswordField pwBox = new PasswordField();
		pwBox.setPrefWidth(185);
		grid.add(pwBox, 1, 3);

		Label email = new Label("Email:");
		grid.add(email, 0, 4);
		email.setStyle("-fx-font-size: 12pt");
		TextField emailTextField = new TextField();
		emailTextField.setPrefWidth(185);
		grid.add(emailTextField, 1, 4);
		
		
		Button btn = new Button("Sign up");
		HBox hbBtn = new HBox(10);
		btn.setMinHeight(35);
		btn.setMinWidth(50);
		btn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 12pt");
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 5);
	
		Button backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		backBtn.setMinHeight(35);
		backBtn.setMinWidth(50);
		backBtn.setStyle("-fx-background-color: #2c3e50;"+"-fx-text-fill: #ffffff;"+"-fx-font-size: 12pt");
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		grid.add(hbBackBtn, 0, 5);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
        grid.add(actionTarget, 1, 7, 7, 1);
        
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
        		grid.getChildren().clear();
        		startPage(grid, stage);
        	}
        });
	}
	public static void main(String[] args){
		launch(args);
	}
	
}
