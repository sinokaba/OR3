package main.java;

public class Restaurant {
	String name, phone, address, city, state, zip;
	double rating = 0;
	int ratingCount = 0;
	
	public Restaurant(String name, String phone){
		this.name = name;
		this.phone = phone;
	}
	
	public void addAddress(String address, String zip){
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public void addRating(double rating){
		this.rating = rating;
		ratingCount += 1;
	}
}
