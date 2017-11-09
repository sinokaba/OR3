
import java.awt.Color;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class searchResultsUI {
	ObservableList<Restaurant> listViewData = FXCollections.observableArrayList();
	ListView<Restaurant> listViewable;
	public void buildStage(AppWindow win, String keyword, String loc, List<String> searchResults, DBConnection db, UIMediator ui){
		win.resetLayout();
		listViewData.clear();
		Label searchTitle = new Label("Search results for '" + keyword + "' at '" + loc + "': ");
		if(loc == null){
			searchTitle = new Label("Search results for '" + keyword + "': ");
		}
		win.layout.add(searchTitle, 0, 0);
		searchTitle.getStyleClass().add("field");
		int pos = 1;
		for(String res : searchResults){
			/*
			System.out.println("res: " + res);
			listViewData.add(db.getRestaurantFromDB(res, null));
			*/
			Restaurant r = db.getRestaurantFromDB(res, null);
			HBox detailsWrapperH = new HBox(10);
			VBox detailsWrapperV = new VBox(10);
			Label lbl = new Label(res);
			lbl.getStyleClass().add("field");
			lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			        ui.restaurantPage(r);
			    }
			});
			Label lbl2 = new Label("Address: " + r.getAddress());
			lbl2.getStyleClass().add("field");
			Label lbl3 = new Label("Phone: " + r.getPhone());
			lbl3.getStyleClass().add("field");
			Label lbl4 = new Label(String.valueOf(r.getRating()) + " stars.");
			lbl4.getStyleClass().add("field");
			detailsWrapperV.getChildren().add(lbl);
			detailsWrapperV.getChildren().add(lbl2);
			detailsWrapperV.getChildren().add(lbl3);
			Rectangle rec = new Rectangle(50, 50);
			detailsWrapperH.getChildren().add(rec);
			detailsWrapperH.getChildren().add(detailsWrapperV);
			detailsWrapperH.getChildren().add(lbl4);
			win.layout.add(detailsWrapperH, 0, pos);
			pos += 1;
		}
		//System.out.println("first item in observable list: " + listViewData.get(0).getName());
		//listViewable = new ListView<>(listViewData);
		//win.layout.add(listViewable, 0, 3);		
	}
}
