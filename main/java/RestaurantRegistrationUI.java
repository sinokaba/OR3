
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class RestaurantRegistrationUI {
	CustomTextField nameField, addressField, cityField, stateField, zipcodeField, phoneField;
	Button registerBtn;
	
	public void buildStage(AppWindow win, int w, int h){
		win.resetLayout();
		
		FormField form = new FormField(w, h);
		
		Label restaurantName = form.createLabel("Business Name:");
		nameField = form.createTextField("Enter business name.", 40);
		createFieldLabelPair(win.layout, nameField, restaurantName, 2);
		
		Label address = form.createLabel("Address:");
		addressField = form.createTextField("Enter street address.", 75);
		createFieldLabelPair(win.layout, addressField, address, 3);
		
		Label zipcode = form.createLabel("Zipcode:");
		zipcodeField = form.createTextField("Enter zipcode.", 5);
		createFieldLabelPair(win.layout, zipcodeField, zipcode, 4);
		
		Label phone = form.createLabel("Phone:");
		phoneField = form.createTextField("Enter business phone number.", 10);
		createFieldLabelPair(win.layout, phoneField, phone, 5);
		/*
		Label hours = form.createLabel("Hours:");
		hoursField = form.createTextField("Enter operating hours.", 16);
		createFieldLabelPair(win.layout, hoursField, hours, 8);
		*/
		registerBtn = new Button("Register Restaurant.");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerBtn);
		win.layout.add(hbBtn, 1, 9);
		
		win.updateElementCount(9);
		
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
