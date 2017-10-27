
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RestaurantUI {
	
	Button addRatingBtn;
	CustomTextField ratingField;
	
	public void buildStage(Restaurant res, AppWindow win){
		win.resetLayout();
		
		Label name = new Label("Name: " + res.name);
		Label address = new Label("Addres: " + res.address);
		Label rating = new Label("Rating: " + String.valueOf(res.rating));
		
		name.getStyleClass().add("field");
		address.getStyleClass().add("field");
		rating.getStyleClass().add("field");

		win.layout.add(name, 1, 2);
		win.layout.add(address, 1, 3);
		win.layout.add(rating, 1, 4);

		ratingField = new CustomTextField("Your rating", 50, 20);
		addRatingBtn = new Button("Rate");
		win.layout.add(ratingField, 1, 5);
		win.layout.add(addRatingBtn, 1, 6);
		
		win.updateElementCount(5);
	}
}
