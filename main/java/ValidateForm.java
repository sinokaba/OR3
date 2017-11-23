
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ValidateForm {
	final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	final Pattern VALID_PASSWORD_REGEX = 
			Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,}$");
	final Pattern VALID_ZIPCODE_REGEX =
			Pattern.compile("^(?=.*[0-9]).{5,5}$");	
	final Pattern VALID_PHONE_REGEX = 
			Pattern.compile("^(?=.*[0-9]).{10,10}$");	
	
	private DBConnection db;
	Popup err;
	
	public ValidateForm(DBConnection db, Popup popup){
		this.db = db;
		err = popup;
	}
	
	public boolean validUserRegistration(String pw, String pwRe, String name, String email, String zip, Pane p){
    	//Tooltip tp = new Tooltip("Invalid password. Must be at least 6 chars long and have 1 number and uppercase letter.");;
    	if(!noEmptyFields(p, true)){
    		return false;
    	}
    	else if(!checkUsername(name)){
    	    return false;       		
    	}
    	else if(!checkPassword(pw, pwRe)){
    		return false;
    	}
    	else if(!checkEmail(email, true)){
    		return false;
    	}
    	else if(!validZipcode(zip)){
    		err.showAlert("Form Error!", "Invalid Zipcode. Zipcode entered must be within the USA.");;    
	    	return false;
		}
    	return true;    
	}
	
	public boolean validRestaurantReg(String name, String address, String zip, String phone, Pane p){
	   	if(!noEmptyFields(p, false)){
    		return false;
    	}
    	else if(!validZipcode(zip)){
    		err.showAlert("Form Error!", "Invalid Zipcode. Zipcode entered must be within the USA.");;    
	    	return false;
		}
    	else if(!validPhone(phone)){
    		err.showAlert("Form Error!", "Invalid phone number entered.");    
	    	return false;   		
    	}
	   	if(db.rowExists("restaurants", "name", name, "address", address)){
	   		err.showAlert("Restaurant already added", "This restaurant at this address already added!");
	   		return false;
	   	}
	   	
	   	return true;
	}
	
	public boolean checkPassword(String pw, String pwRe){
		if(!validPassword(pw)){
			err.showAlert("Form Error!", "Invalid password. Password must have 6 or more characters, and have at least 1 capital letter and 1 digit.");;    
    	    return false;
    	}         
    	else if(!pw.equals(pwRe)){
    		err.showAlert("Form Error!", "Passwords don't match.");;    
    	    return false;    		
    	}		
		return true;
	}
	
	public boolean checkEmail(String email, boolean newUser){
    	if(!validEmail(email)){
    		err.showAlert("Form Error!", "Email address: " + email + ", is invalid.");;
    	    return false;
    	}
	    if(db.rowExists("users", "email", email)){
	    	if(newUser){
		    	err.showAlert("Form Error!", "Email address: " + email + ", already associated with another account.");;
		    	return false; 
	    	}
	    	return true;
    	}
	    if(newUser){
	    	return true;
	    }
	    return false;
	}
	
	public boolean checkUsername(String username){
		if(db.rowExists("users", "username", username)){
    		err.showAlert("Form Error!", "Username: " + username + ", already taken. Please choose a different one.");;    
    	    return false;       		
    	}		
		else if(username.contains(" ")){
    		err.showAlert("Form Error!", "Username: " + username + ", must not have any whitespaces.");;    
			return false;
		}
		return true;
	}
	
	public boolean noEmptyFields(Pane grid, boolean noWhitespace){
		for (Node child : grid.getChildren()) {
    	    if(child instanceof TextField){
    	    	String userInput = ((TextField)child).getText();
    	    	if(userInput.trim().length() <= 0){
    	    		err.showAlert("Form Error!", "All form fields must be completed.");;     	    		
                	return false;
    	    	}
    	    	if(noWhitespace){
	    	    	if(userInput.contains(" ")){
	    	    		err.showAlert("Form Error!", "No white spaces please.");;    
	    	    	    return false;            	    		
	    	    	}
    	    	}
    	    }
    	    else if(child instanceof HBox){
    	    	for(Node n : ((HBox)child).getChildren()){
    	    		if(n instanceof ComboBox){
            	    	@SuppressWarnings("unchecked")
						String selectedVal = ((ComboBox<String>)n).getSelectionModel().getSelectedItem().toString();
            	    	if(selectedVal.equals("Month") || selectedVal.equals("Day") || selectedVal.equals("Year")){
            	    		err.showAlert("Form Error!", "Please enter your birthday.");;     	    		
            	    		return false;
            	    	}   
    	    		}
    	    	}
    	    }
    	}
		return true;
	}
	
	
	public boolean validEmail(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.find();
	}

	public boolean validPassword(String pw) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pw);
        return matcher.find();
	}
	
	public boolean validZipcode(String zipcode){
		Matcher matcher = VALID_ZIPCODE_REGEX.matcher(zipcode);
		return matcher.find();
	}
	
	public boolean validPhone(String phoneNumber){
		Matcher matcher = VALID_PHONE_REGEX.matcher(phoneNumber);
		return matcher.find();
	}	
}
