package main.java;

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
	Button homeBtn, userAccountBtn;
	GridPane layout;
	BorderPane root;
	Stage primaryStage;
	String defaultTitle = "OR3 - Review, Rate, Dine.";
		
	public AppWindow(){
		//changeScene(home, null);
		primaryStage = new Stage();
		layout = new GridPane();
		buildWindow();
	}
	
	/*
	@Override
	public void start(Stage stage){
		primaryStage = stage;		
        //links the css for styling the different views		
        scene.getStylesheets().addAll(getClass().getResource(	
                "/static/css/home.css"
        ).toExternalForm());
        
		//primaryStage.setTitle(windowTitle);
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
	*/
	
	public void buildWindow(){
		root = new BorderPane();
        root.setTop(createWindowMenu());
		Label sceneTitle = new Label("OR3");
		sceneTitle.getStyleClass().add("title");
		sceneTitle.setAlignment(Pos.TOP_CENTER);

		layout.add(sceneTitle, 1, 0);
		
		//numGridElements += 1;
		
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		
		/*
		startPage(this.layout, primaryStage);
		
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	System.out.println("go back home.");
            	layout.getChildren().remove(1, numGridElements);
        		numGridElements = 1;            	
        		//startPage(this.layout, primaryStage);		     
            }
        	
        });
        */
		String backgroundUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/8712449/gbb_food.jpg";
        root.setStyle("-fx-background-image: url('"+backgroundUrl+"');");
        root.setCenter(layout);
        
        //scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);		
   	}
	
	public HBox createWindowMenu(){
        homeBtn = createMenuBtn("brand.png", "OR3", 44);
        userAccountBtn = createMenuBtn("user.png", "", 44);
   
        ToolBar leftBar = new ToolBar();
        leftBar.getItems().addAll(homeBtn);
        ToolBar rightBar = new ToolBar();
        rightBar.getItems().addAll(userAccountBtn);
        
        leftBar.getStyleClass().add("menu-bar");
        rightBar.getStyleClass().add("menu-bar");
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        return menubars;
	}
	
	public Button createMenuBtn(String iconName, String iconTitle, int iconWidth){
        Image icon = new Image(getClass().getResourceAsStream("/static/images/"+iconName));
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
	
	public Stage getStage(){
		return primaryStage;
	}
	
	/*
	public Scene getScene(){
		return scene;
	}
	*/
}
