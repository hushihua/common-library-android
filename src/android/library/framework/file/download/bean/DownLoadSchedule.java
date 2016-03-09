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
	public int property;//���ȼ�
	public String path;  //Ҫ��һ����������
	public String downLoadUrl;
	public RunType common; //emun ����
	
}