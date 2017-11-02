
import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

public class TestClient {
	User user = new User("rs10", "123", "1997-01-26", "rs@gmail.com", "06268", 1);
	GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
	DBConnection db = new DBConnection(mapsAPI);
	Restaurant restaurant = new Restaurant("Monkey Kazoo","911");
	Review review = new Review(5, "good i guess", user, restaurant);


	@Test
	public void clearTables(){
		System.out.println("locations: ");
		db.printTableData("locations");
		db.clearTable("locations");
		db.printTableData("locations");
		
		System.out.println("users: ");
		db.printTableData("users");
		db.clearTable("users");
		db.printTableData("users");
		
		System.out.println("reviews: ");
		db.printTableData("reviews");
		db.clearTable("reviews");
		db.printTableData("reviews");
		
		System.out.println("restaurants: ");
		db.printTableData("restaurants");
		db.clearTable("restaurants");
		db.printTableData("restaurants");
		
	}
	@Test
	public void TestUser(){
		assertEquals("123", user.getPassword());
		user.updatePassword("haha");
		assertEquals("haha", user.getPassword());
		assertEquals("rs10", user.getUsername());
		assertEquals(1, user.getPrivilege());
		assertEquals("rs@gmail.com", user.getEmail());
		user.updateEmail("somewhere@yahoo.com");
		assertEquals("somewhere@yahoo.com", user.getEmail());
		assertEquals("offline", user.getStatus());
		user.loggedIn();
		assertEquals("online", user.getStatus());
		user.loggedOff();
		assertEquals("offline", user.getStatus());
	}
	
	@Test
	public void TestDatabaseInsertUser() throws SQLException{
		db.insertUser(user);
		ResultSet userFromDB = db.getQueryResult("users", "username", user.getUsername(), "password", user.getPassword());
		assertEquals(user.getUsername(), userFromDB.getString("username"));
		assertEquals(user.getPassword(), userFromDB.getString("password"));
		assertEquals(user.getEmail(), userFromDB.getString("email"));
		assertEquals(user.getBday(), userFromDB.getString("birthdate"));
	}

	@Test
	public void TestRestaurant(){
		restaurant.setAddress("123 Nowhere Street", "06268");
		assertEquals("Monkey Kazoo", restaurant.getName());
		assertEquals("123 Nowhere Street", restaurant.getAddress());
		assertEquals("911", restaurant.getPhone());
		assertEquals(0, restaurant.getRating(), .1);
		restaurant.addRating(5);
		assertEquals(5.0, restaurant.getRating(), .1);
		restaurant.addRating(3);
		assertEquals(4.0, restaurant.getRating(), .1);
	}

	@Test
	public void TestDatabaseInsertRestaurant() throws SQLException{
		restaurant.setAddress("123 Nowhere Street", "06268");
		db.insertRestaurant(restaurant);
		ResultSet restaurantFromDB = db.getQueryResult("restaurants", "name", restaurant.getName(), "address", restaurant.getAddress());
		assertEquals(restaurant.getName(), restaurantFromDB.getString("name"));
		assertEquals(restaurant.getAddress(), restaurantFromDB.getString("address"));
		assertEquals(restaurant.getPhone(), restaurantFromDB.getString("phone"));
	}	
	
	@Test
	public void TestDatabaseInsertReview() throws SQLException{
		//User user2 = db.getUserFromDB(user.getPassword(), user.getUsername());
		//Restaurant restaurant2 = db.getRestaurantFromDB(restaurant.getName(), restaurant.getAddress());
		Review review2 = new Review(5, "good i guess", user, restaurant);
		//System.out.println("user id: " +review2.getUserId() + " vs. " + user2.getId() + " rst id: " + review2.getRestaurantId() + " vs. " + restaurant2.getId());
		db.insertReview(review2);
		ResultSet reviewFromDB = db.getQueryResultReviews(user, restaurant);
		System.out.print("comments? " + reviewFromDB.getString("comments"));
		assertEquals(review2.getComments(), reviewFromDB.getString("comments"));
		assertEquals(review2.getRating(), reviewFromDB.getDouble("overall_rating"), .1);
	}
	
	@Test
	public void TestReview(){
		review.updateReview(-1, "It's alright.");
		assertEquals("It's alright.", review.getComments());
		assertEquals(5, review.getRating(), .1);
		review.updateReview(1, "Pitiful service, bad food.");
		assertEquals(1, review.getRating(), .1);
		assertEquals("Pitiful service, bad food.", review.getComments());
	}
}
