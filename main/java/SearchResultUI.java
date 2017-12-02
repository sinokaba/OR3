import java.util.List;

import org.controlsfx.control.Rating;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
	private UIController controller;
	private GoogleMap map;
	BorderPane layout;
	
	public SearchResultUI(DBConnection db, UIController ui){
		layout = new BorderPane();
		this.db = db;
		controller = ui;
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
		
		VBox container = new VBox(30);
		Insets paddingBottom = new Insets(10, 10, 20, 10);
		container.setPrefWidth(630);
		container.setPadding(paddingBottom);
		//container.getStyleClass().add("container");
		Restaurant firstRst = null;
		for(String res : searchResults){
			Restaurant r = db.getRestaurantFromDB(res, null);
			if(firstRst == null){
				firstRst = r;
			}
			HBox detailsWrapperH = new HBox(15);
			VBox detailsWrapperV = new VBox(2);
			Label rstName = new Label(res);
			rstName.getStyleClass().addAll("h4", "rst-link");
			rstName.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			        controller.restaurantView(r);
			    }
			});
			Label lbl2 = new Label("Address: " + r.getAddress());
			lbl2.getStyleClass().add("p");
			Label lbl3 = new Label("Phone: " + r.getPhone());
			lbl3.getStyleClass().add("p");
			
			Rating ratingStars = new Rating();
			ratingStars.setPartialRating(true);
			ratingStars.setMax(5);
			ratingStars.setRating(Math.round(r.getRating()*100.0)/100.0);
			ratingStars.getStyleClass().addAll("stars-small", "overall-rating");
			ratingStars.setDisable(true);
			
			detailsWrapperV.getChildren().addAll(rstName, ratingStars, lbl2, lbl3);
			Rectangle rec = new Rectangle(70, 70);
			rec.getStyleClass().add("rst-link");
			rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			        controller.restaurantView(r);
			    }
			});
			detailsWrapperH.getChildren().addAll(rec, detailsWrapperV);
			detailsWrapperH.setStyle("-fx-border-color: transparent transparent white transparent; "
							        + "-fx-border-width: 0 0 2 0; "
							        + "-fx-border-style: dashed solid solid solid;");
			container.getChildren().add(detailsWrapperH);
		}
		VBox mapContainer = new VBox(15);
		//System.out.println("first rst: " + firstRst.getName());
		if(firstRst != null){
			map.markLocation(firstRst.getAddress());
		    mapContainer.getChildren().add(map.getMap());
			layout.setRight(mapContainer);
		}
	    scroll.setContent(container);
		scroll.getStyleClass().add("scroll-fill");
		layout.setCenter(scroll);
	}
}
