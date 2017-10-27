<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class Popup {
	private static Alert alert; 
	
	public Popup(String type, Window win){
		if(type.equals("err")){
			alert = new Alert(Alert.AlertType.ERROR);
		}
		else if(type.equals("warning")){
			alert = new Alert(Alert.AlertType.WARNING);
		}
		else if(type.equals("conf")){
			alert = new Alert(Alert.AlertType.CONFIRMATION);
		}
		else if(type.equals("info")){
			alert = new Alert(Alert.AlertType.INFORMATION);
		}
	    alert.initOwner(win);
	}
	
	public void showAlert(String title, String message) {
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.show();
	}
}
