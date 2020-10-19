package com.tibcopsg.tibcobw.customfunctions.bw;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.tibco.xml.cxf.common.annotations.*;

import java.util.Base64;

@XPathFunctionGroup(category = "Custom Functions Crypt", prefix = "cf_crypt", namespace = "http://com.tibcopsg.tibcobw.customfunctions.crypt", helpText = "Custom defined function for crypting")
public class FCrypt {

	private static MessageDigest md;
	private static IvParameterSpec iv;
	private static final byte[] IVValue = new byte[] { 'v', 'V', 'Q', 'n', 'F',
			'5', 'n', 's', '4', 'X', 'f', 'v', 'g', 'd', 'Q', 'Z' };

	static {
		try {
			md = MessageDigest.getInstance("MD5");
			iv = new IvParameterSpec(IVValue);
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	public static final String HELP_STRINGS[][] = {
			{ "getMD5",
					"Calculate MD5 hash from string and return 32-byte hexadecimal representation.",
					"Example",
					"getMD5(\"TIBCO Software\"; \"UTF-8\") returns e3f2b373ca278cef3b25d80b178b8238" },
			{ "decryptAES", 
					"Decrypt the input string using AES algorythm",
					"Example",
					"decryptAES(\"VdRsS+fjBWcKDt3sisIo9A==\") returns TIBCO Software" },
			{ "encryptAES", 
					"Encrypt the input string using AES algorythm",
					"Example",
					"encryptAES(\"TIBCO Software\") returns VdRsS+fjBWcKDt3sisIo9A==" } };

	@XPathFunction(helpText = "", parameters = { @XPathFunctionParameter(name = "data", optional = false, optionalValue = "") })
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	@XPathFunction(helpText = "", parameters = {
			@XPathFunctionParameter(name = "text", optional = false, optionalValue = ""),
			@XPathFunctionParameter(name = "enc", optional = false, optionalValue = "") })
	public static String getMD5(String text, String enc) {
		try {
			synchronized(md) {
				byte[] md5hash = new byte[32];
				md.update(text.getBytes(enc), 0, text.length());
				md5hash = md.digest();
				return convertToHex(md5hash);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	@XPathFunction(helpText = "", parameters = { @XPathFunctionParameter(name = "key", optional = false, optionalValue = "") })
	private static Key getsecretKey(String key) {
		byte[] keyInBytes = key.getBytes();
		return new SecretKeySpec(keyInBytes, "AES");
	}
	
	@XPathFunction(helpText = "", parameters = {
			@XPathFunctionParameter(name = "encryptedValue", optional = false, optionalValue = ""),
			@XPathFunctionParameter(name = "key", optional = false, optionalValue = "") })
	public static String decryptAES(String encryptedValue, String key) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, getsecretKey(key), iv);
			byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
			byte[] decValue = c.doFinal(decodedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	@XPathFunction(helpText = "", parameters = {
			@XPathFunctionParameter(name = "valueToEnc", optional = false, optionalValue = ""),
			@XPathFunctionParameter(name = "key", optional = false, optionalValue = "") })
	public static String encryptAES(String valueToEnc, String key) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, getsecretKey(key), iv);
			byte[] encValue = c.doFinal(valueToEnc.getBytes());
			String encryptedValue = Base64.getEncoder().encodeToString(encValue);
			return encryptedValue;
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("getMD5(\"TIBCO Software\", \"UTF-8\") = "
				+ FCrypt.getMD5("TIBCO Software", "UTF-8"));
		String password = "Is8OMB45L4Y=gd0S)Y4BAY";
		String passwordEnc = FCrypt.encryptAES(password, "AjQnF5ns4XlvgwQZ");
		String passwordDec = FCrypt.decryptAES(passwordEnc, "AjQnF5ns4XlvgwQZ");

		System.out.println("Plain Text : " + password);
		System.out.println("Encrypted : " + passwordEnc);
		System.out.println("Decrypted : " + passwordDec);

	}

}
