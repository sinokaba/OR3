
import org.controlsfx.control.PopOver;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AppWindow{
	private int numElements = 1;
	boolean loggedIn = false;
	Button homeBtn, userAccountBtn, userStatus, loginBtn;
	GridPane layout;
	ListView<String> userMenuActions;
	BorderPane root;
	String username = null;
	ToolBar leftBar, rightBar;
	PopOver userMenu;
		
	public AppWindow(){
		layout = new GridPane();
		buildWindow();
	}
	
	public void buildWindow(){
		root = new BorderPane();
        root.setTop(createWindowMenu());	
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		
        root.setCenter(layout);
   	}
	
	public HBox createWindowMenu(){
        homeBtn = createMenuBtn("brand.png", "OR3", 44);
        userAccountBtn = createMenuBtn("user.png", "", 44);
        userAccountBtn.setVisible(false);
		userStatus = new Button("Login");
		
        userMenu = new PopOver();
		userMenu.setDetachable(false);
		userMenu.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
		
		userMenuActions = new ListView<String>();
        userMenuActions.setPrefSize(200, 220);
		userMenuActions.getItems().addAll(username,"Account","Reviews","Messages","Add restaurant");
		
		userMenu.setContentNode(userMenuActions);

		userMenu.autoHideProperty();
	    userMenu.hide();
	    userAccountBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	userMenu.show(userAccountBtn);
            };
	    });
        //DropdownMenu userMenu = new DropdownMenu(homeBtn, numElements, null, numElements, numElements, false);
        
        leftBar = new ToolBar();
        leftBar.getItems().add(homeBtn);
        rightBar = new ToolBar();
        rightBar.getItems().add(userAccountBtn); 
        rightBar.getItems().add(userStatus);
        leftBar.getStyleClass().add("menu-bar");
        rightBar.getStyleClass().add("menu-bar");
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        return menubars;
	}
	
	public void userLogin(String username){
		userAccountBtn.setVisible(true);
		userMenuActions.getItems().set(0, username);
		userStatus.setText("Logout");
		loggedIn = true;
	}
	
	public void userLogout(){
		userAccountBtn.setVisible(false);
		userAccountBtn.setText("");
		userStatus.setText("Login");
		loggedIn = false;
	}
	
	public Button createMenuBtn(String iconName, String iconTitle, int iconWidth){
        Image icon = new Image(getClass().getResourceAsStream("/images/"+iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(iconWidth);
        iconView.setFitHeight(iconWidth);
        
        Button btn = new Button(iconTitle, iconView);
        btn.getStyleClass().add("menuButton");
        
        return btn;
	}
	
	public void resetLayout(){
		layout.getChildren().clear();
		layout.getColumnConstraints().clear();
	}
}
