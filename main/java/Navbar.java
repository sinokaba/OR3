import org.controlsfx.control.PopOver;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Navbar {
	boolean loggedIn = false;
	Button homeBtn, userAccountBtn, loginBtn, signupBtn, logoutBtn;
	ListView<String> userMenuActions;
	String username = null;
	ToolBar leftBar, rightBar;
	PopOver userMenu;
	
	public HBox createWindowMenu(){
        homeBtn = createMenuBtn("brand.png", "OR3", 44);
        userAccountBtn = createMenuBtn("user.png", "", 44);
        userAccountBtn.setVisible(false);
        loginBtn = new Button("Login");
        logoutBtn = new Button("Logout");
        signupBtn = new Button("Signup");
        signupBtn.managedProperty().bind(signupBtn.visibleProperty());
		
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
        leftBar = new ToolBar();
        leftBar.getItems().add(homeBtn);
        rightBar = new ToolBar();
        rightBar.getItems().addAll(userAccountBtn, signupBtn, loginBtn);
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
		if(rightBar.getItems().contains(signupBtn)){
			rightBar.getItems().remove(signupBtn);
		}
		userMenuActions.getItems().set(0, username);
		rightBar.getItems().remove(loginBtn);
		rightBar.getItems().add(1, logoutBtn);
		loggedIn = true;
	}
	
	public void userLogout(){
		userAccountBtn.setVisible(false);
		rightBar.getItems().add(1, signupBtn);
		if(rightBar.getItems().contains(logoutBtn)){
			rightBar.getItems().remove(logoutBtn);
		}
		if(!rightBar.getItems().contains(loginBtn)){
			rightBar.getItems().add(loginBtn);
		}
		userAccountBtn.setText("");
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
}
