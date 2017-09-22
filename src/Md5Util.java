

import java.security.MessageDigest;

public class Md5Util {
	public static String encrypt(String originData) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encodeBuffer(messageDigest.digest(originData
					.getBytes()));
		} catch (Exception e) {
			return null;
		}
	}
}
