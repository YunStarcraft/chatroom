package util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Self-Defined XML Format
 * 
 * <?xml version="1.0" encoding="utf-8"?> <message> <type>1</type>
 * <user>jerry</user> </message>
 *
 * userlogin type = 1 
 * chat message from client to server = 2 
 * chat message from server to all clients = 3 
 * userList type = 4 close Client Window = 5 close
 * Server Window = 6 
 * Client Window Close Confirmation from Server = 7
 * login result from server to client  = 8
 * 
 */

public class XMLUtil
{

	private static Document constructDocument()
	{
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");

		document.setRootElement(root);
		return document;

	}

	/*
	 * Message sent from client to server when login
	 * 
	 * @return
	 */
	public static String constructLoginXML(String username)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();

		Element type = root.addElement("type");
		type.setText("1");

		Element user = root.addElement("user");
		user.setText(username);

		// return document.toString(); // error!

		return document.asXML();

	}

	/**
	 * resolve username from clients login message
	 */

	public static String extractUsername(String xml)
	{

		try
		{
			SAXReader saxReader = new SAXReader();

			Document document = saxReader.read(new StringReader(xml));

			Element user = document.getRootElement().element("user");

			return user.getText();

		} catch (Exception ex)
		{

		}
		return null;
	}

	/***
	 * 
	 * Build UserList XML UserList XML format <message> <type>4</type>
	 * 
	 * <user>jerry</user> <user>tommy</user> <user>jordan</user> </message>
	 * 
	 * @param message
	 * @return
	 */

	public static String constructUserList(Set<String> users)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("4");
		for (String user : users)
		{
			Element e = root.addElement("user");
			e.setText(user);
		}
		return document.asXML();
	}

	/***
	 * extract all username list from xml
	 */

	public static List<String> extractUserList(String xml)
	{
		List<String> list = new ArrayList<String>();
		try
		{
			SAXReader saxReader = new SAXReader();

			Document document = saxReader.read(new StringReader(xml));

			for (Iterator iter = document.getRootElement().elementIterator("user"); iter.hasNext();)
			{
				Element e = (Element) iter.next();
				list.add(e.getText());

			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return list;
	}

	public static String extractType(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element typeElement = document.getRootElement().element("type");

			return typeElement.getText();

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;

	}

	/***
	 * 
	 * Chat message sent from client to server chat message XML format
	 * <message> <type>2</type> <user>jerry</user> <content>chat
	 * message</content> </message>
	 * 
	 * @param message
	 * @return
	 */

	public static String constructMessageXML(String username, String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();

		Element type = root.addElement("type");
		type.setText("2");

		Element user = root.addElement("user");
		user.setText(username);

		Element content = root.addElement("content");
		content.setText(message);

		return document.asXML();

	}

	/***
	 * extract message content inside xml from client to server
	 */

	public static String extractContent(String xml)
	{

		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element contentElement = document.getRootElement().element("content");

			return contentElement.getText();

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/***
	 * 
	 * Chat message sent from server to all clients chat message XML format
	 * <message> <type>3</type>
	 * 
	 * <content>username:chat message</content> </message>
	 * 
	 * @param message
	 * @return
	 */

	public static String constructServerMessageXML(String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("3");

		Element content = root.addElement("content");
		content.setText(message);

		return document.asXML();

	}

	/***
	 * construct server window closing event XML Format
	 * <message> <type>6</type> </message>
	 * 
	 * 
	 */

	public static String constructCloseServerWindowXML()
	{

		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("6");

		return document.asXML();
	}

	/***
	 * construct Client Window closing event XML Format
	 * <message> 
	 * <type>5</type> 
	 * </message>
	 * 
	 * 
	 */

	public static String constructCloseClientWindowXML(String username)
	{

		Document document = constructDocument();
		Element root = document.getRootElement();

		Element type = root.addElement("type");
		type.setText("5");

		Element user = root.addElement("user");
		user.setText(username);

		return document.asXML();
	}

	/***
	 * construct Client Window Close Confirmation from Server XML
	 *  <message> 
	 * <type>7</type> 
	 * </message>
	 */
	public static String constructCloseWindowConfirmationXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();

		Element type = root.addElement("type");
		type.setText("7");

		

		return document.asXML();

	}
	
	/***
	 * build login result from server to client XML
	 *  <message> 
	 * <type>8</type> 
	 * <result>success/error</result>
	 * </message>
	 * 
	 */
	public static String constructLoginResultXML(String result){
		
		Document document = constructDocument();
		Element root = document.getRootElement();

		Element typeElement = root.addElement("type");
		typeElement.setText("8");

		Element resultElement = root.addElement("result");
		resultElement.setText(result);

		return document.asXML();
	}
	
	
	/***
	 * extract login result from XML
	 */
	
	public static String extractLoginResult(String xml){
		String result = null;
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element contentElement = document.getRootElement().element("result");

			return contentElement.getText();

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
		
	}
	

}
