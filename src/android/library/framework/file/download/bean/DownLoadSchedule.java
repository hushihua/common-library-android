/**
 * 
 */
package android.library.framework.file.download.bean;

import java.io.Serializable;

/**
 * @author hushihua
 *
 */
public class DownLoadSchedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum RunType{
		WAITING, LOADING, FINISH, ERROR, PAUSE, DELETE, INSTALL, NOME, REMOVE
	}
	
	public int totalSize;
	public int completeSize;
	public RunType state; //emun
	public int property;//优先级
	public String path;  //要有一个创建机制
	public String downLoadUrl;
	public RunType common; //emun 命令
	
}
