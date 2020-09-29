package com.service.utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;*/

import com.google.gson.Gson;


/**
 * @author Alok-Kumar
 *
 */
public class ParseJsonJava 
{
	//final static Logger logger=LogManager.getLogger(JsonToJava.class);
	
	public static List<Map> getJSONMap(String jsonString, String attributeName) 
	{
		List<Map> listFoundMap=null;
		try {
			Gson gson=new  Gson();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>)gson.fromJson(jsonString, map.getClass());
			//MY_LOGGER.debug("Map: "+map);
			listFoundMap=new ArrayList<Map>();

			Stack<Map<String,Object>> mapStack= new Stack<Map<String,Object>>();
			mapStack.push(map);
			
			SearchStack(mapStack,attributeName,listFoundMap); // null might be here
			//MY_LOGGER.debug("List Found: "+listFoundMap);			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return listFoundMap;
	}

	private static Map SearchStack(Stack<Map<String, Object>> mapStack,
			String attributeName, List foundMap) {
		Map elementMap = null;
		while (!mapStack.isEmpty()) {
			elementMap = mapStack.pop();
			Iterator it = elementMap.entrySet().iterator(); // check for null
			while (it.hasNext()) {
				Entry pair = (Entry) it.next();
				if (pair.getKey().equals(attributeName)) {
					elementMap = new HashMap<String, Object>();
					elementMap.put(pair.getKey(), pair.getValue());
					foundMap.add(elementMap);
				}
				if (pair.getValue() instanceof Map) {
					mapStack.push((Map) pair.getValue());
				} else if (pair.getValue() instanceof ArrayList) {
					List<String> strList = new ArrayList<String>();
					for (int i = 0; i < ((ArrayList) pair.getValue()).size(); i++) {
						Object obj = ((ArrayList) (pair.getValue())).get(i);
						if (obj instanceof String) {
							if (pair.getKey().equals(attributeName)) {
								strList.add(obj.toString());
							}
						} else if (obj instanceof Map) {
							mapStack.push((Map) obj);
						}
					}
					if (pair.getKey().equals(attributeName)) {
						if (strList.size() > 0) {
							elementMap = new HashMap<String, Object>();
							elementMap.put(pair.getKey(), strList);
							foundMap.add(elementMap);
						}
					}
				}
			}
		}
		return elementMap;
	}
}