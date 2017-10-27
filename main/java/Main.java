import java.io.IOException;

import com.google.maps.errors.ApiException;

public class Main{

	public static void main(String[] args){
		//javafx.application.Application.launch(StartUI.class);
		
		javafx.application.Application.launch(UIMediator.class);
		GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		mapsAPI.printResult();
		System.out.println("ok?");
		mapsAPI.printResult();
	}

}
