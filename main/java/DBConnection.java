package main.java;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DBConnection {
    private Connection connection;	
    private Statement statements;

	/**
	* The constructor of the DBConnection class
	* Will connect to the database based on input given, or raise exceptions if something goes wrong
	* 
	* @param takes 3 input, all string, the database url, and the user and password associated with the db
	* @return no return value
	*/
    public DBConnection(String dbURL, String user, String pw){
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(dbURL, user, pw);
	        statements = connection.createStatement();
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
    public void insertUser(String name, String pw, long birthdate){
		String sqlQ = "insert into users "
					+ " (username, birthdate, password)"
					+ " values ('" + name + "', '" + birthdate + "', '" + pw +"')";
		executeQuery(sqlQ, "User: " + name + " added");
    }
   
	/**
	* This method creates an sql query for inserting a new restaurant in the db
	* 	 
	* @param takes 2 input arguments, the name of the restaurant, and their address
	* More fields will soon be added, just testing right now
	* @return no return value
	*/    
    public void insertaRestaurant(String name, String address){
		String sqlQ = "insert into restaurants "
				+ " (name, address, rating)"
				+ " values ('" + name + "', '" + address + "', '" + 3 +"')";
		executeQuery(sqlQ, "Restaurant: " + name + " added");    	
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
        		ResultSet res = statements.executeQuery("select * from " + tableName);
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
    		statements.execute(query);
    		System.out.println(queryMessage + " successfully.");
    	}
    	catch(SQLException ex){
    		System.out.println("SQl exception erro: " + ex);
    	}      	
    }
    
}
