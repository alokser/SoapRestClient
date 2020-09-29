package com.service.client.test;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.service.bean.ConfigBean;
import com.service.bean.RequestErrorVO;
import com.service.client.ServiceClient;

public class SoapClientMain {
	//private static final Logger LOG = LogManager.getLogger(ClientMain.class);
	
	public static void main(String...args) {
		String code = null;
		String response = null;
		ConfigBean bean = new ConfigBean();
		RequestErrorVO resp = null;
		try {
			String request = createRequest(bean);
			//System.out.println("Request: "+request);
			ServiceClient client = new ServiceClient();

			//String url = "http://localhost:7001/service/st.swe?SSource=WebService&Cmd=Execute";
			//String soapAction = "'document/http://service.com/test:Workflow'"; // This will be your action name
			
			
			String url = "http://localhost:7777/eai_anon_enu/start.swe?SWEExtSource=AnonWebService&SweExtCmd=Execute";
			String soapAction = "'document/http://siebel.com/CustomUI:BTFLY_spcCreate_spcSales_spcChannel_spcInbound_spcWorkflow'";
			
			System.out.println("User Create URL: "+url);
			
			Map<String,String> resMap = client.getHttpSoapResponse(url, "POST", request, false, null, null, "6000", soapAction);
			if(null!=resMap) {
				code = (String)resMap.get("responseCode");
				response = (String)resMap.get("response");
				System.out.println("Code: "+code);
				System.out.println("Response: "+response);  
			}
			resp = parseResponse(response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static String createRequest(ConfigBean bean) 
	{
		// Make complete xml request. Below is demo with omitted request
		
		String req = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://test.com\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
					// put soap xml request here
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>\r\n" + 
				"";

		return req;	
	}
	
	private static RequestErrorVO parseResponse(String response) {
		RequestErrorVO resp = null;
		try {
			//System.out.println("response : " + response);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
			NodeList nodes = doc.getElementsByTagName("SOAP-ENV:Body");
			resp = new RequestErrorVO();
			for (int i = 0; i < nodes.getLength(); i++) 
			{
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String errorCode = getValue("ns:Error_spcCode", element);
					String errorMessage = getValue("ns:Error_spcMessage", element);
					System.out.println("Error Code: " + errorCode);
					System.out.println("errorMessage: "+ errorMessage);
					resp.setErrorCode(errorCode);
					resp.setErrorDescription(errorMessage);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}	
}
