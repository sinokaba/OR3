package main.java;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UIMediator extends Application{

	private Stage primaryStage;
	private Scene scene;
	final int WINDOW_HEIGHT = 600;
	final int WINDOW_WIDTH = 800;
	//private StartUI startPage;
	private AppWindow window;	
	private	HomepageUI homeView;
	private RegistrationUI regView;
	private LoginUI loginView;
	
	private int formFieldWidth = 258;
	private int formFieldHeight = 38;
	
	
	public UIMediator(){
		window = new AppWindow();
		homeView = new HomepageUI();
		regView = new RegistrationUI();
		loginView = new LoginUI();
	}
	
	
	public void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage) throws Exception{
		loadHomePage();
		scene = new AppScene(window.root, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage.setScene(scene);
		
		window.homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	loadHomePage();	     
            }
        	
        });
		
		stage.show();
	}
	
	public void loadHomePage(){
		homeView.buildStage(window);
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
	        	//
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
	        	//regView.buildStage(window, formFieldWidth, formFieldHeight);
	        }
	    });
	}

}
