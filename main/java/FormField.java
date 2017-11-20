
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class FormField {
	private final int fieldWidth, fieldHeight;
	
	public FormField(int width, int height){
		fieldWidth = width;
		fieldHeight = height;
	}
	
	public Label createLabel(String labelName){
		Label label = new Label(labelName);
		label.getStyleClass().add("h3");
		return label;
	}
	
	public CustomTextField createTextField(String placeholder, int charLimit){
		CustomTextField textField = new CustomTextField(placeholder, fieldHeight, fieldWidth);
		textField.setCharLimit(charLimit);
		return textField;
	}
	
	public PasswordField createPasswordField(String placeholder){
		PasswordField pwField = new PasswordField();
		pwField.setPromptText(placeholder);
		pwField.getStyleClass().add("form-field");
		return pwField;
	}
}
