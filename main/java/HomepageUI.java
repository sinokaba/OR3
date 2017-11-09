
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HomepageUI{
	//CustomTextField restaurantSearchField;
	AutoCompleteTextField locationSearchField, restaurantSearchField;
	DropdownMenu searchDropdown;
	Button searchBtn, loginBtn, signUpBtn;
	DBConnection db;
	GoogleMapsService mapsApi;
	
	public void buildStage(AppWindow win, boolean loggedInUser, DBConnection db, GoogleMapsService api){;	
		this.db = db;
		mapsApi = api;
		win.resetLayout();
		Label mainTitle = new Label("OR3");
		mainTitle.getStyleClass().add("title");
		mainTitle.setAlignment(Pos.TOP_CENTER);
		win.layout.add(mainTitle, 1, 0);
		HBox searchWrap = createSearchBar(win.layout);
		win.layout.add(searchWrap, 1, 1);
		HBox hbBtn = new HBox(30);		
		hbBtn.setAlignment(Pos.CENTER);
		if(!loggedInUser){
			loginBtn = new Button("Login");
			signUpBtn = new Button("Sign Up");
			hbBtn.getChildren().add(loginBtn);
			hbBtn.getChildren().add(signUpBtn);
		}
		win.layout.add(hbBtn, 1, 6);
	}
	
	public HBox createSearchBar(GridPane grid){
		//pane is empty, acts as buffer for layout other elements
	    Pane buffer = new Pane();					
		grid.add(buffer, 2, 0);
		//String[] testWords = {"hmm", "hell", "heyo", "da", "dark", "app"};
		createSearchOptions();
		//locationSearchField = new CustomTextField("US state, city, or zipcode.", 26, 270);
		locationSearchField = new AutoCompleteTextField("US state, city, or zipcode.", 270, 44);
		locationSearchField.autocomplete(null, mapsApi);
		//locationSearchField.getEntries().addAll(Arrays.asList("AA", "AB", "AC","BCA"));
		restaurantSearchField = new AutoCompleteTextField("of Restaurant...", 350, 44);
		restaurantSearchField.autocomplete(db, null);
		searchBtn = new Button("Search");
		searchBtn.getStyleClass().add("searchButton");
		searchBtn.setDefaultButton(true);
		
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
