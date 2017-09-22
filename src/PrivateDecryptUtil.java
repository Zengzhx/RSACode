import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.lang.ArrayUtils;

import sun.misc.BASE64Decoder;

public class PrivateDecryptUtil {

	// 私钥
	// private static String privatekeystr =
	// "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANIOl0QfREidD1wx"
	// + "Jc4HIax7V8r/Y2H/zTsXK0erRTcfiD3un4/OtgpBQujiBbmUqIvmm81I/UfGSkJs"
	// + "AXtyU4P9xnHsR5hiqAmrPW4zp6IFjmNqGLBXKtv38ySccPnAluDCZyzGQesoCKZZ"
	// + "rMNdRcSXc5eJIj0bPVmZhPVcMxD/AgMBAAECgYEAhVaPvMVgMfvI4UutZL/wJy3Q"
	// + "h+JfsriPDUBiz8avVBNnB3EuxjmAIrhUgcHfwnLUHqJPKht385SyVMF8m2DyE4K/"
	// + "cvsIk8YhnIkkwglZr980xBiC7r/HGvqiY6L5K2FIuCEJHvwjUivGYnfj1yqYFKdE"
	// + "ygoiyNMWSNmCbPzmGmECQQD6fNjTYPaaLzf+ND1mp3iNSgocapGjGBG/ZelotDog"
	// + "ycEgGI2of7US1Ie4ub3SIsr8HqN8kAyB82IHxdrm5+dRAkEA1q35AX61oWyjA5FB"
	// + "AksZsT8OB5oGySgoNTuKDBWFBQ9MQxudCOInOJcQPu5phRxSRGCbRSlbDc78XUQc"
	// + "y2n/TwJADCtknWBomrUQk1kWYpZgVPzz4M2Mpc/VjOb2oA+hg1ZJ+7U1rKoHshKB"
	// + "RYhAoKTwF6+lYbfd47JfYOFL8UvzAQJAdrEVPPhbX3Z64lwKv0PaXp7oGNfV7J2h"
	// + "LV1Pw8KaGuQMXAYGWoT+/lC3ELQr7wZrjZaEAkcNKqNH8CDacrSWDQJBALm6OiSK"
	// + "476AQoDAgMTVC6G9BpjTfJ+RpptG5OpJNzL7xrnfhQxZcoBkjV08TdwCFZmd4ug5"
	// + "c4xJAyqdOYd/Thw=";

