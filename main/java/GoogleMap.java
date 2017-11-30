import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GoogleMap {
	private int width = 400;
	private int height = 225;
	private WebView webView;
	private WebEngine webEngine;
	
	public GoogleMap(String initialAddress){
		webView = new WebView();
		webEngine = webView.getEngine();
	    final java.net.URL urlGoogleMaps = getClass().getResource("/html/googleMaps.html");
	    webEngine.load(urlGoogleMaps.toExternalForm());
	    webEngine.setJavaScriptEnabled(true);
		webView.setMaxSize(width, height);
		markLocation(initialAddress);
	}
	
	public void markLocation(String address){
		System.out.println("address: " + address);
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == State.SUCCEEDED) {
                   webEngine.executeScript("document.goToLocation(\"" + address + "\")");
                }
            }
        });
	}
	
	public WebView getMap(){
		return webView;
	}
	
	
}
