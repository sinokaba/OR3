import java.util.regex.Pattern;

import javax.mail.MessagingException;

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
	private CustomTextField usernameField, emailField, tempPwField;
	private PasswordField passwordField;
	private GridPane layout;
	private Alert forgotPwDialog, tempPwDialog;
	private Popup errDialog;
	private ValidateForm validator;
	private int fieldHeight;
	private int fieldWidth;
	final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private String tempPassword;
	private Window win;
	private DBConnection db;
	private User currentUser;
	private UIController con;
	Button loginBtn, backBtn;
	
	public LoginUI(int width, int height, Window window, ValidateForm validator, DBConnection db, UIController con){
		this.validator = validator;
		fieldHeight = height;
		fieldWidth = width;
		win = window;
		this.db = db;
		this.con = con;
		//buildPage(db, window);
	}
	
	public void buildPage(EmailGenerator email, PasswordEncryption pass){
		createDialog("Forgot password", "myemail@gmail.com", win);
		createDialog("Enter the temporary code you received.", "Forgot password", "", win);
		errDialog = new Popup("err", win);
		Popup confDialog = new Popup("conf", win);
		//super();
		layout = new GridPane();
		FormField form = new FormField(fieldWidth, fieldHeight);
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
				ButtonType res = forgotPwDialog.showAndWait().get();
				if(res == ButtonType.OK){
					String userEmail = emailField.getText().trim(); 
					if(validator.checkEmail(userEmail, false)){
						//confDialog.createDialog("Verification sent!", "Check your email. A temporary password has been sent!");
						confDialog.createDialog("Verification sent!", "Check your email. Your temporary password has been sent!");
						tempPassword = pass.tempPasswordGenerator();
						email.sendMail(userEmail, "This is neither secure nor practical. <br>"
								+ "Note to self: generate temp password and email that instead. <br> <br>"
								+ "Anyways your remporary password is: " + tempPassword + " <br> from or3 java application.");
						confDialog.userConfirmation();
						if(tempPwDialog.showAndWait().get() == ButtonType.OK && tempPwField.getText().equals(tempPassword)){
				        	currentUser = db.getUserFromDB(userEmail);
				        	con.loginUser(currentUser);
						};
						
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
	        	}
	        	else{
	        		if(db.rowExists("users", "username", enteredUsername)){
		        		byte[] salt = db.getUserSalt(enteredUsername);
		        		System.out.println("controller 182 salt: " + salt);
		        		String hashedPassword = pass.getSecurePassword(enteredPassword, salt);
		        		//get user hashed password from db and compare
			        	currentUser = db.getUserFromDB(enteredUsername, hashedPassword);
			        	if(currentUser != null){
				        	clearFields();
			        		con.loginUser(currentUser);
			        	}
			        	else{
			        		System.out.println(currentUser + " " + enteredUsername);
			        		errDialog.showAlert("Error form.", "User not found with that password.");	        		
			        	}
	        		}
	        		else{
	        			errDialog.showAlert("Error form.", "User not found with that password.");	     
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
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
    }
	
	public void createFieldLabelPair(GridPane grid, CustomTextField field, Label lbl, int row){		
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);
	}
	
	public void createFieldLabelPair(GridPane grid, PasswordField field, Label lbl, int row){
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);			
	}
	
	public void createDialog(String title, String placeholder, Window win){
		forgotPwDialog = new Alert(Alert.AlertType.CONFIRMATION);
		forgotPwDialog.setTitle("Forgot password.");
		forgotPwDialog.initOwner(win);
		forgotPwDialog.setHeaderText("Enter the email associated with your account.");
		DialogPane emailDialog = forgotPwDialog.getDialogPane();
		emailDialog.setGraphic(null);
		
		emailField = new CustomTextField(placeholder, 40, 250);
		
		HBox contentContainer = new HBox(15);
		GridPane.setVgrow(emailField, Priority.ALWAYS);
		GridPane.setHgrow(emailField, Priority.ALWAYS);
		contentContainer.getChildren().addAll(new Label("Email: "), emailField);
		emailDialog.setContent(contentContainer);
		
		//button filter which checks if all the fields have been filled
		final Button btnOk = (Button)emailDialog.lookupButton(ButtonType.OK);
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
	
	public void createDialog(String headerTxt, String title, String placeholder, Window win){
		tempPwDialog = new Alert(Alert.AlertType.CONFIRMATION);
		tempPwDialog.setTitle("Forgot password.");
		tempPwDialog.initOwner(win);
		tempPwDialog.setHeaderText(headerTxt);
		DialogPane tempPw = tempPwDialog.getDialogPane();
		tempPw.setGraphic(null);
		
		tempPwField = new CustomTextField(placeholder, 40, 250);
		
		HBox contentContainer = new HBox(15);
		GridPane.setVgrow(tempPwField, Priority.ALWAYS);
		GridPane.setHgrow(tempPwField, Priority.ALWAYS);
		contentContainer.getChildren().addAll(new Label("Code: "), tempPwField);
		tempPw.setContent(contentContainer);
	
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
		if(usernameField != null && passwordField != null){
			usernameField.clear();
			passwordField.clear();			
		}
	}
	
	public User getUser(){
		return currentUser;
	}

}
