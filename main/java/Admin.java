package main.java;

public class Admin extends User{
	/**
	* The Admin class constructor, a child of the User class
	* 
	* @param takes 2 paramaeters, both of the type string, which are the name and password of the user
	* @return no return value
	*/
	public Admin(String name, String pw){
		super(name, pw, "01-26-1997", true);
	}

}