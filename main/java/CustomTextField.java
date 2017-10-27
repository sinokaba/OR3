<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

public class CustomTextField extends TextField{
	
	public CustomTextField(String placeholder, double height, double width){
		super ();
		super.setPromptText(placeholder);
		System.out.println("place holder text: " + placeholder);
		super.setPrefSize(width, height);
	}
	
	public void addCharLimit(int charLimit){
		if(charLimit > 0){
			super.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= charLimit ? change : null));
		}
	}
	
}
