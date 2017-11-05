
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RegistrationUI{
	CustomTextField usernameField, emailField, zipcodeField;
	DropdownMenu month, day, year;
	PasswordField pwField, pwFieldVerify;
	Button registerBtn, backBtn;
	String birthday; 
	
	public void buildStage(AppWindow win, int w, int h){
		win.resetLayout();
		
		FormField form = new FormField(w, h);
		
		Label username = form.createLabel("Username:");
		usernameField = form.createTextField("Enter your unique username.", 16);
		createFieldLabelPair(win.layout, usernameField, username, 2);
		
		Label email = form.createLabel("Email:");
		emailField = form.createTextField("Enter your email.", 0);
		createFieldLabelPair(win.layout, emailField, email, 3);
		
		Label pw = form.createLabel("Password:");
		pwField = form.createPasswordField("Your password.");
		createFieldLabelPair(win.layout, pwField, pw, 4);

		Label pwV = form.createLabel("Password:");
		pwFieldVerify = form.createPasswordField("Verify and re-enter your password.");
		createFieldLabelPair(win.layout, pwFieldVerify, pwV, 5);
		

		Label zipcode = form.createLabel("Zipcode:");
		zipcodeField = form.createTextField("Enter your 5 digit zipcode.", 5);
		createFieldLabelPair(win.layout, zipcodeField, zipcode, 6);
		
		HBox birthdayWrapper = new HBox(10);
		Label bday = form.createLabel("Birthday:");
		win.layout.add(bday, 0, 7);
		month = new DropdownMenu("Month", 9, null, 1, 12, true);
		day = new DropdownMenu("Day", 9, null, 1, 31, true);
		year = new DropdownMenu("Year", 9, null, 1920, 2017, false);
		birthdayWrapper.getChildren().add(month);
		birthdayWrapper.getChildren().add(day);
		birthdayWrapper.getChildren().add(year);
		win.layout.add(birthdayWrapper, 1, 7);
		
		registerBtn = new Button("Sign up!");
		registerBtn.setDefaultButton(true);
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerBtn);
		win.layout.add(hbBtn, 1, 8);
	
		backBtn = new Button("Back");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		win.layout.add(hbBackBtn, 0, 8);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
		win.layout.add(actionTarget, 1, 10, 4, 1);
        
		win.updateElementCount(15);
	}
	
	public String getUsername(){
		return usernameField.getText();
	}
	
	public String getEmail(){
		return emailField.getText();
	}
	
	public String getPassword(){
		return pwField.getText();
	}
	
	public String getPasswordV(){
		return pwFieldVerify.getText();
	}

	public String getZipcode(){
		return zipcodeField.getText();
	}
	
	public String getBirthday(){
		return year.getValue() + "-" + month.getValue() + "-" + day.getValue();
	}
	
	public void createFieldLabelPair(GridPane grid, CustomTextField field, Label lbl, int row){		
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);
	}
	
	public void createFieldLabelPair(GridPane grid, PasswordField field, Label lbl, int row){
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);			
	}
}
