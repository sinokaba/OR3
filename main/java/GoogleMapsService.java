import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

public class GoogleMapsService {
	GeoApiContext context;
	
	public GoogleMapsService(String apiKey){
		context = new GeoApiContext.Builder().apiKey(apiKey).build();
	}
	
	public void printResult(){
		try {
			GeocodingResult[] results =  GeocodingApi.geocode(context,
				    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
			System.out.println("we win");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(results[0].addressComponents));
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
