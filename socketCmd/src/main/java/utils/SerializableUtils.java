package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

public class SerializableUtils {
	
	
	public static byte[] inputStream(Object obj) throws Exception {
		//
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(os);
		out.writeObject(obj);
		//
		byte[] buff = new byte[1024 * 10];
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		int len = is.read(buff);
		buff = Arrays.copyOf(buff, len);

		out.flush();
		out.close();
		return buff;
	}
	
	
	
	public static Object outputStream(byte[] buf) throws Exception {
		ByteArrayInputStream bIn =  new ByteArrayInputStream(buf);
		ObjectInputStream in = new ObjectInputStream(bIn);
		Object obj = in.readObject();
		in.close();
		return obj;
	}
	
	
	
	public static String UUID(){
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return uuid.substring(0, 16);
	}
	
	
	
	
	
	
	
	
}
