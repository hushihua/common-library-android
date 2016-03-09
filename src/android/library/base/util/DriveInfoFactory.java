/**
 * 
 */
package android.library.base.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author hushihua
 *
 */
public class DriveInfoFactory {

	/**
	 * 获取唯一码
	 * @param context
	 * @return
	 */
	public static String getDriveID(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}
	
	/**
	 * 获取IP
	 * @param context
	 * @return
	 */
	public static String getDriveIP(Context context){
		 String ipaddress="";
         try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
	             NetworkInterface intf = en.nextElement();  
	             for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                 InetAddress inetAddress = enumIpAddr.nextElement();  
	                 if (!inetAddress.isLoopbackAddress()) {  
//	                     ipaddress=ipaddress+";"+ inetAddress.getHostAddress().toString(); 
	                	 String address = inetAddress.getHostAddress().toString();
	                	 if (StringUtil.matchIp(address)) {
							return address;
						 }
	                 }  
	             }  
	        }  
	     } catch (SocketException ex) {  
	        Log.e("WifiPreference IpAddress", ex.toString());  
	     }  
	     return ipaddress;
	}
	
	/**
	 * 检查网络是否连接
	 * @param context
	 * @return true 已连接，false 无连接
	 */
	public boolean isNetworkConnected(Context context){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * GET MAC ADDRESS
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {   
	    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
	    WifiInfo info = wifi.getConnectionInfo();   
	    return info.getMacAddress();   
	}   

	/**
	 * get phone number
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getImeiCode(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

}
