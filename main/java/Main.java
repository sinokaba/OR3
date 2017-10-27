
public class Main{

	public static void main(String[] args) {
		//javafx.application.Application.launch(StartUI.class);
		
		//javafx.application.Application.launch(UIMediator.class);
		GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		System.out.println(mapsAPI.getLocation("11111"));
		String[] test = new String[2];
		test[0] = "hmm";
		test[1] = "quotes?";
		System.out.print(test[0] + " " + test[1]);
	}

}
