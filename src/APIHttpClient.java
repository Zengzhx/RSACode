

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Decoder;

public class APIHttpClient {

    // 接口地址
//    private static String apiURL = "http://192.168.1.10:8087/jwt/jkcourse/attendanceCourse.do";
    //private static String apiURL = "http://218.95.176.67:8088/hwyInterface/manager/per_getLatAndLngByEmployeeId.action";
    private static String apiURL = "http://192.168.1.10:8080/hwyInterface/user/user_insertAttendancesByList.action";
    private Log logger = LogFactory.getLog(this.getClass());
//    private static final String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
    private HttpClient httpClient = null;
    private HttpPost method = null;
    private long startTime = 0L;
    private long endTime = 0L;
    private int status = 0;

    /**
     * 接口地址
     * 
     * @param url
     */
    public APIHttpClient(String url) {

        if (url != null) {
            this.apiURL = url;
        }
        if (apiURL != null) {
            httpClient = new DefaultHttpClient();
            method = new HttpPost(apiURL);

        }
    }

    /**
     * 调用 API
     * 
     * @param parameters
     * @return
     */
    public String post(String parameters) {
        String acceptContent = null;
        logger.info("parameters:" + parameters);

        if (method != null & parameters != null
                && !"".equals(parameters.trim())) {
            try {

                // 建立一个NameValuePair数组，用于存储欲传送的参数
                method.addHeader("Content-type","application/json; charset=utf-8");
                method.setHeader("Accept", "application/json");
                method.setEntity(new StringEntity(parameters));
                startTime = System.currentTimeMillis();

                HttpResponse response = httpClient.execute(method);
                
                endTime = System.currentTimeMillis();
                int statusCode = response.getStatusLine().getStatusCode();
                
                logger.info("statusCode:" + statusCode);
                logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
                if (statusCode != HttpStatus.SC_OK) {
                    logger.error("Method failed:" + response.getStatusLine());
                    status = 1;
                }

                // Read the response body
                String body = EntityUtils.toString(response.getEntity());
                // 对返回值进行解密
                byte[] bb = null;
                byte[] array = null;
                try {
                    bb = new BASE64Decoder().decodeBuffer(body);
                    array = PrivateDecryptUtil.decryptByPrivateKey(bb);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 将解密信息进行编码转换
                acceptContent = new String(array);
                try {
                    acceptContent = java.net.URLDecoder.decode(acceptContent, "UTF-8");
                    acceptContent = new String(acceptContent.getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // 网络错误
                status = 3;
            } finally {
                logger.info("调用接口状态：" + status);
            }

        }
        return acceptContent;
    }

    public static void main(String[] args) {
        APIHttpClient ac = new APIHttpClient(apiURL);
        // 每人需要手动设置调用接口参数
        JSONObject object = new JSONObject();
        object.put("employeeId", "134");
     //   object.put("studentId", "902");
        try {
            byte[] content = PublicEncryptUtil.encryptByPublicKey(java.net.URLEncoder.encode(object.toString(), "UTF-8"));
            String results =  new BASE64Encoder().encodeBuffer(content);
            
            // 输出接口信息
            System.out.println(ac.post(results));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 0.成功 1.执行方法失败 2.协议错误 3.网络错误
     * 
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the endTime
     */
    public long getEndTime() {
        return endTime;
    }
}