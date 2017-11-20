
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.QueryAutocompleteRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;

public class GoogleMapsService {
	GeoApiContext geoApi;
	//private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());
	
	public GoogleMapsService(String apiKey){
		geoApi = new GeoApiContext.Builder().apiKey(apiKey).build();
	}
	
	public List<String> getPlacesSuggestions(String keyword){
		List<String> results = new ArrayList<String>();
		//List<CustomMenuItem> results = new LinkedList<>();
		int maxSuggestions = 4;
		if(keyword.trim().length() >= 2){
			System.out.println("k: " + keyword);
			PlaceAutocompleteRequest places = PlacesApi.placeAutocomplete(geoApi, keyword);
			AutocompletePrediction[] placesOptions = places.awaitIgnoreError();
			if(placesOptions.length < 5){
				maxSuggestions = placesOptions.length;
			}
			for(int i=0; i < maxSuggestions; i++){
				//getLocation(placesOptions[i].description);
				//System.out.println(placesOptions[i].description);
				results.add(placesOptions[i].description);
			}
		}
		return results;
	}
	
	public String[] getLocation(String loc){
		GeocodingResult[] results;
		String[] location = new String[4];
		boolean foundCity = false;
		boolean foundState = false;
		boolean foundCountry = false;
		boolean foundPostalCode = false;
		try {
			results = GeocodingApi.geocode(geoApi, loc).await();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int index = 0;
			//System.out.println(gson.toJson(results[0].formattedAddress) + " num results: " + results.length);
			while((!foundCity || !foundState || !foundCountry || !foundPostalCode) && index < results[0].addressComponents.length){
				String type = gson.toJson(results[0].addressComponents[index].types);
				System.out.println("type: " + type);
				if(type.toLowerCase().contains("locality")){
					foundCity = true;
					location[0] = gson.toJson(results[0].addressComponents[index].longName).trim().replace("\"", "");
				}
				if(type.toLowerCase().contains("administrative_area_level_1")){
					foundState = true;
					location[1] = gson.toJson(results[0].addressComponents[index].shortName).trim().replace("\"", "");
				}
				if(type.toLowerCase().contains("country")){
					foundCountry = true;
					location[2] = gson.toJson(results[0].addressComponents[index].shortName).trim().replace("\"", "");
				}
				if(type.toLowerCase().contains("postal")){
					foundPostalCode = true;
					location[3] = gson.toJson(results[0].addressComponents[index].shortName).trim().replace("\"", "");
				}
				index += 1;
			}
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("country: " + location[2] + ", state: " + location[1] + ", city: " + location[0] + " zip: " + location[3]);
		return location;
	}
	
	public String getDistance(String[] origin, String[] dest){
		try {
			DistanceMatrix des = DistanceMatrixApi.getDistanceMatrix(geoApi, origin, dest).await();
			//System.out.println(des.destinationAddresses.toString());
			//System.out.println(des.originAddresses);
			for(int i=0; i < des.rows.length; i++){
				for(int j=0; j < des.rows[i].elements.length; j++){
					System.out.println("distance: " + des.rows[i].elements[j].distance);
					System.out.println("duration: " + des.rows[i].elements[j].duration);
				}
			}
			//System.out.println(des.rows[0].);
			//System.out.println(des.rows[0].elements[0].distance);
			System.out.println(des.rows[0].elements[0].duration);
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDirections(String origin, String dest){
		try {
			DirectionsResult directions = DirectionsApi.getDirections(geoApi, origin, dest).await();
			System.out.println(directions.routes[0].bounds);
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