	private final static String privatekeystr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKXIowLxTnY41lBVJF9Eku2Qr4cu"
			+ "IhaeySRDGT5qPUSxJ6ZwbFRrg5bYa+jnberWJLTJ1ZtRupU6SV/KiMJPLEeW2XQYlrMi52QRWIHL"
			+ "z/x2uOWrDOMNU3IjOeGe/5iDsVJQWjLkvV/61hZ1dqfJCbV8LtmW9pZ8dcaAM7xzZGVXAgMBAAEC"
			+ "gYBnSsAOOE0bqcQWH0ZfOTmWXPyQbDT/BQXr2waidc0gvIQo1okYfDhrxwJNBczdcXlDTzIQ6PYo"
			+ "vj0zOtzEvFGocrGlBKPmPndiWu3IvJ+8UcwXPe75mCiNzLRa98L4AbvQDhOxC2B+jC+3efOd7h45"
			+ "mMJdOVEEqr91EEQ0WbH8IQJBAOccRJaslWW7R+R0uvjcPzHAwi0lrhKvJynNEnSya2LtTZ2vFkMG"
			+ "9P1u2+P6STS16eORyutA+tXbyerA5OumSLECQQC3o06gj19/LrNRKFdh15o55rJifEwTKR4SK2b5"
			+ "AvQvPjLRDTvsCics4uVEta3brchHmzhwkxUSjeASdaP2+RCHAkAPeVEOVZfbK+LwZPBv4S9KAYH+"
			+ "y9sP3TJeFiD6N0gBY6oiuKpg6oJUxatrZEKqiNOULRrNDZi94O1wDmJFdOjBAkAc+dq9QQI7ij3n"
			+ "L2tiV+fQ/RLmsWA7ocBJ86lC33FyRukzzVfILe3yijbM+bHeOPSiP97O/JZVoCdcIk3l+XTfAkEA"
			+ "4cTT65Wq2nwxOPpKJBu94Jy67lUmXlN1HoPxjRTazAM7rF06btdsTqYNQToxPsAcMkeDIVZcNNnJ" + "q602BEg2YA==";

	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** */
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData) throws Exception {
		byte[] keyBytes = Base64Utils.decode(privatekeystr);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > 128) {
				cache = cipher.doFinal(encryptedData, offSet, 128);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * 128;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	public static void main(String[] args) throws IOException, Exception {
		// String str =
		// "h+n2G2zQO9T+kb6H2UeX8pdl/IOtCI8GUizKF9FR5tk7kfb9rdf4TMl9LUgdkCclxX++IWZ8QYNgQMt7Ox3/ZhXZQcfVaE03I9XMrQKfScH/m6qF4I7+fpzMJUrYG+KFehSCzBsXDmkz6IIEzvI8kdtf7xKp3+uCwNoaUbLeQhKJ+FKNGHa/iAZCWFhcDbQt/5UNhA6TAJFMx/3S+wTac9uMr4ZrSpFzrVTXkFGos8T4TAsWycujxgc1sD0wcNpI/pS4T7PqZXPomKZ2RcQmCloW500w87Vfx665ja8RhUZrD1lQTySIfV28ZJqUpH7DgG/RMhCQUtsb6E9vEGTMB5iJTCiaGngm7/7xY6vK8imvgwfKOpKA2v3XpE3R/pTZua//azd/zXiAqOBUBsdQrw==";
		String str = "mbNRNt7ts1DzCNk7pF+9PntAuaMWetIvn"
				+ "XPfLx3FiTkVhe16lYxAXLcOVWfsHRd4R13Q7cpCtZgigR"
				+ "ESWP4tkNjEHq6Lhc8Sr/m1U1FWl5AgoyGp+eOtgc+Yth9dXcUhrH"
				+ "kKkzeDAjkTFEz5E4Z/VEtcaUPjqzYdKJ/BGo6zJcUNACSAQPXmUip4Hq"
				+ "NauKiOzUH7/OuhiWwDKcp218l6t9fAc8rk3Mls0yzxYILd5rGlGR6urHgRhR0"
				+ "j4QOS4AD79NiaYpozeZZoC9VBGmzlqOl3JdQlgmDWeoeWkfEihaRr8+nhIgj6bE"
				+ "ZPx3+wmyvHEQObhcpjJDyiiYPK/FyHip6bis2zt/JfN6zaSCX6nzGr4T"
				+ "hQ04rTpv+WAgIYhbjqaPwEUidclpmWJbV8nLS4BAL1WC6EiJUd4fsumQbQ"
				+ "dOnnAYTPVi7TLxbwO82fq+aAgOsMicLQUcAspHlpC3N1UuQ3QqiwZglFT2"
				+ "Vh5+IC3492NvKKLCEcHCEn1lR8CVa2oO/S3ejRBRZYUBh5BbrkaWAmf7aX0K"
				+ "2dB+rshK1ETLuAJW6imDBhOQd8caOccSmWSPjSTyCEP3/3b763a/E7Q56Cpd"
				+ "mr+Nv6BmrW/dc1pe17cCpyQWblwCY2J6UbY1GGE7u1v55zhWPMxHudqYDnphB"
				+ "DzwqniLFDOaVtYET3eoqhLvfObd0IAoZWYiXND2kFrJw48Lc/+SfCHw28eFWv"
				+ "ggLUpsq0qqc7wZvlxvdeKXUbd2qgbzQy2AA8/uVtWZOHqmSR5JJFiarSzN+czdE"
				+ "FHaZIZuxVIUfGmiPc2s9pmzIFbP9eukgH0REg3mZURN0X/sbcIvoKCbLuCKWeaTmW8Q==";
//		byte[] bb = new BASE64Decoder().decodeBuffer(str);
//		byte[] dd = decryptByPrivateKey(bb);
//		System.out.println(new String(dd));
		
		/*******************************/
		
		byte[] bt = RSAUtils.decrypt(str, privatekeystr);
		System.out.println(java.net.URLDecoder.decode(new String(bt,"UTF-8"), "UTF-8"));
		// String encodestr = new BASE64Encoder().encodeBuffer(str.getBytes());

		// String inStr = "已提交至\"热线\"系统";
		// // String test = new String(inStr.getBytes("GBK"),"UTF-8");
		// System.out.println(inStr);
	}

}
