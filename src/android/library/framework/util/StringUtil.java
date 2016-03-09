/**
 * 
 */
package android.library.framework.util;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

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
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) {
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = p.matcher(email);
		System.out.println(m.matches() + "-email-");
		return m.matches();
	}
	
	/**
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,3,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "-telnum-");
		return m.matches();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isCorrectUserName(String name) {
		Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");
		Matcher m = p.matcher(name);
		System.out.println(m.matches() + "-name-");
		return m.matches();
	}

	/**
	 * 
	 * @param pwd
	 * @return
	 * 
	 */
	public static boolean isCorrectUserPwd(String pwd) {
		Pattern p = Pattern.compile("\\w{6,18}");
		Matcher m = p.matcher(pwd);
		System.out.println(m.matches() + "-pwd-");
		return m.matches();
	}
	
    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
    
	/**
	 * 
	 * @param expression
	 * @param text
	 * @return
	 */
	private static boolean matchingText(String expression, String text) {
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(text);
		boolean b = m.matches();
		return b;
	}

	/**
	 * 
	 * @param zipcode
	 * @return
	 */
	public static boolean isZipcode(String zipcode) {
		Pattern p = Pattern.compile("[0-9]\\d{5}");
		Matcher m = p.matcher(zipcode);
		System.out.println(m.matches() + "-zipcode-");
		return m.matches();
	}



	/**
	 * 
	 * @param telfix
	 * @return
	 */
	public static boolean isTelfix(String telfix) {
		Pattern p = Pattern.compile("d{3}-d{8}|d{4}-d{7}");
		Matcher m = p.matcher(telfix);
		System.out.println(m.matches() + "-telfix-");
		return m.matches();
	}
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static String md5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
}
