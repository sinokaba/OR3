import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class RestaurantRegUI {
	CustomTextField nameField, zipcodeField, phoneField;
	AutoCompleteTextField addressField;
	GridPane layout;
	Button registerBtn, backBtn;
	private GoogleMapsService mapsApi;
	private int fieldW, fieldH;
	
	public RestaurantRegUI(GoogleMapsService mapsApi, int fieldW, int fieldH){
		this.fieldH = fieldH;
		this.fieldW = fieldW;
		this.mapsApi = mapsApi;
	}
	
	public void buildPage(){		
		layout = new GridPane();
		FormField form = new FormField(fieldW, fieldH);
		
		Label restaurantName = form.createLabel("Business Name:");
		nameField = form.createTextField("Enter business name.", 40);
		createFieldLabelPair(layout, nameField, restaurantName, 1);
		
		Label address = form.createLabel("Address:");
		addressField = new AutoCompleteTextField("Enter street address.", fieldW, fieldH);
		addressField.autocomplete(null, mapsApi);
		createFieldLabelPair(layout, addressField, address, 2);
		
		Label zipcode = form.createLabel("Zipcode:");
		zipcodeField = form.createTextField("Enter zipcode.", 5);
		createFieldLabelPair(layout, zipcodeField, zipcode, 3);
		
		Label phone = form.createLabel("Phone:");
		phoneField = form.createTextField("Enter business phone number.", 10);
		createFieldLabelPair(layout, phoneField, phone, 4);
		/*
		Label hours = form.createLabel("Hours:");
		hoursField = form.createTextField("Enter operating hours.", 16);
		createFieldLabelPair(win.layout, hoursField, hours, 8);
		*/
		backBtn = new Button("Back");
		backBtn.getStyleClass().add("main-button");
		layout.add(backBtn, 0, 7);
		registerBtn = new Button("Register");
		registerBtn.getStyleClass().add("main-button");
		registerBtn.setDefaultButton(true);
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerBtn);
		layout.add(hbBtn, 1, 7);
		
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
	
	public void clearFields(){
		try{
			nameField.clear();
			addressField.clear();
			phoneField.clear();
			zipcodeField.clear();
		}
		catch(NullPointerException err){
			//err.printStackTrace();
		}
	}
	
}
