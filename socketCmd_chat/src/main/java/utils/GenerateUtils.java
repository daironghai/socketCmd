package utils;


import java.util.UUID;

public class GenerateUtils {

	public static String UUID(){
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return uuid;
	}
	
	
}
