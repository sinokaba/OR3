

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DropdownMenu extends ComboBox<String>{
	String defaultValue;
	
	public DropdownMenu(String defVal, int numVis, ObservableList<String> l, int s, int end, boolean asc){
		super(addElements(l, s, end, asc));
		defaultValue = defVal;
		
		if(defVal == null){
			super.getSelectionModel().selectFirst();
		}
		else{
			super.setValue(defaultValue);
		}
		super.setVisibleRowCount(numVis);
	}

	public DropdownMenu(ImageView def){
		@SuppressWarnings("unused")
		final ObservableList<Image> data = FXCollections.observableArrayList();
	}
	
	public static ObservableList<String> addElements(ObservableList<String> l, int start, int end, boolean fw){
		if(l != null){
			return l;
		}
		
		ObservableList<String> dropdownItems = FXCollections.observableArrayList();
		if(fw){
			if(l == null){
				for(int i = start; i <= end; i++){
					dropdownItems.add(String.valueOf(i));
				}
			}
		}
		else{
			for(int i = end; i > start; i--){
				dropdownItems.add(String.valueOf(i));
			}		
		}
		return dropdownItems;
	}
	
	public void addClass(String className){
		super.getStyleClass().add(className);
	}
	
	public void clear(){
		super.getSelectionModel().clearSelection();
		super.setValue(defaultValue);
	}
}
