import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoginUI{

	private CustomTextField usernameField;
	private PasswordField passwordField;
	Button loginBtn, backBtn;
	private GridPane layout;

	public LoginUI(int width, int height){
		//super();
		layout = new GridPane();
		FormField form = new FormField(width, height);
		Label username = form.createLabel("Username:");
		usernameField = form.createTextField("Enter your unique username.", 16);
		createFieldLabelPair(layout, usernameField, username, 2);
		
		Label pw = form.createLabel("Password:");
		passwordField = form.createPasswordField("Your password.");
		createFieldLabelPair(layout, passwordField, pw, 3);

		loginBtn = new Button("Login");
		loginBtn.getStyleClass().add("main-button");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginBtn);
		layout.add(hbBtn, 1, 7);
		loginBtn.defaultButtonProperty().bind(Bindings.or(
				passwordField.focusedProperty(),
				usernameField.focusedProperty()
				));
	
		backBtn = new Button("Back");
		backBtn.getStyleClass().add("main-button");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		layout.add(hbBackBtn, 0, 7);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
		layout.add(actionTarget, 1, 9, 7, 1);
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		//root.setCenter(layout);
    }
	
	public void createFieldLabelPair(GridPane grid, CustomTextField field, Label lbl, int row){		
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);
	}
	
	public void createFieldLabelPair(GridPane grid, PasswordField field, Label lbl, int row){
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);			
	}
	
	public GridPane getLayout(){
		return layout;
	}
	public String getUsernameEntered(){
		return usernameField.getText();
	}
	
	public String getPasswordEntered(){
		return passwordField.getText();
	}
	
	public void clearFields(){
		usernameField.clear();
		passwordField.clear();
	}
}

