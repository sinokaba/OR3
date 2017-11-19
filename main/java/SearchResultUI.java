import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class SearchResultUI {
	private DBConnection db;
	private UIController controller;
	BorderPane layout;
	
	public SearchResultUI(DBConnection db, UIController ui){
		layout = new BorderPane();
		this.db = db;
		controller = ui;
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
		container.getStyleClass().add("container");
		for(String res : searchResults){
			Restaurant r = db.getRestaurantFromDB(res, null);
			HBox detailsWrapperH = new HBox(15);
			VBox detailsWrapperV = new VBox(2);
			Label rstName = new Label(res);
			rstName.getStyleClass().add("h4");
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
			Label lbl4 = new Label(String.valueOf(r.getRating()) + " stars.");
			lbl4.getStyleClass().add("p");
			detailsWrapperV.getChildren().add(rstName);
			detailsWrapperV.getChildren().add(lbl2);
			detailsWrapperV.getChildren().add(lbl3);
			Rectangle rec = new Rectangle(70, 70);
			detailsWrapperH.getChildren().add(rec);
			detailsWrapperH.getChildren().add(detailsWrapperV);
			detailsWrapperH.getChildren().add(lbl4);
			detailsWrapperH.setStyle("-fx-border-color: transparent transparent white transparent; "
							        + "-fx-border-width: 0 0 4 0; "
							        + "-fx-border-style: dashed solid solid solid;");
			container.getChildren().add(detailsWrapperH);
		}
		VBox mapContainer = new VBox(15);
		Image img = new Image(getClass().getResourceAsStream("/images/testMap.png"));
	    ImageView imgView = new ImageView(img);
	    imgView.setFitWidth(360);
	    imgView.setFitHeight(200);
	    mapContainer.getChildren().add(imgView);
		layout.setRight(mapContainer);
	    scroll.setContent(container);
		scroll.getStyleClass().add("search");
		scroll.setPrefSize(400, 300);
		layout.setCenter(scroll);
		//System.out.print(imgView);
		layout.setMargin(searchTitle, new Insets(10));
	}
}
