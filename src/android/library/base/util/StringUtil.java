/**
 * 
 */
package android.library.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hushihua
 *
 */
public class StringUtil {
	
	private final static String IP_PATTERN = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	private final static String PHONE_PATTERN = "^[1][3-8]\\d{9}$";
	private final static String EMAIL_PATTERN = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	/**
	 * 
	 * @param value
	 * @return true 验证通过，false验证不通过
	 */
	public static boolean matchIp(String value){
		if (null == value || "".equals(value)) return false;  
		Pattern pattern = Pattern.compile(IP_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
	
	/**
	 * 
	 * match return false
	 * @param value 
	 * @return true 验证通过，false验证不通过
	 */
	public static boolean matchPhoneNumber(String value){
		if (null == value || "".equals(value)) return false;  
		Pattern pattern = Pattern.compile(PHONE_PATTERN);
	    Matcher matcher = pattern.matcher(value);
	    return matcher.matches();
	}
	
	/**
	 * matchEmailValue
	 * 
	 * @param value
	 * @return true 验证通过，false验证不通过
	 */
	public static boolean matchEmail(String value){
		if (null == value || "".equals(value)) return false;  
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	    Matcher matcher = pattern.matcher(value);
	    return matcher.matches();
	}
}
