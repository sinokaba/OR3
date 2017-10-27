<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

public class FormField {
	private final int fieldWidth, fieldHeight;
	
	public FormField(int width, int height){
		fieldWidth = width;
		fieldHeight = height;
	}
	
	public Label createLabel(String labelName){
		Label label = new Label(labelName);
		label.getStyleClass().add("field");
		return label;
	}
	
	public CustomTextField createTextField(String placeholder, int charLimit){
		CustomTextField textField = new CustomTextField(placeholder, fieldHeight, fieldWidth);
		textField.addCharLimit(charLimit);
		return textField;
	}
	
	public PasswordField createPasswordField(String placeholder){
		PasswordField pwField = new PasswordField();
		pwField.setPromptText(placeholder);
		pwField.getStyleClass().add("formField");
		return pwField;
	}
}
