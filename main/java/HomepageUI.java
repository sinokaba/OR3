package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HomepageUI{
	private CustomTextField restaurantSearchField, locationSearchField;
	DropdownMenu searchDropdown;
	Button searchBtn, loginBtn, signUpBtn;
	
	public void buildStage(AppWindow win){
		//pane is empty, acts as buffer for layout other elements
		win.resetLayout();
		HBox searchWrap = createSearchBar(win.layout);
		win.layout.add(searchWrap, 1, 1);
				
		loginBtn = new Button("Login");
		signUpBtn = new Button("Sign Up");
		
		HBox hbBtn = new HBox(30);		
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(loginBtn);
		hbBtn.getChildren().add(signUpBtn);
		
		win.layout.add(hbBtn, 1, 6);
		
		win.updateElementCount(3);
		/*
		//gridEleCount += 3;
		
		//if the login button is clicked the view is switched to the login page
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	//gets rid of all the elements in grid except for title
        		grid.getChildren().remove(1, gridEleCount);
        			//resets number of children to 1
        			//gridEleCount = 1;						
	            	//login(grid, stage);
            }
        });
		
		//if the sign up button is clicked the view is switched to the sign up page
		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	//gets rid of all the elements in grid except for title
        		grid.getChildren().remove(1, gridEleCount);	
        		//resets the number of children to 1
        		//gridEleCount = 1;							
            	//signUp(grid, stage);
            }
        });
        */
		
	}
	
	public HBox createSearchBar(GridPane grid){
	    Pane buffer = new Pane();					
		grid.add(buffer, 2, 0);
		
		createSearchOptions();
		locationSearchField = new CustomTextField("US state, city, or zipcode.", 42, 210);
		
		searchBtn = new Button("Search");
		searchBtn.getStyleClass().add("searchButton");
		
		//hbox lays out its children in a single row, for formatting
		HBox searchWrap = new HBox(3);				
		searchWrap.getChildren().add(searchDropdown);
		searchWrap.getChildren().add(restaurantSearchField);
		searchWrap.getChildren().add(locationSearchField);
		searchWrap.getChildren().add(searchBtn);
		
		return searchWrap;
	}
	
	public void createSearchOptions(){
		ObservableList<String> searchChoices = 
			    FXCollections.observableArrayList(
			        "Name",
			        "Type",
			        "Food",
			        "Keyword"			        
			    );
		searchDropdown = new DropdownMenu(null, 4, searchChoices, 0, 0, true);
		searchDropdown.addClass("mainSearchDropdown");
		
		restaurantSearchField = new CustomTextField("of Restaurant...", 42, 315);

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
	}
}
