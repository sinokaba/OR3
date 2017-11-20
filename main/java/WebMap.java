import javax.print.DocFlavor.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebMap extends Application {
    
    private Scene scene;
    MyBrowser myBrowser;
     
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        myBrowser = new MyBrowser();
        scene = new Scene(myBrowser, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
     
    class MyBrowser extends Region {
        HBox toolbar;
         
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
         
         
        public MyBrowser(){
          
            final java.net.URL urlGoogleMaps = getClass().getResource("/html/googleMaps.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            webEngine.setJavaScriptEnabled(true);
             
            getChildren().add(webView);
          
      }
    }
}