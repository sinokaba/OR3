
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

public class GoogleMapsService {
	GeoApiContext geoAPI;
	
	public GoogleMapsService(String apiKey){
		geoAPI = new GeoApiContext.Builder().apiKey(apiKey).build();
	}
	
	public String[] getLocation(String zip){
		GeocodingResult[] results;
		String[] location = new String[3];
		boolean foundCity = false;
		boolean foundState = false;
		boolean foundCountry = false;
		try {
			results = GeocodingApi.geocode(geoAPI, zip).await();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int index = 0;
			while((!foundCity || !foundState || !foundCountry) && index < results[0].addressComponents.length){
				String type = gson.toJson(results[0].addressComponents[index].types);
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
				index += 1;
			}
			System.out.println("country: " + location[2] + ", state: " + location[1] + ", city: " + location[0]);
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}
	
}
