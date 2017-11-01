
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.QueryAutocompleteRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;

public class GoogleMapsService {
	GeoApiContext geoApi;
	private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());
	
	public GoogleMapsService(String apiKey){
		geoApi = new GeoApiContext.Builder().apiKey(apiKey).build();
	}
	
	public List<String> getPlacesSuggestions(String keyword){
		List<String> results = new ArrayList<String>();
		int maxSuggestions = 5;
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
		String[] location = new String[3];
		boolean foundCity = false;
		boolean foundState = false;
		boolean foundCountry = false;
		try {
			results = GeocodingApi.geocode(geoApi, loc).await();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int index = 0;
			System.out.println(gson.toJson(results[0].formattedAddress));
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
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("country: " + location[2] + ", state: " + location[1] + ", city: " + location[0]);
		return location;
	}
	
}
