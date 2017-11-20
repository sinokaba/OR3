import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.util.LinkedList;
import java.util.List;

public class AutoCompleteTextField extends CustomTextField{
	private ContextMenu suggestionsPopup;
	private PauseTransition pause;
	private final static double delay = .65;
	private int width, height;
	private int maxEntries = 5;
	
	public AutoCompleteTextField(String ph, int w, int h){
		super(ph, h, w);
		width = w;
		height = h;
		suggestionsPopup = new ContextMenu();
		pause = new PauseTransition(Duration.seconds(delay));
		focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void  changed(ObservableValue<? extends Boolean> arg0, Boolean oldV, Boolean newV){
				if(!newV){
					suggestionsPopup.hide();
				}
			}
		});
	}
	
	public void autocomplete(DBConnection db, GoogleMapsService mapsApi){
		textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldStr, String newStr){
				String userInput = getText().trim();
				boolean showing = suggestionsPopup.isShowing();
				System.out.println("user input: " + newStr);
				if(userInput.length() <= 1 || oldStr == newStr){
					if(showing){
						suggestionsPopup.hide();
					}
				}
				else{
					if(mapsApi != null || db != null){
						List<String> suggestions = null;
						System.out.println(mapsApi);
						if(mapsApi != null){
							suggestions = mapsApi.getPlacesSuggestions(userInput);
						}
						else{
							suggestions = db.getRestaurantSuggestions(userInput, null);
						}
						if(!suggestions.isEmpty()){
							populatePopup(suggestions);
							if(!showing){
								suggestionsPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
							}
						}
					}
				}
				pause.play();
			}
		});
	}
	
	public void populatePopup(List<String> options){
		List<CustomMenuItem> optionList = new LinkedList();
		int numOptions = options.size();
		if(numOptions < maxEntries){
			maxEntries = numOptions;
		}
		for(int i=0; i < numOptions; i++){
			String option = options.get(i);
			Label optionLbl = new Label(option);
			optionLbl.setPrefSize(width-20, height-10);
			CustomMenuItem optionItem = new CustomMenuItem(optionLbl);
			optionItem.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent act){
					setText(option);
					suggestionsPopup.hide();
				}
			});
			optionList.add(optionItem);
		}
		suggestionsPopup.getItems().clear();
		suggestionsPopup.getItems().addAll(optionList);
	}
}