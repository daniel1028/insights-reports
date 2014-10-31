package org.gooru.insights.services;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.gooru.insights.models.RequestParamsCoreDTO;
import org.gooru.insights.models.RequestParamsDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

@Service
public class BaseAPIServiceImpl implements BaseAPIService{


	@Autowired
	private BaseConnectionService baseConnectionService;
	
	
	public RequestParamsDTO buildRequestParameters(String data){

		try{
		return data != null ? deserialize(data, RequestParamsDTO.class) : null;
		}catch(Exception e){
			throw new JSONException();
		}
	}

	public RequestParamsCoreDTO buildRequestParamsCoreDTO(String data){

		try{
		return data != null ? deserialize(data, RequestParamsCoreDTO.class) : null;
		}catch(Exception e){
			throw new JSONException();
		}
	}
	public boolean checkNull(String parameter) {

		if (parameter != null && parameter != "" && (!parameter.isEmpty())) {

			return true;

		} else {

			return false;
		}
	}
	
	public boolean checkNull(Object request){
		if (request != null) {

			return true;

		} else {

			return false;
		}
	}
	
	public boolean checkNull(Map<?,?> request) {

		if (request != null && (!request.isEmpty())) {

			return true;

		} else {

			return false;
		}
	}
	
	public boolean checkNull(Collection<?> request) {

		if (request != null && (!request.isEmpty())) {

			return true;

		} else {

			return false;
		}
	}
	
	public boolean checkNull(Integer parameter) {

		if (parameter != null && parameter.SIZE > 0 && (!parameter.toString().isEmpty())) {

			return true;

		} else {

			return false;
		}
	}

	public String assignValue(String parameter) {
		if (parameter != null && parameter != "" && (!parameter.isEmpty())) {

			return parameter;

		} else {

			return null;
		}

	}

	public Integer assignValue(Integer parameter) {

		if (parameter != null && parameter.SIZE > 0 && (!parameter.toString().isEmpty())) {

			return parameter;

		} else {

			return null;
		}
	}
	public <T>  T  deserialize(String json, Class<T> clazz) {
		try {
			return new JSONDeserializer<T>().use(null, clazz).deserialize(json);
		} catch (Exception e) {
			throw new JSONException();
		}
	}
	
	public <T> T deserializeTypeRef(String json, TypeReference<T> type) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, type);
	}
	
	public String[] convertStringtoArray(String data){
		return data.split(",");
	}
	
	public Object[] convertSettoArray(Set<?> data){
		return data.toArray(new Object[data.size()]);
	}
	
	public JSONArray convertListtoJsonArray(List<Map<String,Object>> result){
		JSONArray jsonArray = new JSONArray();
		for(Map<String,Object> entry : result){
			jsonArray.put(entry);	
		}
		return jsonArray;
	}
	public JSONArray InnerJoin(List<Map<String, Object>> parent, List<Map<String, Object>> child, String commonKey) {
		JSONArray jsonArray = new JSONArray();
		if (!child.isEmpty() && !parent.isEmpty()) {
			for (Map<String, Object> childEntry : child) {
				Map<String, Object> appended = new HashMap<String, Object>();
				for (Map<String, Object> parentEntry : parent) {
					if (childEntry.containsKey(commonKey) && parentEntry.containsKey(commonKey)) {
						if (childEntry.get(commonKey).equals(parentEntry.get(commonKey))) {
							childEntry.remove(commonKey);
							appended.putAll(childEntry);
							appended.putAll(parentEntry);
							break;
						}
					}
				}
				if(checkNull(appended)){
				jsonArray.put(appended);
				}
			}
			return jsonArray;
		}
		return jsonArray;
	}
	
	public 	JSONArray InnerJoin(List<Map<String, Object>> parent, List<Map<String, Object>> child){
		JSONArray jsonArray = new JSONArray();
		if (!child.isEmpty() && !parent.isEmpty()) {
			for (Map<String, Object> childEntry : child) {
				Map<String, Object> appended = new HashMap<String, Object>();
					Set<String> keys = childEntry.keySet();
				for (Map<String, Object> parentEntry : parent) {
					boolean valid = true;
					for(String key : keys){
					if(parentEntry.containsKey(key) && childEntry.containsKey(key) && (!parentEntry.get(key).equals(childEntry.get(key)))){
						valid = false;
					}
					}
					if(valid){
					appended.putAll(parentEntry);
					appended.putAll(childEntry);
					break;
					}
				}
				if(checkNull(appended)){
				jsonArray.put(appended);
				}
				}
			}
			return jsonArray;
		}

	public List<Map<String, Object>> innerJoin(List<Map<String, Object>> parent, List<Map<String, Object>> child){
		List<Map<String, Object>> resultData = new ArrayList<Map<String,Object>>();
		if (!child.isEmpty() && !parent.isEmpty()) {
			for (Map<String, Object> childEntry : child) {
				Map<String, Object> appended = new HashMap<String, Object>();
					Set<String> keys = childEntry.keySet();
				for (Map<String, Object> parentEntry : parent) {
					boolean valid = true;
					for(String key : keys){
					if(parentEntry.containsKey(key) && childEntry.containsKey(key) && (!parentEntry.get(key).equals(childEntry.get(key)))){
						valid = false;
					}
					}
					if(valid){
					appended.putAll(parentEntry);
					appended.putAll(childEntry);
					break;
					}
				}
				if(checkNull(appended)){
					resultData.add(appended);
				}
				}
			}
			return resultData;
	}
	@Override
	public String convertArraytoString(String[] datas) {
		StringBuffer result = new StringBuffer();
		for(String data : datas){
			if(result.length() > 0){
			result.append(",");
			}
			result.append(data);
		}
		return result.toString();
	}
	
