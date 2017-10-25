package main.java;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DBConnection {
    private Connection connection;	
    private Statement statement;
    private GoogleMapsRequest mapsAPI;

	/**
	* The constructor of the DBConnection class
	* Will connect to the database based on input given, or raise exceptions if something goes wrong
	* 
	* @param takes 3 input, all string, the database url, and the user and password associated with the db
	* @return no return value
	*/
    public DBConnection(String dbURL, String user, String pw){
		mapsAPI = new GoogleMapsRequest("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(dbURL, user, pw);
	        
	        statement = connection.createStatement();
    	}
    	catch(SQLException ex){
    		System.out.println("Something went wrong with sql! Error message: " + ex);
    	} catch (ClassNotFoundException e) {
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
    
    public void insertLocation(String zipcode, String city, String state){
    	String sqlQ = "INSERT INTO locations \n"
				+ " (country, state, city, zipcode) "
    			+ " values ('USA', '" + state + "', '" + city + "', '" + zipcode + "')";
    	System.out.println("location q: " + sqlQ);
    	executeQuery(sqlQ, "Added location infor for " + zipcode);
    }
    
    public boolean rowExists(String tableName, String arg, String input){
    	String sqlQ = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE " + arg + " = '"+input+"')";
    	System.out.println("Checking if " + input + " already in table " + tableName);
    	return executeQuery(sqlQ);
    }
    
    public void insertUser(User newUser){
    	java.sql.Date birthday = null;
    	boolean validZipcode = true;
    	//boolean locationInDB = executeQuery("SELECT EXISTS(SELECT 1 FROM locations WHERE zipcode = "+zipcode+")");
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
    		String loc[] = mapsAPI.getGeolocation(zip);
    		if(loc[0] != null){
    			insertLocation(zip, loc[0], loc[1]);
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
    
    //very unsafe, but ok for now just for testing
    public User verifyUser(String username, String pass){
    	/*
    	if(!rowExists("users", "username", username)){
    		return false;
    	}
    	*/
    	String sqlQ = "Select * from users Where username='" + username + "' and password='" + pass + "'";
    	return getQueryResultSet(sqlQ);
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
    		String loc[] = mapsAPI.getGeolocation(zip);
    		if(loc[0] != null){
    			insertLocation(zip, loc[0], loc[1]);
    		}
    		else{
    			validZip = false;
    		}
    	}
    	if(validZip){
    		String sqlQ = "INSERT INTO restaurants \n"
					+ " SET name = '" + restaurant.name + "',\n"
					+ "  address = '" + restaurant.address + "', \n"
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
    public void insertReview(String comments, int restaurantId, int userId, double rating){
    	String sqlQ = "insert into reviews "
    				+ " (rating, up, down, comment, restaurant, user)"
    				+ " values ('" + rating + "', '0', '0', '" + comments +"', '" + restaurantId + "', '" + userId + "')";
    	executeQuery(sqlQ, "Review for: " + restaurantId + " by: " + userId + " added");
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
    	executeQuery("truncate " + tableName, "Cleared table " + tableName);
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
            	//List<User> personList = new ArrayList<>();
                while (res.next()) {
                    String name = res.getString("username");
                    String pw = res.getString("password");
                    System.out.println("Name: " + name + " password: " + pw);
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
    
    public User getQueryResultSet(String query){
		User usr = null;
    	try{
			ResultSet rs = statement.executeQuery(query);
			System.out.println("result of q: " + rs);
			if(rs.next()) {
				System.out.println(rs.getInt(1));
			    if(rs.getInt(1) > 0){
			    	String name = rs.getString("username");
			    	String pw = rs.getString("password");
			    	String birthdate = rs.getString("birthdate");
			    	String email = rs.getString("email");
			    	String loc = rs.getString("fk_location");
			    	int privilege = rs.getInt("privilege");
			    	usr = new User(name, pw, birthdate, email, loc, privilege);
			    }
			}
    	}
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
		return usr;
    }
    
}
