
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CustomTextField extends TextField{
	
	public CustomTextField(String placeholder, double height, double width){
		super ();
		super.setPromptText(placeholder);
		System.out.println("place holder text: " + placeholder);
		super.setPrefSize(width, height);
	}
	
	public void setCharLimit(int charLimit){
		if(charLimit > 0){
			super.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= charLimit ? change : null));
		}
	}
	
}
