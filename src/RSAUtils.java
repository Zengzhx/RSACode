import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import org.bouncycastle.util.encoders.Base64;

public class RSAUtils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117; //RSA最大加密明文大小
    private static final int MAX_DECRYPT_BLOCK = 128; //RSA最大解密密文大小

    public static byte[] encrypt(String src, String key) throws Exception {
        byte[] keyBytes = Base64.decode(key.getBytes());
        PublicKey publicKey = getPublicKey(keyBytes);

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int inputLen = src.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(src.getBytes(), offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(src.getBytes(), offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] decrypt(String src, String key) throws Exception {
//		byte[] basesrc = Base64.decode(key.getBytes());
		byte[] basesrc = Base64.decode(key.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(basesrc);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey =  keyFactory.generatePrivate(keySpec);
//        PrivateKey privateKey = getPrivateKey(keyBytes);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] srcBytes = base5.encode(src.getBytes());
//        byte[] srcBytes =Base64Utils.decode(src);
        byte[] srcBytes = Base64.decode(src.getBytes());
        int inputLen = srcBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(srcBytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    	
    }

    /**
     * 获取公钥
     *
     * @param keyBytes 公钥的byte[]
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     * @param keyBytes 私钥的byte[]
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
}
