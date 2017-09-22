import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;


 
public class CreatAllowFile {

    public static void main(String[] args) {

        // 版本信息：
        //String versionInfo = "C存1.0版";
        // 客户名称：
        String customerName = "北京成众志";
        // 系统部署地址：
        String deployAddress = "北京成众志科技有限公司";
        // 电脑Mac地址：
//        String macAddress = "00:0c:29:e8:cb:c4";
        String macAddress = "00:24:ec:f0:39:96";
        //String macAddress = "a0:1d:48:fc:7b:d5";
        // 电脑IP地址：
        	String ipAddress = "10.10.10.100";
//        String ipAddress = "192.168.1.250";
        // 合同编号：
        String contractNum = "11095687421354402";
        // 合同名称：
        String contractName = "图像采集存储合同";
        // 合同描述：
        String contractDescribe = "基于量子加密技术的存储系统";
        // 产品有限期限：
        String endDate = "2017-10-30";
        // 产品序列号：
        //String serialNumber = "01fa-5566-123e-tt56-09ht";
        String serialNumber = "0122-5511-123e-tt56-0233";
        // 将参数信息转换成JSONObject
        JSONObject object = new JSONObject();
        //object.put("versionInfo", versionInfo);        
        object.put("customerName", customerName);
        object.put("deployAddress", deployAddress);
        object.put("macAddress", macAddress);
        object.put("ipAddress", ipAddress);
        object.put("contractNum", contractNum);
        object.put("contractName", contractName);
        object.put("contractDescribe", contractDescribe);
        object.put("endDate", endDate);
        object.put("serialNumber", serialNumber);
        System.out.println("加密前的文件内容: " + object.toString());
        try {
            // 非对称加密算法加密
			// System.out.println(object.toString().getBytes().length);
			// System.out.println(object.toString().getBytes());
            byte[] content = PublicEncryptUtil.encryptByPublicKey(object.toString());
            // 将加密数据转换成BASE64编码格式
            String results =  new BASE64Encoder().encodeBuffer(content);
			// System.out.println("11加密前的文件内容: " + results);
            // 生成空白的文件
            File f = new File("E:\\cstorage.license");  
            if (f.exists()) {  
                //System.out.println("文件存在");  
            } else {  
                System.out.println("文件不存在");  
                // 不存在则创建
                f.createNewFile();  
            } 
            // 将加密信息写到txt文件中
            FileOutputStream o;
            try {  
                o = new FileOutputStream("E:\\cstorage.license");  
                o.write(results.getBytes("UTF-8"));  
                o.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
            
            
            
            // 读取txt中的文件,并且进行解密
            String result = "";  
            FileReader fileReader = null;  
            BufferedReader bufferedReader = null;  
            try{  
                fileReader = new FileReader("E:\\cstorage.license");  
                bufferedReader = new BufferedReader(fileReader);  
                try{  
                    String read = null;  
                    while((read = bufferedReader.readLine()) != null){  
                        result = result + read + "\r\n";  
                    }  
                }catch(Exception e){  
                    e.printStackTrace();  
                }  
            }catch(Exception e){  
                e.printStackTrace();  
            }finally{  
                if(bufferedReader!=null){  
                    bufferedReader.close();  
                }  
                if(fileReader!=null){  
                    fileReader.close();  
                }  
            }  
            System.out.println("加密后的文件内容: " + result);  
            
            // 进行解密
            byte[] bb = null;
            byte[] array = null;
            try {
                bb = new BASE64Decoder().decodeBuffer(result);
                array = PrivateDecryptUtil.decryptByPrivateKey(bb);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 将解密信息进行编码转换
            String acceptContent = new String(array);
            try {
                acceptContent = new String(acceptContent.getBytes(), "UTF-8");
                System.out.println("解密后的文件内容: " + acceptContent);  
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
