

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

///import org.json.*;

public class GoogleMapsRequest {
	  private String apiBase = "https://maps.googleapis.com/maps/api/";
	  private String apiKey = null;
	  private Pattern VALID_ADDRESS =
				Pattern.compile("^(?=.*[0-9])$");
	  
	  GoogleMapsRequest(String apiKey){
		  this.apiKey = apiKey;
	  }
	  
	  public String[] getGeolocation(String zipcode){
		  return sendRequest("geocode/json?address="+zipcode+"&key="+apiKey);
	  }
	  
	  public String[] sendRequest(String params){
		  try{
			  URL apiURL = new URL(apiBase + params);
			  URLConnection conn = apiURL.openConnection();
			  InputStreamReader reader = new InputStreamReader(conn.getInputStream());
			  BufferedReader output = new BufferedReader(reader);
			  return readResponse(output);
		  }
		  catch(IOException ex){
			  System.out.println("Something went wrong while sending request! Stack trace: ");
			  ex.printStackTrace();
		  }
		  return null;
	  }
	  
	  public String[] readResponse(BufferedReader data){
		  //System.out.println(data);
		  String[] loc = new String[2];
		  String line;
		  try{
			  while((line = data.readLine()) != null){
				  System.out.println(line);
				  if(line.contains("formatted_address")){
					 if(line.contains("USA")){
						  String[] sp = line.split(":");
						  String[] sp2 = sp[1].split(",");
						  String[] sp3 = sp2[1].split(" ");
						  String city = sp2[0].substring(2);
						  System.out.println("City: " + city + ", State: " + sp3[1]);
						  Matcher matcher = VALID_ADDRESS.matcher(sp3[1]);
						  if(matcher.find()){
							  return loc;
						  }
						  else{
							  loc[0] = city;
							  loc[1] = sp3[1];
							  return loc;
						  }
					  }
					  else{
						  System.out.println("Sorry only looking at USA based restaurants atm.");
						  return loc;
					  }
				  }
				  
			  }
		  }
		  catch(IOException ex){
			  System.out.println("Something spooky happened while reading response! Stack trace: ");
			  ex.printStackTrace();
		  }
		  return loc;
	  }
	  
	  
}
