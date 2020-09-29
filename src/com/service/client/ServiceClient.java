package com.service.client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;


/**
 * @author Alok Kumar
 *
 */
public class ServiceClient 
{
	//private final Logger LOG = LogManager.getLogger(ServiceClient.class);
		
	// Method without credentials to call rest APIs
	public Map<String,String> getMapResponse(String url, String httpMethod, String jsonReq, String timeOut, String siNumber) 
	{
		String resp = null;
		InputStream is = null;
		BufferedReader rd = null;
		StringBuffer result = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		String methodName="ServiceClient :: getMapResponse : "+siNumber;
		try {
			int timeout = 0;
			if(null!=timeOut) {
				timeout = Integer.parseInt(timeOut);
			}
			System.out.println(methodName+" Request URL: "+url+", HTTP Method: "+httpMethod+", jsonReq: "+jsonReq+", TimeOut: "+timeOut);
			
			
			long startTime = System.currentTimeMillis();
			
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			if (httpMethod.equalsIgnoreCase("post")) {
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
				OutputStream out = connection.getOutputStream();
				out.write(jsonReq.getBytes("UTF-8"));
				out.close();
			} else {
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
			}
			int statusCode = connection.getResponseCode();
			
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(methodName+" Response startTime: "+startTime+" Response endTime: "+endTime+" Response totalTime: "+totalTime);
			System.out.println(methodName+" Response Code: "+statusCode);

			if (statusCode !=200) {
				 is = connection.getErrorStream();
			}
			else {
				 is = connection.getInputStream(); 
			}
			rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			resp = result.toString();
			System.out.println(methodName+" Response from ServiceClient: "+resp);
			
			map.put("responseCode", ""+statusCode);
			map.put("response", resp);
			rd.close();
			is.close();

		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" IOException: "+error);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" Exception: "+error);
		}
		
		return map;
	}

	// Method with credentials to call rest APIs
	
	public Map<String,String> getMapResponse1(String url, String httpMethod, String jsonReq, boolean cerdentialFlag, String user,String pass,String timeOut, String siNumber) 
	{
		String resp = null;
		InputStream is = null;
		BufferedReader rd = null;
		StringBuffer result = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		String methodName="ServiceClient :: getMapResponse : "+siNumber;
		try {
			int timeout = 0;
			if(null!=timeOut) {
				timeout = Integer.parseInt(timeOut);
			}
			System.out.println(methodName+" Request URL: "+url+", HTTP Method: "+httpMethod+", jsonReq: "+jsonReq+", TimeOut: "+timeOut+", Credential Flag: "+cerdentialFlag+", User: "+user);
			
			String authString = user + ":" + pass;
			String authStringEnc = "Basic " + new BASE64Encoder().encode(authString.getBytes());
			
			
			long startTime = System.currentTimeMillis();
			
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			if (httpMethod.equalsIgnoreCase("post")) {
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				connection.setRequestProperty("Authorization", authStringEnc);
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
				OutputStream out = connection.getOutputStream(); //OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(jsonReq.getBytes("UTF-8")); //out.write(jsonReq.toString());
				out.close();
			} else {
				connection.setRequestProperty("Authorization", authStringEnc);
				connection.setRequestMethod("GET");
				//connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
			}
			int statusCode = connection.getResponseCode();
			
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(methodName+" Response startTime: "+startTime+" Response endTime: "+endTime+" Response totalTime: "+totalTime);
			System.out.println(methodName+" Response Code: "+statusCode);

			if (statusCode !=200) {   //if (statusCode >= 200 && statusCode < 400) {
				 is = connection.getErrorStream();
			}
			else {
				 is = connection.getInputStream(); 
			}
			rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			resp = result.toString();
			System.out.println(methodName+" Response from ServiceClient: "+resp);
			
			map.put("responseCode", ""+statusCode);
			map.put("response", resp);
			rd.close();
			is.close();

		} catch (IOException e) {
			//e.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" IOException: "+error);
		} catch (Exception e) {
			//e.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" Exception: "+error);
		}
		
		return map;
	}
	
	// This method is used to call SOAP services. No need to create stub etc and extra code to call the same.
	
	public Map<String,String> getHttpSoapResponse(String url, String httpMethod, String req, boolean cerdentialFlag, String user,String pass,String timeOut, String soapAction) 
	{
		String resp = null;
		InputStream is = null;
		BufferedReader rd = null;
		StringBuffer result = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		String methodName="getHttpSoapResponse : ";
		try {
			int timeout = 0;
			if(null!=timeOut) {
				timeout = Integer.parseInt(timeOut);
			}
			//System.out.println(methodName+" Request URL: "+url+", HTTP Method: "+httpMethod+", Request: "+req+", TimeOut: "+timeOut+", Credential Flag: "+cerdentialFlag+", User: "+user);

			String authString = user + ":" + pass;
			String authStringEnc = "Basic " + new BASE64Encoder().encode(authString.getBytes());

			long startTime = System.currentTimeMillis();
			System.out.println(methodName+" Request URL: "+url+", HTTP Method: "+httpMethod+", Request: "+req+", TimeOut: "+timeOut+", Credential Flag: "+cerdentialFlag+", User: "+user+", SoapAction: "+soapAction);

			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			
			//System.out.println("SoapAction : " + soapAction + " req : " + req);
			if (httpMethod.equalsIgnoreCase("post")) {
				//				connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
				connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
				connection.setRequestProperty("Authorization", authStringEnc);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("SOAPAction",soapAction);
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
				//				OutputStream out = connection.getOutputStream(); 
				//				out.write(req.getBytes("UTF-8"));
				//				out.write(req);
				//				out.close();
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(req);
				out.flush();
				out.close();
			} else {
				//System.out.println("inside else part");
				connection.setRequestProperty("Authorization", authStringEnc);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/xml");
				connection.setDoOutput(true);
				connection.setConnectTimeout(timeout);
			}
			int statusCode = connection.getResponseCode();

			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(methodName+" Response startTime: "+startTime+" Response endTime: "+endTime+" Response totalTime: "+totalTime);
			System.out.println(methodName+" Response Code: "+statusCode);
			//System.out.println(methodName+" Response Code: "+statusCode);
			if (statusCode !=200) {
				is = connection.getErrorStream();
			}
			else {
				is = connection.getInputStream(); 
			}
			rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			resp = result.toString();
			System.out.println(methodName+" Response from ServiceClient: "+resp);
			//System.out.println(methodName+" Response from ServiceClient: "+resp);
			map.put("responseCode", ""+statusCode);
			map.put("response", ""+result.toString());
			rd.close();
			is.close();

		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" IOException: "+error);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String error = sw.toString();
			System.err.println(methodName+" Exception: "+error);
		}

		return map;
	}	
}
