

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PrivateEncrptUtil {
	// 私钥
	private static String privatekeystr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANIOl0QfREidD1wx"
			+ "Jc4HIax7V8r/Y2H/zTsXK0erRTcfiD3un4/OtgpBQujiBbmUqIvmm81I/UfGSkJs"
			+ "AXtyU4P9xnHsR5hiqAmrPW4zp6IFjmNqGLBXKtv38ySccPnAluDCZyzGQesoCKZZ"
			+ "rMNdRcSXc5eJIj0bPVmZhPVcMxD/AgMBAAECgYEAhVaPvMVgMfvI4UutZL/wJy3Q"
			+ "h+JfsriPDUBiz8avVBNnB3EuxjmAIrhUgcHfwnLUHqJPKht385SyVMF8m2DyE4K/"
			+ "cvsIk8YhnIkkwglZr980xBiC7r/HGvqiY6L5K2FIuCEJHvwjUivGYnfj1yqYFKdE"
			+ "ygoiyNMWSNmCbPzmGmECQQD6fNjTYPaaLzf+ND1mp3iNSgocapGjGBG/ZelotDog"
			+ "ycEgGI2of7US1Ie4ub3SIsr8HqN8kAyB82IHxdrm5+dRAkEA1q35AX61oWyjA5FB"
			+ "AksZsT8OB5oGySgoNTuKDBWFBQ9MQxudCOInOJcQPu5phRxSRGCbRSlbDc78XUQc"
			+ "y2n/TwJADCtknWBomrUQk1kWYpZgVPzz4M2Mpc/VjOb2oA+hg1ZJ+7U1rKoHshKB"
			+ "RYhAoKTwF6+lYbfd47JfYOFL8UvzAQJAdrEVPPhbX3Z64lwKv0PaXp7oGNfV7J2h"
			+ "LV1Pw8KaGuQMXAYGWoT+/lC3ELQr7wZrjZaEAkcNKqNH8CDacrSWDQJBALm6OiSK"
			+ "476AQoDAgMTVC6G9BpjTfJ+RpptG5OpJNzL7xrnfhQxZcoBkjV08TdwCFZmd4ug5"
			+ "c4xJAyqdOYd/Thw=";
	// 盐
	private static String ivData = "K/li2pT9WmlYvPdIML8ZIg==";

	private static byte[] keyData = null;

	/**
	 * AES256加密
	 */
	public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes,
			byte[] textBytes) throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		return cipher.doFinal(textBytes);
	}

	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA加密
	 * 
	 * @throws IOException
	 */
	public static byte[] RSAEncrypt(byte[] content) throws IOException {
		byte[] keyBytes = new BASE64Decoder().decodeBuffer(privatekeystr);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e3) {
			e3.printStackTrace();
		}
		PrivateKey key = null;
		try {
			key = keyFactory.generatePrivate(spec);
		} catch (InvalidKeySpecException e2) {
			e2.printStackTrace();
		}
		byte[] encryptedText = null;
		try {
			// encrypt the message
			Cipher cipher = null;

			cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.ENCRYPT_MODE, key);

		//	encryptedText = cipher.doFinal(content);
			
			int inputLen = content.length;  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        int offSet = 0;  
	        byte[] cache;  
	        int i = 0;  
	        // 对数据分段加密  
	        while (inputLen - offSet > 0) {  
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
	                cache = cipher.doFinal(content, offSet, MAX_ENCRYPT_BLOCK);  
	            } else {  
	                cache = cipher.doFinal(content, offSet, inputLen - offSet);  
	            }  
	            out.write(cache, 0, cache.length);  
	            i++;  
	            offSet = i * MAX_ENCRYPT_BLOCK;  
	        }  
	        encryptedText = out.toByteArray();  
	        out.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedText;
	}

	/**
	 * AES加密
	 */
	static byte[] AESenc(String content) {
		byte[] ret = null;
		try {
			ret = encrypt(new BASE64Decoder().decodeBuffer(ivData), keyData,
					content.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 整合之后的加密签名
	 * 
	 * @throws IOException
	 */
	public static String Encrypt(String Content) throws IOException {

		byte[] enckeybytes = RSAEncrypt(keyData);

		byte[] ciphertext = AESenc(Content);

		byte[] nCon = new byte[ciphertext.length + enckeybytes.length];
		System.arraycopy(enckeybytes, 0, nCon, 0, enckeybytes.length);
		System.arraycopy(ciphertext, 0, nCon, enckeybytes.length,
				ciphertext.length);

		byte[] signbytes = RSASign(nCon);

		byte[] nCon1 = new byte[signbytes.length + nCon.length];
		System.arraycopy(signbytes, 0, nCon1, 0, signbytes.length);
		System.arraycopy(nCon, 0, nCon1, signbytes.length, nCon.length);

		return new BASE64Encoder().encode(nCon1);
	}

	/**
	 * SHA1withRSA文件签名
	 * 
	 * @throws IOException
	 */
	public static byte[] RSASign(byte[] content) throws IOException {
		byte[] keyBytes = new BASE64Decoder().decodeBuffer(privatekeystr);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e3) {
			e3.printStackTrace();
		}

		PrivateKey key = null;
		try {
			key = keyFactory.generatePrivate(spec);
		} catch (InvalidKeySpecException e2) {
			e2.printStackTrace();
		}

		Signature instance = null;
		byte[] signed = null;
		try {
			instance = Signature.getInstance("SHA1withRSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try {
			instance.initSign(key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		try {
			instance.update(content);
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		try {
			signed = instance.sign();
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		return signed;
	}

	public static String doEncode(String content) throws UnsupportedEncodingException {
		String str = "abcdefghijklmnop";
		String tmp = String.valueOf((int) (Math.random() * 10000))
				+ String.valueOf((int) (Math.random() * 10000));
		str = tmp + str.substring(tmp.length());
		keyData = str.getBytes("UTF-8");
		try {
			return Encrypt(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) throws IOException {
		String data = "需要加密数据";
		System.out.println("加密前："+data);
		String str = PrivateEncrptUtil.doEncode(data);
		System.out.println("加密后："+str);
		System.out.println("解密后:"+PublicDecryptUtil.decrypt(str));
	}
}
