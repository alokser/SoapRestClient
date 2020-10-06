package com.service.client.test;

import java.util.List;
import java.util.Map;

import com.service.bean.ConfigBean;
import com.service.bean.RequestErrorVO;
import com.service.client.ServiceClient;
import com.service.utility.ParseJsonJava;

public class RestClientMain 
{
	public static void main(String...args) 
	{
		ConfigBean bean = new ConfigBean();
		new RestClientMain().createUser(bean);
	}
	
	public RequestErrorVO createUser(ConfigBean bean) {

		RequestErrorVO errorVo = new RequestErrorVO();

		String createUser = "{\"msisdn\":\"9999999999\",\"userName\":\"Alok\"}";

		Map<String,String> map = createUser(bean, createUser);

		if(null!=map) 
		{
			String responseCode = (String)map.get("responseCode");
			System.out.println("Create User Response Code: "+responseCode);

			errorVo = setServiceMessage((String)map.get("response"),errorVo);

			if(null!=responseCode && "200".equalsIgnoreCase(responseCode.trim()) && "SUCCESS".equalsIgnoreCase(errorVo.getErrorStatus().trim())) 
			{
				errorVo.setErrorCode("00OK");
			} else {
				errorVo.setErrorCode("00NOTOK");
			}
		}
		return errorVo;
	}

	private RequestErrorVO setServiceMessage(String response, RequestErrorVO errorVo) 
	{
		String errorCode = null;
		String errorMessage = null;

		ParseJsonJava jtj = new ParseJsonJava();
		List<Map> lm = jtj.getJSONMap(response, "error");
		if(null!=lm) {
			Map statusMap = (Map)lm.get(0);
			if (null != statusMap) 
			{
				Map status = (Map) statusMap.get("error");
				if (null != status) {
					errorCode = (String)status.get("errorCode");
					errorMessage = (String)status.get("errorMessage");
					errorVo.setErrorStatus(errorCode);
					errorVo.setErrorMessage(errorMessage);

				}
			}
		}

		return errorVo;
	}

	private RequestErrorVO setServiceMessage1(String response, RequestErrorVO errorVo) 
	{
		String errorCode = null;
		String errorMessage = null;

		ParseJsonJava jtj = new ParseJsonJava();
		List<Map> lm = jtj.getJSONMap(response, "status");
		if(null!=lm) {
			Map statusMap = (Map)lm.get(0);
			if (null != statusMap) 
			{
				Map status = (Map) statusMap.get("status");
				if (null != status) {
					errorCode = (String)status.get("code");
					errorMessage = (String)status.get("message");
					errorVo.setErrorStatus(errorCode);
					errorVo.setErrorMessage(errorMessage);

				}
			}
		}
		return errorVo;
	}
	
	private Map<String, String> createUser(ConfigBean schannel, String jsonReq) {
		Map<String,String> map = null;
		try {
			String url = ""; // Give your SIT/PT/PROD URL

			ServiceClient client = new ServiceClient();
			map  = client.getMapResponse1(url, "POST", jsonReq, false, null, null, "10000", "CREATE_USER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}	
}
