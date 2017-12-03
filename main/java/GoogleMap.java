import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GoogleMap {
	private int width = 410;
	private int height = 230;
	private WebView webView;
	private WebEngine webEngine;
	
	public GoogleMap(){
		webView = new WebView();
		webEngine = webView.getEngine();
	    final java.net.URL urlGoogleMaps = getClass().getResource("/html/googleMaps.html");
	    webEngine.load(urlGoogleMaps.toExternalForm());
	    webEngine.setJavaScriptEnabled(true);
		webView.setMaxSize(width, height);
		//markLocation(initialAddress);
	}
	
	public void markLocation(String address, String name){
		System.out.println("address: " + address);
    	webEngine.executeScript("document.goToLocation(\"" + address + "\", \"" + name + "\")");
	}
	
	public void removeMarkers(){
    	webEngine.executeScript("document.removeAllMarkers()");
	}
	
	public WebView getMap(){
		return webView;
	}
	
	
}
