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
	private CustomTextField usernameField;
	private PasswordField passwordField;
	private GridPane layout;
	private Alert forgotPwDialog;
	private CustomTextField emailField;
	private Popup errDialog;
	private ValidateForm validator;
	private int fieldHeight;
	private int fieldWidth;
	final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private String tempPassword;
	Button loginBtn, backBtn;
	
	public LoginUI(int width, int height, Window window, ValidateForm validator, DBConnection db){
		this.validator = validator;
		fieldHeight = height;
		fieldWidth = width;
		buildPage(db, window);
	}
	
	public void buildPage(DBConnection db, Window win){
		EmailGenerator emailGen = new EmailGenerator();
		PasswordEncryption pwEncrypt = new PasswordEncryption();
		createDialog("Forgot password", "myemail@gmail.com", win);
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
						confDialog.createDialog("Verification sent!", "Check your email. Your password has been sent!");
						//tempPassword = pwEncrypt.tempPasswordGenerator();
						
						emailGen.sendMail(userEmail, "This is neither secure nor practical. <br>"
								+ "Note to self: generate temp password and email that instead. <br> <br>"
								+ "Anyways your password is: " + db.getUserPassword(userEmail) + " <br> from or3 java application.");
						confDialog.userConfirmation();
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
}

