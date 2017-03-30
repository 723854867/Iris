package org.Iris.app.pay.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author 樊水东
 * 2016年5月19日
 */
public class XMLParser {

	
	static Logger logger = LoggerFactory.getLogger(XMLParser.class);
	
    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
    	
    	InputStream is =  Util.getStringStream(xmlString);

    	return getMapFromStream(is);

    }
    
    public static Map<String,Object> getMapFromStream(InputStream is) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }
    
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
					   +"<return_msg><![CDATA[OK]]></return_msg>"
					   +"<appid><![CDATA[wx2421b1c4370ec43b]]></appid>"
					   +"<mch_id><![CDATA[10000100]]></mch_id>"
					   +"<nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>"
					   +"<sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>"
					   +"<result_code><![CDATA[SUCCESS]]></result_code>"
					   +"<prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>"
					   +"<trade_type><![CDATA[APP]]></trade_type>"
					   +"</xml>";
		
		Map<String,Object> map = getMapFromXML(xml);
		System.out.println(map.size());
	}



}
