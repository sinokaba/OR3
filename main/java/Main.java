package main.java;

public class Main{

	public static void main(String[] args) {
		//Just testing out the classes, and the database connection
		Admin imKing = new Admin("rs", "123");
		imKing.printInfo();
		//imKing.addAge(20);
		imKing.printInfo();
		imKing.getAccountCreationDate();
		Review review = new Review(Long.parseLong(String.valueOf(1)), 2.5);
		review.addComments("This restaurant sucks, they use rat meat. Not recommended. The staff is nice thou.");
		imKing.addReview(Long.parseLong(String.valueOf(1)), review);
		imKing.getReviews(Long.parseLong(String.valueOf(1)));
		review.updateReview(4, null);
		imKing.getReviews(Long.parseLong(String.valueOf(1)));
		review.updateReview(4, "good stuff.");
		imKing.getReviews(Long.parseLong(String.valueOf(1)));
		String additonal = "?profileSQL=true&autoReconnect=true&useSSL=false";
		DBConnection db = new DBConnection("jdbc:mysql://localhost:3306/or3?autoReconnect=true&useSSL=false", "root", "allanK0_ph");
        db.insertUser(imKing.name, imKing.password, imKing.birthdate);
        db.insertaRestaurant("UCONN Dairy Bar", "Storrs CT");
        db.insertReview("Is gucci mang.", 1, 1, 4.5);
        //db.printTableData("user");
		//db.clearTable("users");
		javafx.application.Application.launch(StartUI.class);
	}

}
