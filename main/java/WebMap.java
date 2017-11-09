import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebMap {
	/*
	 * Map from google maps that shows location of restaurant, will be a small picture in user search page
	 * will also show up for the restaurant's page
	 */
	final WebEngine webEngine;
	public WebMap(){
		WebView webView = new WebView();
		webEngine = webView.getEngine();
		webEngine.load("/html/googleMap.html");
	}
}
