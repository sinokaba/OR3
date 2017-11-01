import java.awt.Window;
import java.util.List;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class searchResultsUI {
	
	public void buildStage(AppWindow win, String keyword, List<String> searchResults){
		win.resetLayout();
		Label title = new Label("Search results for '" + keyword + "': ");
		win.layout.add(title, 0, 2);
		title.getStyleClass().add("field");
		int index = 3;
		for(String res : searchResults){
			System.out.println("res: " + res);
			Hyperlink link = new Hyperlink(res);
			win.layout.add(link, 1, index);
			link.getStyleClass().add("mainText");
			Rectangle picPlaceholder = new Rectangle(64,64);
			win.layout.add(picPlaceholder, 0, index);
			index += 1;
		}
		win.updateElementCount(index+1);
		
	}
}
