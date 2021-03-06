import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordEncryption {

	public String getSecurePassword(String password){
		String securePassword = password;
		byte[] salt = getSalt();
		securePassword = generateHash(password, salt);
		System.out.println(verifyPassword(securePassword, password, salt));
		return securePassword;
	}
	
	public String getSecurePassword(String password, byte[] salt){
		return generateHash(password, salt);
	}
	
	public boolean verifyPassword(String hashedPassword, String password, byte[] salt){
		String generatedPassword = generateHash(password, salt);
		return hashedPassword.equals(generatedPassword);
	}
	
	private static String generateHash(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     
    //Add salt
    public static byte[] getSalt(){
    	byte[] salt = null;
    	try{
	        //Always use a SecureRandom generator
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
	        //Create array for salt
        	salt = new byte[16];
	        //Get a random salt
	        sr.nextBytes(salt);
	        //return salt
    	}
    	catch(NoSuchAlgorithmException | NoSuchProviderException ex){
    		System.out.println("Unable to generate salt!");
    		ex.printStackTrace();
    	}
        return salt;
    }
    
    public String tempPasswordGenerator(){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder tempPassword = new StringBuilder();
        Random rnd = new Random();
        while (tempPassword.length() < 12) { // length of the random string.
            int index = rnd.nextInt(chars.length());
            tempPassword.append(chars.charAt(index));
        }
        return tempPassword.toString();
    }
}
