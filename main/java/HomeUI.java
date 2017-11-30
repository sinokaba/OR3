import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HomeUI {
	AutoCompleteTextField locationSearchField, restaurantSearchField;
	DropdownMenu searchDropdown;
	Button searchBtn;
	private DBConnection db;
	private GoogleMapsService mapsApi;
	GridPane layout;
	
	public HomeUI(boolean loggedInUser, DBConnection db, GoogleMapsService api){
		layout = new GridPane();
		layout.getStyleClass().add("bg-image");
		this.db = db;
		mapsApi = api;
		Label mainTitle = new Label("OR3");
		mainTitle.getStyleClass().add("title");
		mainTitle.setAlignment(Pos.TOP_CENTER);
		layout.add(mainTitle, 1, 0);
		HBox searchWrap = createSearchBar(layout);
		layout.add(searchWrap, 1, 1);
		HBox hbBtn = new HBox(30);		
		hbBtn.setAlignment(Pos.CENTER);
		layout.add(hbBtn, 1, 6);
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
	}
	
	public HBox createSearchBar(GridPane grid){
		//pane is empty, acts as buffer for layout other elements
	    Pane buffer = new Pane();					
		grid.add(buffer, 2, 0);
		createSearchOptions();
		locationSearchField = new AutoCompleteTextField("US state, city, or zipcode.", 300, 44);
		locationSearchField.autocomplete(null, mapsApi);
		//restaurantSearchField = new AutoCompleteTextField("of Restaurant...", 420, 44);
		restaurantSearchField = new AutoCompleteTextField("Restaurant name or keyword.", 420, 44);
		restaurantSearchField.autocomplete(db, null);
		searchBtn = new Button("Search");
		searchBtn.getStyleClass().addAll("main-button", "search-button");
		searchBtn.defaultButtonProperty().bind(Bindings.or(
				locationSearchField.focusedProperty(), 
				restaurantSearchField.focusedProperty()));
		//hbox lays out its children in a single row, for formatting
		HBox searchWrap = new HBox(3);				
		//searchWrap.getChildren().add(searchDropdown);
		searchWrap.getChildren().addAll(restaurantSearchField, locationSearchField, searchBtn);
		
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
		searchDropdown.addClass("main-search-dropdown");
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
	
	public void clearFields(){
		locationSearchField.clear();
		restaurantSearchField.clear();
	}
}
