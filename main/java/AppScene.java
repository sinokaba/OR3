

import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppScene extends Scene{
	final static int WINDOW_HEIGHT = 620;
	final static int WINDOW_WIDTH = 992;
	
	public AppScene(Parent p){
		super(p, WINDOW_WIDTH, WINDOW_HEIGHT);
		super.getStylesheets().add(getClass().getResource(	
                "/css/main.css"
        ).toExternalForm());
	}
	
	public void addCss(String fileName){
		super.getStylesheets().add(getClass().getResource(	
                "/css/" + fileName + ".css"
        ).toExternalForm());
	}
}
