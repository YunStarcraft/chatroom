package util;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLUtilTest
{
	private SAXReader saxReader;
	
	@Before
	public void setUp(){
		saxReader = new SAXReader();
		
	}
	
	@Test
	public void testConstructLoingXML(){
		try{
		
			String xml = XMLUtil.constructLoginXML("helen");
			System.out.println(xml);
			Document document = saxReader.read(new StringReader(xml));
			
			Element root = document.getRootElement();
			String rootName = root.getName();
			
		
			Element typeElement = root.element("type");
			Element userElement = root.element("user");
			
			assertEquals("message",rootName);
			assertEquals("1",typeElement.getText());
			assertEquals("helen",userElement.getText());
		
		
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
	}

}
