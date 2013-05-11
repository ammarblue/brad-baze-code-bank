package reuze.test;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.ce_CipherRot13;


public class CipherRot13Test extends TestCase {

	@Test
	public void testEncrypt() {
        	Assert.assertEquals(Encrypt("rotate by 13 places"), "ebgngr ol 13 cynprf");
	}

	@Test
	public void testDecrypt() {
        	Assert.assertEquals(Decrypt("Ebgngr ol 13 Cynprf"), "Rotate by 13 Places");
	}
	
	private static String Decrypt(String toDecrypt) {
		ce_CipherRot13 encryption = new ce_CipherRot13();
		byte[] input = toDecrypt.getBytes();
		for (int i = 0; i<input.length;i++){
			input[i] = encryption.decrypt(input[i]);
		}
		return new String(input);
	}

	private static String Encrypt(String toEncrypt) {
		ce_CipherRot13 encryption = new ce_CipherRot13();
		byte[] input = toEncrypt.getBytes();
		for (int i = 0; i<input.length;i++){
			input[i] = encryption.encrypt(input[i]);
		}
		return new String(input);
	}	
}
