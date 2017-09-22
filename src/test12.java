import java.io.IOException;

import sun.misc.BASE64Decoder;


public class test12 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String body = "Je3v5KL3LRhNeKY48j0tC1hJepCnJl8gmxn7FKcS7gaATl5U4i1vYw1IZnGrxMkaUuQs56rKRnQmwl78uDPqqBO8o0zTKM/i5xNli25nIK05WfSm9B5UZDg2VzDxiXzvCXG/WxOOAFFnvVWfZ3cUKwx7EJNgjL2rTKeDTcR1Dyk=";
		byte[] bb = null;
		try {
			bb = new BASE64Decoder().decodeBuffer(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			byte[] array = PrivateDecryptUtil.decryptByPrivateKey(bb);
			String  acceptContent = new String(array);
			System.out.println(acceptContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
