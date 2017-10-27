
public class GoogleMapsService {
	
	public GoogleMapsService(String apiKey){
		GeoApiContext context = new GeoApiContext.Builder()
			    .apiKey(apiKey)
			    .build();
	}
}
