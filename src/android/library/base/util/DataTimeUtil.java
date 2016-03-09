/**
 * 
 */
package android.library.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author hushihua
 *
 */
public class DataTimeUtil {

	private static SimpleDateFormat formatBuilder;

	public static String getDate(String format) {
		formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(new Date());
	}

	public static String getDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}
	public static String getDates() {
		return getDate("MM-dd HH:mm:ss");
	}
	
	public static String getSystemTime(){
		return getDate("yyyyMMddHHmmssSSS");
	}
	
	public static Date getData(String sDt){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmssSSS");
		try {
			Date dt2 = sdf.parse(sDt);
			return dt2;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDate(Date date, String format){
		SimpleDateFormat formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(date);
	}
	
    /**
    *
    * @param endTime
    * @param countDown
    * @return 剩余时间
    */
	public static String calculationRemainTime(String endTime, long countDown) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date now = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
			Date endData = df.parse(endTime);
			long l = endData.getTime() - countDown - now.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			return "ʣ��" + day + "��" + hour + "Сʱ" + min + "��" + s + "��";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}
