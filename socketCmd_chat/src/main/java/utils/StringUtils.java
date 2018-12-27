package utils;

import java.util.Collection;
import java.util.Collections;

public class StringUtils {

	
	public static String join(String[] arr, String flag){
		String ret = "";
		for (String string : arr) {
			ret += string + flag;
		}
		ret = ret.trim();
		return ret;
	}
	
	
	public static String join(Collection<String> list, String flag){
		String ret = "";
		for (String string : list) {
			ret += string + flag;
		}
		ret = ret.trim();
		return ret;
	}
	
	
	
}
