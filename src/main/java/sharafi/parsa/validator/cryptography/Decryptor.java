package sharafi.parsa.validator.cryptography;

import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class Decryptor {
	
	private static SecretKey key = new SecretKeySpec(Arrays.copyOf(Base64.getDecoder().decode("CryptographicKey"), 16), "AES");
	private static Cipher cipher;
	static { try { cipher = Cipher.getInstance("AES"); cipher.init(Cipher.DECRYPT_MODE, key); } catch (Exception e) {e.printStackTrace();} }
	
	public String decrypt(String input) {
		try {
			byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(input));
			return new String(cipherText);
		} catch (Exception e) { throw new RuntimeException("Error occured while decrypting data", e); }
	}
}