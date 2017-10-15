package main.java;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DBConnection {
	//String dbURL = "mysql://localhost:3306/or3?profileSQL=true";
    private Connection connection;	
    private Statement statements;
    
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
    
    public void insertUser(String name, String pw, long birthdate, long creationDate){

		String sqlQ = "insert into user "
					+ " (username, creation_date, birthdate, password)"
					+ " values ('" + name + "', '" + creationDate + "', '" + birthdate + "', '" + pw +"')";
		executeQuery(sqlQ, "User: " + name + " added");
    }
    
    public void removeTable(String tableName){
    	executeQuery("drop table if exists " + tableName, "Dropped table " + tableName);
    }
    
    public void clearTable(String tableName){
    	executeQuery("truncate " + tableName, "Cleared table " + tableName);

    }
    
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
