package main.java;

public class Main{

	public static void main(String[] args) {
		Admin imKing = new Admin("rs", "123");
		imKing.printInfo();
		imKing.addAge(20);
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

        javafx.application.Application.launch(StartUI.class);
	}

}
