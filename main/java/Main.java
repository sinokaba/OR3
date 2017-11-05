import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main{

	public static void main(String[] args) {
		javafx.application.Application.launch(UIMediator.class);
		//GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		//System.out.println(mapsAPI.getLocation("11111"));
		//mapsAPI.getPlacesSuggestions("bad");
		GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		//DBConnection db = new DBConnection("jdbc:mysql://localhost:3306/2102_or3?autoReconnect=true&useSSL=false", "root", "allanK0_ph", mapsAPI);
		//db.printTableData("restaurants");
		//db.clearRestaurants();
		//db.printTableData("restaurants");
		//String[] l1 = mapsAPI.getLocation("06830");
		//String[] l2 = mapsAPI.getLocation("garden catering");
		//System.out.print(l1 + " " + l2);
		//mapsAPI.getDirections("06830", "06268");
		//String[] or = new String[]{"Greenwich", "CT"};
		
		//mapsAPI.getDistance(new String[]{"Washington", "DC"}, new String[]{"New York City", "NY"});
	}

}
