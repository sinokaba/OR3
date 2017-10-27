<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppScene extends Scene{
	
	public AppScene(Parent p, double window_width, double window_height){
		super(p, window_width, window_height);
		super.getStylesheets().addAll(getClass().getResource(	
<<<<<<< HEAD
                "/css/home.css"
=======
                "/static/css/home.css"
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122
        ).toExternalForm());
	}
}
