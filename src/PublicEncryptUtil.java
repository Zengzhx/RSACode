

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.lang.ArrayUtils;

import sun.misc.BASE64Decoder;

public class PublicEncryptUtil {
	
//	public static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSDpdEH0RInQ9cMSXOByGse1fK"
//			+ "/2Nh/807FytHq0U3H4g97p+PzrYKQULo4gW5lKiL5pvNSP1HxkpCbAF7clOD/cZx"
//			+ "7EeYYqgJqz1uM6eiBY5jahiwVyrb9/MknHD5wJbgwmcsxkHrKAimWazDXUXEl3OX"
//			+ "iSI9Gz1ZmYT1XDMQ/wIDAQAB";
	
	
	public static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClyKMC8U52ONZQVSRfRJLtkK+HLiIWnskkQxk+"
			+ "aj1EsSemcGxUa4OW2Gvo523q1iS0ydWbUbqVOklfyojCTyxHltl0GJazIudkEViBy8/8drjlqwzj"
			+ "DVNyIznhnv+Yg7FSUFoy5L1f+tYWdXanyQm1fC7ZlvaWfHXGgDO8c2RlVwIDAQAB";
	
    /** *//** 
     * 加密算法RSA 
     */  
    public static final String KEY_ALGORITHM = "RSA";  
      
    /** *//** 
     * 签名算法 
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /** *//** 
     * 获取公钥的key 
     */  
    private static final String PUBLIC_KEY = "RSAPublicKey";  
      
    /** *//** 
     * 获取私钥的key 
     */  
    private static final String PRIVATE_KEY = "RSAPrivateKey";  
      
    /** *//** 
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /** *//** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;  
    
	/** 
     * 加密<br> 
     * 用公钥加密 
     *  
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(String content)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(publicKeyStr);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
//        Key publicK = keyFactory.generatePublic(x509KeySpec);  //生成密钥对象
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);  //生成密钥对象
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        
        
        int inputLen = content.getBytes().length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(content.getBytes(), offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(content.getBytes(), offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }
    
    public static void main(String[] args) throws Exception {
    	PublicEncryptUtil service = new PublicEncryptUtil();
    	String content = "{\"osPlatform\":\"android\",\"key\":\"37416643affb3520d5a2a136\",\"companyCode\":\"uusafe\",\"secret\":\"3f1e7b8ab2b2338eebf4b3db\"}";
//    	String content = "{"securityId":"864167037631241","appInfo":[{"name":"QQ邮箱","app_sort":"Y","app_version_code":"10126475","app_pkg_name":"com.tencent.androidqqmail","app_version_name":"5.3.3"},{"name":"网易邮箱大师","app_sort":"Y","app_version_code":"124","app_pkg_name":"com.netease.mail","app_version_name":"5.7.2"},{"name":"百度浏览器","app_sort":"Y","app_version_code":"231","app_pkg_name":"com.baidu.browser.apps","app_version_name":"7.14.14.0"},{"name":"人脉通","app_sort":"N","app_version_code":"169","app_pkg_name":"com.bizsocialnet","app_version_name":"3.0.6.0"},{"name":"UC浏览器","app_sort":"Y","app_version_code":"701","app_pkg_name":"com.UCMobile","app_version_name":"11.6.4.950"},{"name":"139邮箱","app_sort":"Y","app_version_code":"201702040","app_pkg_name":"cn.cj.pe","app_version_name":"7.2.1"},{"name":"安全浏览器","app_sort":"Y","app_version_code":"1","app_pkg_name":"com.uusafe.secbrowser","app_version_name":"1.0"}],"token":"23fc5023216847e88bfdec5517236fcf","companyCode":"uusafe"}";
    	byte[] bt=RSAUtils.encrypt(content, publicKeyStr);
//    	String uniContent = java.net.URLEncoder.encode(content);
//    	System.out.println(uniContent);
//    	byte[] result = service.encryptByPublicKey(uniContent);
//    	System.out.println(new BASE64Encoder().encodeBuffer(result));
    	String str = new BASE64Encoder().encodeBuffer(bt);
    	System.out.println(str);
//    	byte[] dd = PrivateDecryptUtil.decryptByPrivateKey(new BASE64Decoder().decodeBuffer(str));
//		System.out.println(java.net.URLDecoder.decode(new String(dd)));
    }
}
