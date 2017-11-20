import org.controlsfx.control.PopOver;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

public class Navbar {
	boolean loggedIn = false;
	Button homeBtn, userAccountBtn, loginBtn, signupBtn, logoutBtn;
	ListView<String> userMenuActions;
	private String username = null;
	private Label usernameLabel;
	private ToolBar leftBar, rightBar;
	private PopOver userMenu;
	
	public HBox createWindowMenu(){
		usernameLabel = new Label();
		usernameLabel.getStyleClass().add("h3");
        homeBtn = createMenuBtn("brand.png", "OR3", 44);
		
        IconFontFX.register(FontAwesome.getIconFont());
        IconNode iconNode = new IconNode(FontAwesome.USER_CIRCLE_O);
        iconNode.setIconSize(44);
        iconNode.setFill(Color.WHITE);
        userAccountBtn = new Button("", iconNode);
        userAccountBtn.getStyleClass().add("menu-button");
        userAccountBtn.setOnMouseEntered(e -> iconNode.setFill(Color.DEEPSKYBLUE));
        userAccountBtn.setOnMouseExited(e -> iconNode.setFill(Color.WHITE));
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
		userMenuActions.getItems().addAll("Account","Reviews","Messages","Add restaurant");
		
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
        rightBar.getItems().addAll(usernameLabel, userAccountBtn, signupBtn, loginBtn);
        leftBar.getStyleClass().add("menu-bar");
        rightBar.getStyleClass().add("menu-bar");
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        return menubars;
	}
	
	public void userLogin(String username){
		usernameLabel.setText(username);
		userAccountBtn.setVisible(true);
		if(rightBar.getItems().contains(signupBtn)){
			rightBar.getItems().remove(signupBtn);
		}
		rightBar.getItems().remove(loginBtn);
		rightBar.getItems().add(2, logoutBtn);
		loggedIn = true;
	}
	
	public void userLogout(){
		usernameLabel.setText("");
		userAccountBtn.setVisible(false);
		rightBar.getItems().add(2, signupBtn);
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
        btn.getStyleClass().add("menu-button");
        return btn;
	}	
	
}
