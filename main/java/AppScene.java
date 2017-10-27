

import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppScene extends Scene{
	
	public AppScene(Parent p, double window_width, double window_height){
		super(p, window_width, window_height);
		super.getStylesheets().addAll(getClass().getResource(	
                "/css/home.css"
        ).toExternalForm());
	}
}
