
public class Main{

	public static void main(String[] args) {
		//javafx.application.Application.launch(UIMediator.class);
		//GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		//System.out.println(mapsAPI.getLocation("11111"));
		//mapsAPI.getAutocompleteRes("bad");
		GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
		DBConnection db = new DBConnection("jdbc:mysql://localhost:3306/2102_or3?autoReconnect=true&useSSL=false", "root", "allanK0_ph", mapsAPI);
		db.printTableData("restaurants");
		db.clearTable("restaurants");
		db.printTableData("restaurants");
	}

}
