package com.inng.hope.springmvc.init;

import java.io.File;
import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringMvcInit extends DispatcherServlet{

	public SpringMvcInit(){
		//getXml();
	}
	
	
	private void getXml(){
		try {
			String xmlPath = this.getClass().getClassLoader().getResource("").getPath()+"com/inng/hope/dao/UserDaoMapper.xml";
			SAXReader saxReader = new SAXReader();
			System.out.println(this.getClass().getClassLoader().getResource("").getPath());
			System.out.println(System.getProperty("user.dir"));
			Document document = saxReader.read(new File(xmlPath));
			Element root = document.getRootElement();
			
			
			Element selectElement = root.addElement("select");
			selectElement.addAttribute("id", "getById");
			selectElement.addAttribute("parameterType", "Integer");
			selectElement.addAttribute("resultType", "user");
			selectElement.setText("select * from user where id =#{id}");
			
			OutputFormat format = new OutputFormat();
			format.setEncoding("utf8");
			
			XMLWriter xmlWriter = new XMLWriter(new FileWriter(xmlPath),format);
			
	        xmlWriter.write(document);
	        xmlWriter.flush();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// <select id="getById" parameterType="Integer" resultType="user"> 
   // select * from user where id =#{id}
//</select>
	
	public static void main(String[] args) {
		
		
	}
	

}
