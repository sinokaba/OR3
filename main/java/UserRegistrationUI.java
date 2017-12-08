
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class UserRegistrationUI {

	private CustomTextField usernameField, emailField, zipcodeField;
	private DropdownMenu month, day, year;
	private PasswordField pwField, pwFieldVerify;
	private int fieldW, fieldH;
	GridPane layout;
	Button registerBtn, backBtn;
	
	public UserRegistrationUI(int w, int h){
		fieldW = w;
		fieldH = h;
	}
	
	public void buildPage(){
		layout = new GridPane();
		
		FormField form = new FormField(fieldW, fieldH);
		Label titlePage = new Label("Create your account!");
		titlePage.getStyleClass().add("h3");
		layout.add(titlePage, 1, 0);
		
		Label username = form.createLabel("Username:");
		usernameField = form.createTextField("Enter your unique username.", 16);
		createFieldLabelPair(layout, usernameField, username, 1);
		
		Label email = form.createLabel("Email:");
		emailField = form.createTextField("Enter your email.", 0);
		createFieldLabelPair(layout, emailField, email, 2);
		
		Label pw = form.createLabel("Password:");
		pwField = form.createPasswordField("Your password.");
		createFieldLabelPair(layout, pwField, pw, 3);

		Label pwV = form.createLabel("Password:");
		pwFieldVerify = form.createPasswordField("Verify and re-enter your password.");
		createFieldLabelPair(layout, pwFieldVerify, pwV, 4);
		

		Label zipcode = form.createLabel("Zipcode:");
		zipcodeField = form.createTextField("Enter your 5 digit zipcode.", 5);
		createFieldLabelPair(layout, zipcodeField, zipcode, 5);
		
		HBox birthdayContainer = new HBox(10);
		Label bday = form.createLabel("Birthday:");
		layout.add(bday, 0, 6);
		month = new DropdownMenu("Month", 9, null, 1, 12, true);
		day = new DropdownMenu("Day", 9, null, 1, 31, true);
		year = new DropdownMenu("Year", 9, null, 1920, 2017, false);
		birthdayContainer.getChildren().add(month);
		birthdayContainer.getChildren().add(day);
		birthdayContainer.getChildren().add(year);
		layout.add(birthdayContainer, 1, 6);
		
		registerBtn = new Button("Sign up!");
		registerBtn.getStyleClass().add("main-button");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerBtn);
		layout.add(hbBtn, 1,7);
		registerBtn.setDefaultButton(true);

		backBtn = new Button("Back");
		backBtn.getStyleClass().add("main-button");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		layout.add(hbBackBtn, 0, 7);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
		layout.add(actionTarget, 1, 9, 4, 1);
		
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
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
	
	public void clearFields(){
		try{
			usernameField.clear();
			zipcodeField.clear();
			pwField.clear();
			pwFieldVerify.clear();
			emailField.clear();
			month.clear();
			day.clear();
			year.clear();
		}
		catch(NullPointerException err){
			//err.printStackTrace();
		}
	}

}
