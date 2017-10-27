
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoginUI {
	private CustomTextField usernameField;
	private PasswordField passwordField;
	Button loginBtn, backBtn;
	
	public void buildStage(AppWindow win, int w, int h){
		win.resetLayout();
		
		FormField form = new FormField(w, h);
				
		Label username = form.createLabel("Username:");
		usernameField = form.createTextField("Enter your unique username.", 16);
		createFieldLabelPair(win.layout, usernameField, username, 2);
		
		Label pw = form.createLabel("Password:");
		passwordField = form.createPasswordField("Your password.");
		createFieldLabelPair(win.layout, passwordField, pw, 3);

		loginBtn = new Button("Login");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginBtn);
		win.layout.add(hbBtn, 1, 7);
	
		backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		win.layout.add(hbBackBtn, 0, 7);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
		win.layout.add(actionTarget, 1, 9, 7, 1);
        
		win.updateElementCount(7);
	}
	
	public void createFieldLabelPair(GridPane grid, CustomTextField field, Label lbl, int row){		
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);
	}
	
	public void createFieldLabelPair(GridPane grid, PasswordField field, Label lbl, int row){
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);			
	}
	
	public String getUsernameEntered(){
		return usernameField.getText();
	}
	
	public String getPasswordEntered(){
		return passwordField.getText();
	}
}
