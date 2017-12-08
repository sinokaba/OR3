import java.util.regex.Pattern;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class LoginUI{
	private CustomTextField usernameField, emailField, tempCodeField;
	private PasswordField passwordField, newPwField, newPwVerifyField;
	private GridPane layout;
	private Alert forgotPwDlg, tempCodeDlg, newPwDlg;
	private Popup errDialog, confDialog;
	private ValidateForm validator;
	private FormField form;
	final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private String tempCode;
	private Window win;
	private DBConnection db;
	private User currentUser;
	private UIController ctrl;
	Button loginBtn, backBtn;
	
	public LoginUI(int width, int height, Window window, ValidateForm validator, DBConnection db, UIController ui){
		win = window;
		ctrl = ui;
		this.validator = validator;		
		this.db = db;
		form = new FormField(width, height);
		createDialogs();
	}
	
	public void buildPage(EmailGenerator email, PasswordEncryption pass){
		layout = new GridPane();
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		
		Label username = form.createLabel("Username:");
		usernameField = form.createTextField("Enter your unique username.", 16);
		createFieldLabelPair(layout, usernameField, username, 2);
		
		Label pw = form.createLabel("Password:");
		passwordField = form.createPasswordField("Your password.");
		createFieldLabelPair(layout, passwordField, pw, 3);

		Hyperlink forgotPw = new Hyperlink("Forgot password?");
		forgotPw.setStyle("-fx-font-size: 16");
		layout.add(forgotPw, 1, 4);
		forgotPw.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				ButtonType enterEmailBtn = forgotPwDlg.showAndWait().get();
				if(enterEmailBtn == ButtonType.OK){
					String userEmail = emailField.getText().trim(); 
					if(validator.checkEmail(userEmail, false)){
						//confDialog.createDialog("Verification sent!", "Check your email. A temporary password has been sent!");
						confDialog.createDialog("Verification sent!", "Check your email. Your temporary password has been sent!");
						tempCode = pass.tempPasswordGenerator();
						email.sendMail(userEmail, "This is neither secure nor practical. <br>"
								+ "Your temporary code is: " + tempCode + " <br> from or3 java application. <br>"
								+ "Ignore this email if you don't know whatever this is about.");
						confDialog.userConfirmation();
						if(tempCodeDlg.showAndWait().get() == ButtonType.OK && tempCodeField.getText().trim().equals(tempCode)){
				        	ButtonType newPwBtn = newPwDlg.showAndWait().get();	
				        	if(newPwBtn == ButtonType.OK){
				        		String pw = newPwField.getText();
				        		String hashedPw = pass.getSecurePassword(pw, db.getUserSalt(null, userEmail));
			        			db.updateUserPassword(userEmail, hashedPw);
			        			confDialog.showAlert("Updated Password!", "Your new password has been set!");
				        	}
							tempCodeField.clear();
				        	//con.loginUser(currentUser);
						}
						else{
							errDialog.showAlert("Failed to log in", "The code you entered did not match the code sent to your email, access denied.");
							tempCodeField.clear();
						}
						
					}
				}
				emailField.clear();
			}
		});
		
		loginBtn = new Button("Login");
		loginBtn.getStyleClass().add("main-button");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginBtn);
		layout.add(hbBtn, 1, 6);
		loginBtn.defaultButtonProperty().bind(Bindings.or(
				passwordField.focusedProperty(),
				usernameField.focusedProperty()
				));
		
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	String enteredUsername = getUsernameEntered();
	        	String enteredPassword = getPasswordEntered();
	        	//refactor this later
	        	if(enteredUsername.trim().length() <= 0 || enteredPassword.trim().length() <= 0){
	        		errDialog.showAlert("Error form.", "Please fill out all the fields.");
        			usernameField.getStyleClass().add("error");
        			passwordField.getStyleClass().add("error");
	        	}
	        	else{
	        		if(db.rowExists("users", "username", enteredUsername)){
		        		byte[] salt = db.getUserSalt(enteredUsername, null);
		        		System.out.println("controller 182 salt: " + salt);
		        		String hashedPassword = pass.getSecurePassword(enteredPassword, salt);
		        		//get user hashed password from db and compare
			        	currentUser = db.getUserFromDB(enteredUsername, hashedPassword);
			        	if(currentUser != null){
				        	clearFields();
			        		ctrl.loginUser(currentUser);
			        	}
			        	else{
			        		System.out.println(currentUser + " " + enteredUsername);
			        		errDialog.showAlert("Error form.", "User not found with that password.");	        		
			        	}
	        		}
	        		else{
	        			errDialog.showAlert("Error form.", "User not found with that password.");	
	        			if(!usernameField.getStyleClass().contains("error")){
		        			usernameField.getStyleClass().add("error");
		        			passwordField.getStyleClass().add("error");
	        			}
	        		}
	        	}
	        }
	    });
		backBtn = new Button("Back");
		backBtn.getStyleClass().add("main-button");
		HBox hbBackBtn = new HBox(10);
		hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBackBtn.getChildren().add(backBtn);
		layout.add(hbBackBtn, 0, 6);
		
		final Text actionTarget = new Text();
		actionTarget.setStyle("-fx-font-size: 13pt");
		layout.add(actionTarget, 1, 9, 7, 1);
    }
	
	public void createDialogs(){
		errDialog = new Popup("err", win);
		confDialog = new Popup("conf", win);
		
		createEmailDialog("Forgot password", "myemail@gmail.com");
		createTempCodeDialog("Enter the temporary code you received.", "Forgot password");
		createNewPwDialog("Create new password.");
	}
	
	public Alert createDialog(String headerText, HBox content){
		Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
		dialog.setTitle("Forgot Password.");
		dialog.initOwner(win);
		dialog.setHeaderText(headerText);
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setGraphic(null);
		dialogPane.setContent(content);

		return dialog;
	}
	
	public void createEmailDialog(String headerText, String fieldPlaceholder){
		emailField = new CustomTextField(fieldPlaceholder, 40, 250);
		HBox emailDlgContainer = new HBox(15);
		GridPane.setVgrow(emailField, Priority.ALWAYS);
		GridPane.setHgrow(emailField, Priority.ALWAYS);
		emailDlgContainer.getChildren().addAll(new Label("Email: "), emailField);
		
		forgotPwDlg = createDialog(headerText, emailDlgContainer);
		
		//button filter which checks if all the fields have been filled
		final Button btnOk = (Button)forgotPwDlg.getDialogPane().lookupButton(ButtonType.OK);
		btnOk.addEventFilter(ActionEvent.ACTION, event -> {
				String userEmail = emailField.getText().trim();
		        if(userEmail.length() <= 0 || !validator.checkEmail(userEmail, false)){
		            //The conditions are not fulfilled so we consume the event to prevent the dialog from closing
		            event.consume();
		            if(userEmail.length() <= 0){
		            	errDialog.showAlert("Incomplete form!", "Error! You must fill out all the fields!");	
		            }
		            else{
		            	errDialog.showAlert("Invalid Email entered!", "Error! The email you entered is not valid or is not associated with an account!");	
		            }
		        }
		    }
		);
	}
	
	public void createTempCodeDialog(String headerText, String placeholder){
		tempCodeField = new CustomTextField(placeholder, 40, 250);
		HBox tempCodeContainer = new HBox(15);
		GridPane.setVgrow(tempCodeField, Priority.ALWAYS);
		GridPane.setHgrow(tempCodeField, Priority.ALWAYS);
		tempCodeContainer.getChildren().addAll(new Label("Code: "), tempCodeField);
		
		tempCodeDlg = createDialog(headerText, tempCodeContainer);
	}
	
	public void createNewPwDialog(String headerText){
		newPwField = form.createPasswordField("New Password");
		newPwVerifyField = form.createPasswordField("Reenter password");

		HBox newPwContainer = new HBox(15);
		GridPane.setVgrow(tempCodeField, Priority.ALWAYS);
		GridPane.setHgrow(tempCodeField, Priority.ALWAYS);
		newPwContainer.getChildren().addAll(new Label("New Password: "), newPwField, newPwVerifyField);

		newPwDlg = createDialog(headerText, newPwContainer);
		
		final Button btnOk = (Button)newPwDlg.getDialogPane().lookupButton(ButtonType.OK);
		btnOk.addEventFilter(ActionEvent.ACTION, event -> {
	    		if(!validator.checkPassword(newPwField.getText(), newPwVerifyField.getText())){
	    			event.consume();
	    		}
		    }
		);
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
		if(usernameField != null && passwordField != null){
			usernameField.clear();
			passwordField.clear();			
		}
	}
	
	public User getUser(){
		return currentUser;
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
}
