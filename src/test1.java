import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


 
public class test1 {

    public static void main(String[] args) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try
        {
            /*urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();*/
            InputStream in = new FileInputStream("E:\\1.txt");
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(new FileOutputStream("F:\\2.txt"));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}
