<<<<<<< HEAD

=======
package main.java;
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AppWindow{
	private int numElements = 1;
	Button homeBtn, userAccountBtn, logoutBtn;
	GridPane layout;
	BorderPane root;
	Stage primaryStage;
	String defaultTitle = "OR3 - Review, Rate, Dine.";
	ToolBar leftBar, rightBar;
		
	public AppWindow(){
		primaryStage = new Stage();
		layout = new GridPane();
		buildWindow();
	}
	
	public void buildWindow(){
		root = new BorderPane();
        root.setTop(createWindowMenu());
		Label sceneTitle = new Label("OR3");
		sceneTitle.getStyleClass().add("title");
		sceneTitle.setAlignment(Pos.TOP_CENTER);

		layout.add(sceneTitle, 1, 0);		
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
	
		String backgroundUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/8712449/gbb_food.jpg";
        root.setStyle("-fx-background-image: url('"+backgroundUrl+"');");
        root.setCenter(layout);
   	}
	
	public HBox createWindowMenu(){
        homeBtn = createMenuBtn("brand.png", "OR3", 44);
        userAccountBtn = createMenuBtn("user.png", "", 44);
		logoutBtn = new Button("Logout");
		logoutBtn.setVisible(false);
        //DropdownMenu userMenu = new DropdownMenu(homeBtn, numElements, null, numElements, numElements, false);
        
        leftBar = new ToolBar();
        leftBar.getItems().add(homeBtn);
        rightBar = new ToolBar();
        rightBar.getItems().add(userAccountBtn);
        rightBar.getItems().add(logoutBtn);
        
        leftBar.getStyleClass().add("menu-bar");
        rightBar.getStyleClass().add("menu-bar");
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        return menubars;
	}
	
	public void userLogin(String newTitle){
		userAccountBtn.setText(newTitle);
		logoutBtn.setVisible(true);
	}
	
	public void userLogout(){
		userAccountBtn.setText("");
		logoutBtn.setVisible(false);
	}
	
	public Button createMenuBtn(String iconName, String iconTitle, int iconWidth){
<<<<<<< HEAD
        Image icon = new Image(getClass().getResourceAsStream("/images/"+iconName));
=======
        Image icon = new Image(getClass().getResourceAsStream("/static/images/"+iconName));
>>>>>>> 236a6548d6f5e6ea026942fdef416a8e06ba3122
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(iconWidth);
        iconView.setFitHeight(iconWidth);
        
        Button btn = new Button(iconTitle, iconView);
        btn.getStyleClass().add("menuButton");
        
        return btn;
	}
	
	public void updateElementCount(int n){
		numElements += n;
		System.out.println("Number of elements: " + numElements);
	}
	
	public int getNumElements(){
		return numElements;
	}
	
	public void resetLayout(){
		layout.getChildren().remove(1, numElements);
		numElements = 1;
	}
}
