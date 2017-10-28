
import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

public class TestClient {
	User user = new User("rs10", "123", "1997-01-26", "rs@gmail.com", "06268", 1);
	GoogleMapsService mapsAPI = new GoogleMapsService("AIzaSyCP-qr7umfKFSrmnbOB-cl-djIhD5p1mJ8");
	DBConnection db = new DBConnection("jdbc:mysql://localhost:3306/2102_or3?autoReconnect=true&useSSL=false", "root", "allanK0_ph", mapsAPI);
	Restaurant restaurant = new Restaurant("Monkey Kazoo","911");
	Review review = new Review(5);

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
		ResultSet userFromDB = db.getQueryResult(user.getUsername(), user.getPassword());
		assertEquals(user.getUsername(), userFromDB.getString("username"));
		assertEquals(user.getPassword(), userFromDB.getString("password"));
		assertEquals(user.getEmail(), userFromDB.getString("email"));
		assertEquals(user.getBday(), userFromDB.getString("birthdate"));
	}

	@Test
	public void TestRestaurant(){
		restaurant.addAddress("123 Nowhere Street", "06268");
		assertEquals("Monkey Kazoo", restaurant.getName());
		assertEquals("123 Nowhere Street", restaurant.getAddress());
		assertEquals("911", restaurant.getPhone());
		assertEquals(0, restaurant.getRating(), .1);
		restaurant.addRating(5);
		assertEquals(5.0, restaurant.getRating(), .1);
		restaurant.addRating(3);
		assertEquals(4.0, restaurant.getRating(), .1);
		review.addComments("It's pretty good, yeh?");
		restaurant.addReview(user, review);
		assertEquals(review, restaurant.getUserReview(user));
	}

	@Test
	public void TestDatabaseInsertRestaurant() throws SQLException{
		restaurant.addAddress("123 Nowhere Street", "06268");
		db.insertRestaurant(restaurant);
		ResultSet restaurantFromDB = db.getQueryResultRst(restaurant.getName(), restaurant.getAddress());
		assertEquals(restaurant.getName(), restaurantFromDB.getString("name"));
		assertEquals(restaurant.getAddress(), restaurantFromDB.getString("address"));
		assertEquals(restaurant.getPhone(), restaurantFromDB.getString("phone"));
	}	
	
	@Test
	public void TestReview(){
		review.addComments("It's alright.");
		assertEquals("It's alright.", review.getComments());
		assertEquals(5, review.getRating(), .1);
		review.updateReview(1, "Pitiful service, bad food.");
		assertEquals(1, review.getRating(), .1);
		assertEquals("Pitiful service, bad food.", review.getComments());
	}
}
