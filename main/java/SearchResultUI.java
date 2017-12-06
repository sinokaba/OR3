import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.Rating;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.*;

public class SearchResultUI {
	private DBConnection db;
	private UIController ctrl;
	private GoogleMap map;
	BorderPane layout;
	
	public SearchResultUI(DBConnection db, UIController ui){
		layout = new BorderPane();
		this.db = db;
		ctrl = ui;
		map = new GoogleMap();
	}
	public void build(String loc, String kw, List<String> searchResults){
		Label searchTitle = new Label("Search results for '" + kw + "' at '" + loc + "': ");
		if(loc == null){
			searchTitle = new Label("Search results for '" + kw + "': ");
		}
		searchTitle.getStyleClass().add("h2");
		layout.setTop(searchTitle);
		ScrollPane scroll = new ScrollPane();
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		ListView<HBox> searchRes = new ListView<HBox>();
		searchRes.setPrefSize(630, 500);
		searchRes.getStyleClass().add("search-results");
		Restaurant firstRst = null;
		for(String rst : searchResults){
			Restaurant r = db.getRestaurantFromDB(rst, null);
			if(firstRst == null){
				firstRst = r;
			}
			
			HBox tagsWrapper = new HBox(8);
			ArrayList<String> tags = r.getTags();
			for(int i = 0; i < tags.size(); i++){
				Label tag = new Label(tags.get(i) + " | ");
				tag.getStyleClass().add("h5");
				tagsWrapper.getChildren().add(tag);
			}
			
			HBox detailsWrapperH = new HBox(15);
			VBox detailsWrapperV = new VBox(2);
			Label rstName = new Label(rst);
			rstName.getStyleClass().addAll("h4", "rst-link");
			rstName.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			        ctrl.restaurantView(r);
			    }
			});
			Label address = new Label("Address: " + r.getAddress());
			address.getStyleClass().add("p");
			Label phone = new Label("Phone: " + r.getPhone());
			phone.getStyleClass().add("p");
			
			Rating ratingStars = new Rating();
			ratingStars.setPartialRating(true);
			ratingStars.setMax(5);
			ratingStars.setRating(Math.round(r.getRating()*100.0)/100.0);
			ratingStars.getStyleClass().addAll("stars-small", "overall-rating");
			ratingStars.setDisable(true);
			
			detailsWrapperV.getChildren().addAll(rstName, ratingStars, address, phone, tagsWrapper);
			Rectangle rec = new Rectangle(85, 85);
			rec.getStyleClass().add("rst-link");
			rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			        ctrl.restaurantView(r);
			    }
			});
			detailsWrapperH.getChildren().addAll(rec, detailsWrapperV);
			searchRes.getItems().add(detailsWrapperH);
		}
		VBox mapContainer = new VBox(15);
		//System.out.println("first rst: " + firstRst.getName());
		if(firstRst != null){
			map.removeMarkers();
			map.markLocation(firstRst.getAddress(), firstRst.getName());
		    mapContainer.getChildren().add(map.getMap());
			layout.setRight(mapContainer);
		}
		
		searchRes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>(){
		    @Override
		    public void changed(ObservableValue<? extends HBox> observable, HBox oldValue, HBox newValue){
		    	String address = ((Label)((VBox)newValue.getChildren().get(1)).getChildren().get(2)).getText();
		    	String name = ((Label)((VBox)newValue.getChildren().get(1)).getChildren().get(0)).getText();
		    	System.out.println("Selected item: " + address);
		        map.markLocation(address, name);
		    }
		});
		
	    scroll.setContent(searchRes);
		scroll.getStyleClass().add("scroll-fill");
		layout.setCenter(scroll);
	}
}
