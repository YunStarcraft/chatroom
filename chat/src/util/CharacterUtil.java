package util;

import java.util.Iterator;
import java.util.Map;

public class CharacterUtil
{
	public static final int LOGIN = 1;
	
	public static final int CLIENT_MESSAGE = 2;
	
	public static final int SERVER_MESSAGE = 3;
	
	public static final int USER_LIST = 4;
	
	public static final int CLOSE_CLIENT_WINDOW = 5;
	
	public static final int CLOSE_SERVER_WINDOW = 6;
	
	public static final int CLOSE_CLIENT_WINDOW_CONFIRMATION = 7;
	
	public static final String ERROR = "ERROR";
	
	public static final String SUCCESS = "SUCCESS";
	
	public static int PORT = generatePort();
	public static int PORT2 = generatePort();
	public static String SERVER_HOST; //server address info
	public static String SERVER_PORT;
	public static String CLIENT_NAME;
	
	public static int randomPort = generatePort(); // receive message Port
	public static int randomPort2 = generatePort(); // receive user-list Port
	
	// no member instance needed , static/class method is good

	// decide whether string is null
	public static boolean isEmpty(String str)
	{
		if ("".equals(str))
		{
			return true;
		}
		return false;
	}

	public static boolean isNumber(String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;

	}

	public static boolean isPortCorrect(String port)
	{
		int temp = Integer.parseInt(port);
		if(temp<=1024||temp>65535){
			return false;
		}
		return true;

	}
	
	/**
	 * generate random port number ,which is >=1025
	 */
	
	public static int generatePort(){
		int port = (int)(Math.random()*50000 +1025);
		return port;
	}
	
	/**
	 * decide whether duplicate username
	 */
	public static boolean isUsernameDuplicated(Map map, String username){
		for(Iterator iter = map.keySet().iterator();iter.hasNext();){
			String temp = (String)iter.next();
			if(username.equals(temp)){
				return true;
			}
			
		}
		return false;
	}
	
	
	public static boolean isCorrect(String str){
		for(int i = 0 ;i <str.length(); i++){
			
			char ch = str.charAt(i);
			if('@' == ch || '/' == ch){
				return false;
				
			}
		}
		return true;
	}
}
