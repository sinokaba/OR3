
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.shape.Rectangle;

public class searchResultsUI {
	ObservableList<Restaurant> listViewData = FXCollections.observableArrayList();
	ListView<Restaurant> listViewable;
	public void buildStage(AppWindow win, String keyword, List<String> searchResults, DBConnection db){
		win.resetLayout();
		listViewData.clear();
		Label title = new Label("Search results for '" + keyword + "': ");
		win.layout.add(title, 0, 2);
		title.getStyleClass().add("field");
		for(String res : searchResults){
			System.out.println("res: " + res);
			listViewData.add(db.getRestaurantFromDB(res, null));
		}
		System.out.println("first item in observable list: " + listViewData.get(0).getName());
		listViewable = new ListView<>(listViewData);
		win.layout.add(listViewable, 0, 3);
		win.updateElementCount(2);
		
	}
}
