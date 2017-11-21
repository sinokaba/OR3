
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private Connection connection;	
    private Statement statement;
    private GoogleMapsService mapsAPI;
	final private String dbUsername = "rs";
	final private String dbPort = "3306";
	final private String dbName = "or3";
	final private String dbHostName = "or3-db-instance.cyfhhdzaczmo.us-east-1.rds.amazonaws.com";
	final private String dbPassword = "getInternship2017";

	/**
	* The constructor of the DBConnection class
	* Will connect to the database based on input given, or raise exceptions if something goes wrong
	* 
	* @param takes 1 input an instance of the google maps api, to check for location
	* @return no return value
	*/
    public DBConnection(GoogleMapsService api) {
    	mapsAPI = api;
        try {
			Class.forName("com.mysql.jdbc.Driver");
			String jdbcUrl = "jdbc:mysql://" + dbHostName + ":" + dbPort + "/" + dbName;
			connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
			statement = connection.createStatement();
			System.out.println("connection good bruh? " + connection);
        }
        catch (SQLException e) { 
        	System.out.println("something wrong with sql connection.");
        	e.printStackTrace();
        } catch (ClassNotFoundException e) {
        	System.out.println("Class not found 57.");
			e.printStackTrace();
		}  
    }
	/**
	* This method shutdowns and closes connection with db when called.
	* 	 
	* @param takes no input
	* @return no return value
	*/    
    public void disconnect(){
    	try{
    		if(connection != null){
    			connection.close();
    		}
    	}
    	catch(SQLException ex){
    		System.out.println("Something went wrong with sql! Error message: " + ex);    		
    	}
    }
 
	/**
	* This method creates an sql query for inserting a new user in the db
	* 	 
	* @param takes 3 input arguments, the name of the user, their associated password, and their birthdate
	* @return no return value
	*/    
    
    public void insertLocation(String zipcode, String[] location){
    	String sqlQ = "INSERT INTO locations \n"
				+ " (country, state, city, zipcode) "
    			+ " values ('"+location[2] +"', '" + location[1] + "', '" + location[0] + "', '" + zipcode + "')";
    	System.out.println("location q: " + sqlQ);
    	executeQuery(sqlQ, "Added location infor for " + zipcode);
    }
    
    public boolean rowExists(String tableName, String arg, String input){
    	String sqlQ = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE " + arg + " = '"+input+"')";
    	System.out.println("Checking if " + input + " already in table " + tableName);
    	return executeQuery(sqlQ);
    }
    
    public boolean rowExists(String tableName, String arg1, String input1, String arg2, String input2){
    	String sqlQ = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE " + 
    					arg1 + " = '"+input1+"' AND "+ arg2 + " = '" + input2 + "')";
    	System.out.println("Checking if " + input1 + " already in table " + tableName);
    	return executeQuery(sqlQ);    	
    }
    
    public void insertUser(User newUser){
    	java.sql.Date birthday = null;
    	boolean validZipcode = true;
    	String zip = newUser.getZipcode();
    	try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = format.parse(newUser.getBday());
            System.out.println(parsed);
            birthday = new java.sql.Date(parsed.getTime());
    	}
    	catch(ParseException e){
    		System.out.println("Something wrong with date. Stack trace:");
    		e.printStackTrace();
    	}
    	if(!rowExists("locations", "zipcode", zip)){
    		String loc[] = mapsAPI.getLocation(zip);
    		if(loc[0] != null){
    			insertLocation(zip, loc);
    		}
    		else{
    			validZipcode = false;
    		}
    	}
    	if(validZipcode){
	    	System.out.println("birthday: " + birthday);
			String sqlQ = "INSERT INTO users \n"
						+ " SET username = '" + newUser.getUsername() + "',\n"
						+ "  email = '" + newUser.getEmail() + "',\n"
						+ "  birthdate = '" + birthday + "',\n"
						+ "  password = '" + newUser.getPassword() + "',\n"
						+ "  fk_location = (SELECT idlocations FROM locations WHERE zipcode = '" + zip + "')";	
			System.out.println(sqlQ);
			executeQuery(sqlQ, "User: " + newUser.getUsername() + " added");
    	}
    	else{
    		System.out.println("User could not be added because entered zipcode is not supported.");
    	}
    }
    
    public ResultSet getQueryResult(String table, String field1, String val1, String field2, String val2){
    	String sqlQ = "Select * from " + table + " Where " + field1 + "='" + val1 + "' and " + field2 + "='" + val2 + "'";
    	return getQueryResultSet(sqlQ);
    }
    
    public List<String> getRestaurantSuggestions(String keyword, String loc){
    	List<String> suggestions = new ArrayList<String>();
		try {
			String[] geoLocation = new String[3];
	    	if(loc != null){
	    		System.out.println("location entred: " + loc);
	    		String[] locationSplit = loc.split(",");
	    		String country = locationSplit[locationSplit.length - 1].trim();
	    		System.out.println("country: " + country + country.toLowerCase().equals("united states"));
	    		if(country.toLowerCase().equals("united states")){
	    			geoLocation[0] = "US";
		    		geoLocation[1] = locationSplit[locationSplit.length - 2].trim();
		    		if(locationSplit.length >= 3){
		    			geoLocation[2] = locationSplit[locationSplit.length - 3].trim();
		    		}
	    		}
	    	}
    		String locStatement = "SELECT idlocations FROM locations WHERE locations.state = '" + geoLocation[1];
	    	if(geoLocation[2] != null){
	    		locStatement = "SELECT idlocations FROM locations WHERE locations.state = '" + geoLocation[1] + "' and locations.city = '" + geoLocation[2] +"'";
	    	}
	    	else{
	    		locStatement = "SELECT idlocations FROM locations WHERE locations.state = '" + geoLocation[1] + "'";
	    	}
			if(keyword.length() >= 1){
	    		String sqlStatement = "SELECT * FROM restaurants WHERE restaurants.name LIKE ? ESCAPE '!'";
	    		if(geoLocation[2] != null){
	    			sqlStatement = "SELECT * FROM restaurants WHERE restaurants.name LIKE ? ESCAPE '!' AND restaurants.fk_location IN (" + locStatement + ")";
	    		}
				keyword = keyword
						.replace("!", "!!")
						.replace("_", "!_")
						.replace("%", "!%")
						.replace("'", "!'")
						.replace("[", "![");
				PreparedStatement pStatement = connection.prepareStatement(sqlStatement);
				pStatement.setString(1, "%" + keyword + "%");
				System.out.println(pStatement);
	        	ResultSet res = pStatement.executeQuery();
				while(res.next()) {
				    if(res.getInt(1) > 0){
						System.out.println(res.getInt(1));
						suggestions.add(res.getString("name"));
				    }
				}
			}
			else{
				if(geoLocation[0] != null){
			    	PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM restaurants WHERE restaurants.fk_location IN (" + locStatement + ")");
			    	System.out.println(pStatement);
		        	ResultSet res = pStatement.executeQuery();
					while(res.next()) {
					    if(res.getInt(1) > 0){
							System.out.println(res.getInt(1));
							suggestions.add(res.getString("name"));
					    }
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return suggestions;
    }
    
    public ResultSet getQueryResultReviews(User user, Restaurant restaurant){
		ResultSet rs = null;
		try {
			String getUserIdQ = "SELECT iduser FROM users WHERE username = '" + user.getUsername() + "'";
			ResultSet rUser = getQueryResultSet(getUserIdQ);
			int userId = rUser.getInt(1);
			String getRstIdQ = "SELECT idrestaurant FROM restaurants WHERE name = '" + restaurant.getName() + "'";
			ResultSet rRst = getQueryResultSet(getRstIdQ);
			int rstId = rRst.getInt(1);
	    	String sqlQ = "Select * from reviews Where fk_user = " + userId + " and fk_restaurant = " + rstId + ";";
	    	rs = getQueryResultSet(sqlQ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
    }
	/**
	* This method creates an sql query for inserting a new restaurant in the db
	* 	 
	* @param takes 2 input arguments, the name of the restaurant, and their address
	* More fields will soon be added, just testing right now
	* @return no return value
	*/    
    public void insertRestaurant(Restaurant restaurant){
    	boolean validZip = true;
    	String zip = restaurant.zip;
    	if(!rowExists("locations", "zipcode", zip)){
    		String loc[] = mapsAPI.getLocation(zip);
    		if(loc[0] != null){
    			insertLocation(zip, loc);
    		}
    		else{
    			validZip = false;
    		}
    	}
    	if(validZip){
    		String safeAddress = restaurant.address.replace("'", "''");
    		String safeName = restaurant.name.replace("'", "''");
    		String sqlQ = "INSERT INTO restaurants \n"
					+ " SET name = '" + safeName + "',\n"
					+ "  address = '" + safeAddress + "', \n"
					+ "  phone = '" + restaurant.phone + "',\n"
					+ "  fk_location = (SELECT idlocations FROM locations WHERE zipcode = '" + zip + "')";	
    		System.out.println(sqlQ);
			executeQuery(sqlQ, "User: " + restaurant.name + " added");
    	}
    }
    
	/**
	* This method creates an sql query for inserting a new review in the db
	* 	 
	* @param takes 4 input, the restaurant associated with the review, the user that created the review
	* the rating they gave, and their comments
	* 
	* @return no return value
	*/    
    public void insertReview(Review review){
    	String restaurantName = review.getRestaurantName().replace("'", "\\'");
    	System.out.println("restaurant name: " + restaurantName);
    	String comments = review.getComments().replace("'", "\\'");
		String sqlQ = "INSERT INTO reviews \n"
				+ " SET overall_rating = '" + review.getRating() + "',\n"
				+ "  comments = '" + comments + "', \n"
				+ "  fk_user = (SELECT iduser FROM users WHERE username = '" + review.getUserName() + "'), \n"	
				+ "  fk_restaurant = (SELECT idrestaurant FROM restaurants WHERE name = '" + restaurantName + "')";	
		System.out.println(sqlQ);
		
    	executeQuery(sqlQ, "Review for: " + review.getRestaurantName() + " by: " + review.getUserName() + " added");
    }
    
    public void deleteReview(Review review){
    	String sqlQ = "DELETE FROM reviews WHERE comments = '" + review.getComments() + "' AND fk_user = " + review.getUserId();
    	executeQuery(sqlQ, "Deleted review by " + review.getUserId());
    }
    
	/**
	* This method drops the specified table from the database
	* 	 
	* @param takes one argument of type string, the name of the table to remove
	* @return no return value
	*/    
    public void removeTable(String tableName){
    	executeQuery("drop table if exists " + tableName, "Dropped table " + tableName);
    }
    
	/**
	* This method removes all data currently in the table
	* 	 
	* @param takes 1 input argument, the name of the table to clear the data from
	* @return no return value
	*/    
    public void clearTable(String tableName){
    	String disableFK = "SET FOREIGN_KEY_CHECKS=0;";
    	String clearTable = "TRUNCATE " + tableName + ";";
    	String enableFK = "SET FOREIGN_KEY_CHECKS=1;";
    	executeQuery(disableFK, "disabled foreign keys");
    	executeQuery(clearTable, "Cleared table " + tableName);
    	executeQuery(enableFK, "enabled foreign keys");
    }
    
	/**
	* This method prints all the data along with their fields in the specified table
	* 	 
	* @param takes 1 input, the table name as a string
	* @return no return value
	*/    
    public void printTableData(String tableName){
        try{
        		ResultSet res = statement.executeQuery("select * from " + tableName);
        		String[] keys = {"username", "password", "email"};
        		if(tableName.contains("restaurant")){
        			keys[0] = "name";
        			keys[1] = "address";
        			keys[2] = "phone";	
        		}
        		else if(tableName.contains("locations")){
        			keys[0] = "state";
        			keys[1] = "city";
        			keys[2] = "country";        			
        		}
            	//List<User> personList = new ArrayList<>();
                while (res.next()) {
                    String val1 = res.getString(keys[0]);
                    String val2 = res.getString(keys[1]);
                    String val3 = res.getString(keys[2]);
                    System.out.println(val1 + ", " + val2 + ", " + val3);
                    //User person = new User(firstName, lastName, email);
                    //personList.add(person);
                }
            }
        catch(SQLException e){
        	System.out.println("Error occured, can't print table " + tableName);
        	e.getStackTrace();
        }
    }
    
	/**
	* This method executes the query it is given
	* 	 
	* @param takes two arguments, the query as a string, and the message associated with the query(determined by user)
	* @return no return value
	*/    
    public void executeQuery(String query, String queryMessage){
    	try{
    		System.out.println("query: " + query);
    		statement.executeUpdate(query);
    		System.out.println(queryMessage + " successfully.");
    	}
    	catch(SQLException ex){
    		System.out.println("SQl exception erro: " + ex);
    	}      	
    }

    public boolean executeQuery(String query){
    	boolean queryRes = false;
    	try{
    		//System.out.println(statements.executeUpdate(query));
    		ResultSet rs = statement.executeQuery(query);
    		System.out.println("result of q: " + rs);
    		if(rs.next()) {
    		    if(rs.getInt(1) > 0){
    		    	queryRes = true;
    		    }
    		}
    	}
    	catch(SQLException ex){
    		System.out.println("SQl exception erro: " + ex);
    	}      	
    	return queryRes;
    }
    
    public User getUserFromDB(String username, String password){
		User usr = null;
    	try{
			ResultSet rs = statement.executeQuery("Select * from users Where username='" + username + "' and password='" + password + "'");
			System.out.println("result of q: 269 " + rs);
			if(rs.next()) {
			    if(rs.getInt(1) > 0){
					System.out.println(rs.getInt(1));
			    	System.out.println("transferring data from db to user class.");
			    	String name = rs.getString("username");
			    	String pw = rs.getString("password");
			    	String birthdate = rs.getString("birthdate");
			    	String email = rs.getString("email");
			    	String locId = rs.getString("fk_location");
			    	int privilege = rs.getInt("privilege");
			    	int userId = rs.getInt(1);
			    	usr = new User(name, pw, birthdate, email, locId, privilege);
			    	System.out.println("id from db of user: " + userId);
			    	usr.setId(userId);
			    }
			}
    	}
    	catch(SQLException ex){
    		System.out.println("db error getting user from db line 287.");
    		ex.printStackTrace();
    	}
		return usr;
    }
    
    public Restaurant getRestaurantFromDB(String name, String addrs){
    	System.out.println("getting restaurant from db..");
		Restaurant rst = null;
    	try{
    		name = name.replace("'", "''");
    		if(addrs != null){
    			addrs = addrs.replace("'", "''");
    		}
    		String query = "Select * from restaurants Where name='" + name + "' and address='" + addrs + "';";
    		if(addrs == null){
    			query = "Select * from restaurants Where name='" + name + "';";
    		}
			ResultSet rs = statement.executeQuery(query);
			System.out.println("result of q: 297" + rs);
			if(rs.next()) {
			    if(rs.getInt(1) > 0){
			    	System.out.println("transferring data from db to restaurant class.");
			    	String rstName = rs.getString("name");
			    	String address = rs.getString("address");
			    	String phone = rs.getString("phone");
			    	int rstId = rs.getInt(1);
			    	String locId = rs.getString("fk_location");
			    	rst = new Restaurant(rstName, phone);
			    	rst.setAddress(address, locId);
			    	System.out.println("id from db of restaurnt: " + rstId);
			    	rst.setId(rstId);
			    }
			}
    	}
    	catch(SQLException ex){
    		System.out.print("Db error getting restaurant from db line 315.");
    		ex.printStackTrace();
    	}
		return rst;
    }
    
    public List<Review> getRestaurantReviewsFromDB(Restaurant restaurant){
    	List<Review> reviewList = new ArrayList<Review>();
    	String restaurantName = restaurant.getName().replace("'", "\'");
    	try{
    		PreparedStatement queryReviews = connection.prepareStatement("SELECT * FROM reviews WHERE fk_restaurant IN (SELECT idrestaurant FROM restaurants WHERE name = ?)");
    		//String reviewsQuery = "SELECT * FROM reviews WHERE fk_restaurant IN (SELECT idrestaurant FROM restaurants WHERE name = '" + restaurantName +"' ESCAPE '!')";
    		//System.out.println(reviewsQuery);
    		queryReviews.setString(1, restaurantName);
    		System.out.println(queryReviews);
    		ResultSet rs = queryReviews.executeQuery();
    		while(rs.next()){
    			if(rs.getInt(1) > 0){
    				Date creationDate = rs.getDate("creation_date");
    				String comments = rs.getString("comments");
    				double rating = rs.getDouble("overall_rating");
    				int userId = rs.getInt("fk_user");
    				String userQuery = "SELECT * FROM users WHERE iduser = " + userId;
    				System.out.println(userQuery);
    				ResultSet userRs = statement.executeQuery(userQuery);
    				User usr = null;
    				if(userRs.next()){
    					if(userRs.getInt(1) > 0){
    						String name = userRs.getString("username");
    				    	String pw = userRs.getString("password");
    				    	String birthdate = userRs.getString("birthdate");
    				    	String email = userRs.getString("email");
    				    	String locId = userRs.getString("fk_location");
    				    	int privilege = userRs.getInt("privilege");
    				    	usr = new User(name, pw, birthdate, email, locId, privilege);
    				    	//System.out.println("id from db of user: " + userId);
    				    	usr.setId(userId);    					
    				    }
    				}
    				Review review = new Review(rating, comments, usr, restaurant);
    				review.setCreationDate(creationDate);
    				reviewList.add(review);
    			}
    		}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return reviewList;
    }
    public ResultSet getQueryResultSet(String query){
		ResultSet res = null;
    	try{
			ResultSet rs = statement.executeQuery(query);
			System.out.println("result of q: " + rs);
			if(rs.next()) {
				System.out.println(rs.getInt(1));
			    if(rs.getInt(1) > 0){
			    	res = rs;
			    }
			}
    	}
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
		return res;
    }
    
    public List<String> getQueryResultList(String query){
		List<String> res = new ArrayList<String>();
    	try{
    		System.out.println("363 query: " + query);
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()) {
			    if(rs.getInt(1) > 0){
					System.out.println(rs.getInt(1));
					if(res.size() < 10){
						res.add(rs.getString("name"));
					}
					else{
						break;
					}
			    }
			}
    	}
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	//System.out.println("restaurant suggestions: " + res);
		return res;
    }
    
}
