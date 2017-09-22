

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

//解密
public class PublicDecryptUtil {
	public static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSDpdEH0RInQ9cMSXOByGse1fK"
			+ "/2Nh/807FytHq0U3H4g97p+PzrYKQULo4gW5lKiL5pvNSP1HxkpCbAF7clOD/cZx"
			+ "7EeYYqgJqz1uM6eiBY5jahiwVyrb9/MknHD5wJbgwmcsxkHrKAimWazDXUXEl3OX"
			+ "iSI9Gz1ZmYT1XDMQ/wIDAQAB";

	private static String ivData = "K/li2pT9WmlYvPdIML8ZIg==";

	public static String decrypt(String encContent) throws IOException {
		byte[] encbytes = new BASE64Decoder().decodeBuffer(encContent);

		int lenth = encbytes.length;

		byte[] signbytes = new byte[128];
		System.arraycopy(encbytes, 0, signbytes, 0, 128);

		byte[] enckeybytes = new byte[128];
		System.arraycopy(encbytes, 128, enckeybytes, 0, 128);

		int cipherlen = lenth - 128 - 128;
		byte[] ciphertext = new byte[cipherlen];
		System.arraycopy(encbytes, 256, ciphertext, 0, cipherlen);

		byte[] nCon = new byte[ciphertext.length + enckeybytes.length];
		System.arraycopy(enckeybytes, 0, nCon, 0, enckeybytes.length);
		System.arraycopy(ciphertext, 0, nCon, enckeybytes.length,
				ciphertext.length);

		if (doRSAVerifySign(nCon, signbytes)) {
			byte[] randkeybytes = doRSADecrypt(enckeybytes);
			byte[] plaintbytes = doAESdec(ciphertext, randkeybytes,
					new BASE64Decoder().decodeBuffer(ivData));

			try {
				return new String(plaintbytes, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes,
			byte[] textBytes) throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return cipher.doFinal(textBytes);
	}

	public static byte[] doAESdec(byte[] content, byte[] key, byte[] ivDateKey) {
		byte[] ret = null;
		try {
			ret = decrypt(ivDateKey, key, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean doRSAVerifySign(byte[] content, byte[] signature)
			throws IOException {
		byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKeyStr);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e3) {
			e3.printStackTrace();
		}

		PublicKey key = null;
		try {
			key = keyFactory.generatePublic(spec);
		} catch (InvalidKeySpecException e2) {
			e2.printStackTrace();
		}

		Signature sg = null;
		try {
			sg = Signature.getInstance("SHA1withRSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try {
			sg.initVerify(key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		try {
			sg.update(content);
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		boolean ok = false;
		try {
			// 对签名后的文件进行验证
			ok = sg.verify(signature);
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		return ok;
	}

	/**
	 * RSA解密
	 * 
	 * @throws IOException
	 */
	public static byte[] doRSADecrypt(byte[] content) throws IOException {
		byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKeyStr);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e3) {
			e3.printStackTrace();
		}
		PublicKey key = null;
		try {
			key = keyFactory.generatePublic(spec);
		} catch (InvalidKeySpecException e2) {
			e2.printStackTrace();
		}
		byte[] dectyptedText = null;
		try {
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				e1.printStackTrace();
			}
			try {
				cipher.init(Cipher.DECRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}

			dectyptedText = cipher.doFinal(content);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return dectyptedText;
	}

	/*public static void main(String[] args) throws IOException {
		String encContent = "kHnllpcV6qaE/NIUJCi7q2Vt3y6kLJDBPdLiidZnNPkOxsIqxXQXTEebFHfCkrHbLWC6eNuoZ9kg"
				+ "7GUfc+mwANeQnLtkya50EvsUBhppAlYX2QAd0roviLievTwZZLgHaSPTplV4zMMMrmfjhZ/MyGLV"
				+ "Mxaj5SMa8p5ua+O8P0jNbwRWedFForKArc7wayYPs4253tY3/Y85pwpnyk1K2GL7H+2NPF1COJrw"
				+ "VtYn7rSn86ENQW4jrbCqjiptthZL6Tq2ehSQ9xONn5SjAfQbVZxZzLPlr6CHW+z8auV3ycjPFKY9"
				+ "SpxVuPoYPqTulzM8PUjfc0W4b07U71hqgcND7u07nzvR9acEtQGFcyoHUHQ=";
		String encContent = "Jl72DbEy0Cxa8bdTeXZ3MiATZ9L+2AlwfjVLkAM2L8ON7aeUOmqN5OcACnHy"
				+ "K0yYRMvBq3pWd1hLyxKjWiTL9QbCim5EtJW+RumjejdWELo3pyw7yZEh9XQL"
				+ "SM6XNKDDHhGD9A8vWQlONk1XuLXIdqwMTwUDI1cd+PY+sey32ZSou9yrBFEg"
				+ "T+fIcxBq0UWhQzLEh60FAsT4fpwPXybns0Lfog+eekQoM0d+dQjkc28TR3NC"
				+ "3XcM0vTKztCL16roCmDGf6Uv9CnOY4AdsPWg1HlgS8zFaFo2FzpaNgufmvpN"
				+ "F8/cGu4SPBvI/FOVkDyh+njrw3cZWBsRNpry4o/xCJqlZ9a+50OekEaK2i99"
				+ "D2gbiZ0C4UBY8ELTnHWOsYzUu9i60Xdzx+XkvSITUkD1qOYT2o8Lws/QBgAd"
				+ "dvp7Mlj7e4FZV6ASCNEXYNV/ADnRH+EwoWHw1ND4NQnrhCegRGw/j47D6OT+"
				+ "SvgP0R1ZIc/vvrtAWmEjQWIslh7GdFYqpYDCFxYAPS5OvJ6Vw/hr+AUKbGr0"
				+ "Y0Xc1c30FUL5FN8+moB4pnXnYNjogkhEbjiDyT0wO09XNMF8oevhImeoZ/QR"
				+ "9dH1TU9o19KxO0jQWx+MCxrAnk73ViDnABbckeMl9HfJ6IY/oicaVAt5Gu7k"
				+ "4l45hYJQQkw+CFaIjCe3y090EW7ldn500qlHxkGmP/DtSthsZlXdLo8ND3WQ"
				+ "uP1cFQ==";
		PublicDecryptUtil service = new PublicDecryptUtil();
		String result = service.decrypt(encContent);
		System.out.println(result);
	}*/
}
