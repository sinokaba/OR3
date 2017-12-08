
public class Admin extends User{
	/**
	* The Admin class constructor, a child of the User class
	* 
	* @param takes 2 paramaeters, both of the type string, which are the name and password of the user
	* @return no return value
	*/
	public Admin(String name, String pw, String birthdate, String email){
		super(name, pw, birthdate, email);
	}
	
	@Override
	public int getPrivilege(){
		return 1;
	}

}