public static void main(String args[]){
	BaseAPIServiceImpl baseAPIService = new BaseAPIServiceImpl();
	double count =1;
	String data [] = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
	for(int i=0;i<26;i++){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("timespent", 1*count);
		dataMap.put("views", i);
		dataMap.put("title", data[i]);
		count = count*100;
		dataList.add(dataMap);
	}
	
	baseAPIService.sortBy(dataList, "title", "DESC");
}
	
	public List<Map<String, Object>> sortBy(List<Map<String, Object>> requestData, String sortBy, String sortOrder) {

		if (checkNull(sortBy)) {
			for (final String name : sortBy.split(",")) {
				boolean ascending = false;
				boolean descending = false;
				if (checkNull(sortOrder)) {
					if (sortOrder.equalsIgnoreCase("ASC")) {
						ascending = true;
					} else if (sortOrder.equalsIgnoreCase("DESC")) {
						descending = true;
					} else {
						ascending = true;
					}
				} else {
					ascending = true;
				}
				if (ascending) {
					Collections.sort(requestData, new Comparator<Map<String, Object>>() {
						public int compare(final Map<String, Object> m1, final Map<String, Object> m2) {
							System.out.println(" m1 type "+m1.get(name).getClass().getName()+" me2 type "+m2.get(name).getClass().getName()+" m1 name "+m1.get(name)+" m2 name"+m2.get(name)+" name"+name);
							if (m1.containsKey(name) && m2.containsKey(name)) {
								if (m1.get(name) instanceof String) {
									if (m2.containsKey(name))
										return ((String) m1.get(name).toString().toLowerCase()).compareTo((String) m2.get(name).toString().toLowerCase());
								}  else if (m1.get(name) instanceof Integer) {
									if (m2.containsKey(name))
										try{
										return ((Integer) m1.get(name)).compareTo((Integer) m2.get(name));
										}catch(Exception e){
											System.out.println("exception 1");
											try{
											return ((Long.valueOf(m1.get(name).toString())).compareTo((Long.valueOf(m2.get(name).toString()))));
										}catch(Exception e1){
											System.out.println("exception 2");
											return ((Double.valueOf(m1.get(name).toString())).compareTo((Double.valueOf(m2.get(name).toString()))));
										}
										}
								}
							}
							return 1;
						}
					});
				}
				if (descending) {
					
					Collections.sort(requestData, new Comparator<Map<String, Object>>() {
						public int compare(final Map<String, Object> m1, final Map<String, Object> m2) {

							if (m2.containsKey(name)) {
								if (m1.containsKey(name)) {
									if (m1.get(name) instanceof String) {
										if (m2.containsKey(name))
											return ((String) m2.get(name).toString().toLowerCase()).compareTo((String) m1.get(name).toString().toLowerCase());
									}  else if (m1.get(name) instanceof Integer) {
										if (m2.containsKey(name))
											try{
											return ((Integer) m2.get(name)).compareTo((Integer) m1.get(name));
											}catch(Exception e){
												System.out.println("exception 3");
												try{
												return ((Long.valueOf(m2.get(name).toString())).compareTo((Long.valueOf(m1.get(name).toString()))));
											}catch(Exception e1){
												System.out.println("exception 4");
												return ((Double.valueOf(m2.get(name).toString())).compareTo((Double.valueOf(m1.get(name).toString()))));
											}
											}
									}
								} else {
									return 1;
								}
							} else {
								return -1;
							}
							return 0;
						}
					});

				}
			}
		}
		System.out.println("result a"+requestData);
		return requestData;
	}
	
	public JSONArray formatKeyValueJson(List<Map<String,Object>> dataMap,String key) throws org.json.JSONException{
	
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		Map<String,String> resultMap = new HashMap<String, String>();
		Gson gson = new Gson();
		for(Map<String,Object> map : dataMap){
			if(map.containsKey(key)){
				String jsonKey = map.get(key).toString();
				map.remove(key);
					json.accumulate(jsonKey, map);
			}
		}
		resultMap = gson.fromJson(json.toString(),resultMap.getClass());
		Map<String,Object> Treedata = new TreeMap<String, Object>(resultMap);
		for(Map.Entry<String, Object> entry : Treedata.entrySet()){
			JSONObject resultJson = new JSONObject();
			resultJson.put(entry.getKey(), entry.getValue());
			jsonArray.put(resultJson);
		}
		return jsonArray;
	}
	
	public String convertTimeMstoISO(Object milliseconds){
		
		DateFormat ISO_8601_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
		ISO_8601_DATE_TIME.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date(Long.valueOf(milliseconds.toString()));
		return ISO_8601_DATE_TIME.format(date);
	}
	
}
