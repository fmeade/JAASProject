package JAASMaster;
import java.security.*;
import java.math.*;

public class MD5Hash {
	
	public MD5Hash() throws NoSuchAlgorithmException {
		
	}
	
	public String hash(String str) throws NoSuchAlgorithmException {
		
		MessageDigest message = MessageDigest.getInstance("MD5");
		message.reset();
		message.update(str.getBytes());
		
		byte[] digest = message.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		
		while(hashtext.length() < 32){
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}
}