package com.devspark.jsoncompare;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


/**
 * 
 * @author e.shishkin
 *
 */
public class SimpleJson implements JsonHandler {

    public String getName() {
        return "JSON.simple";
    }

    @SuppressWarnings("rawtypes")
	public List<Map<String, Object>> parsePublicTimeline(InputStream inputStream) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        JSONParser p = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) p.parse(new InputStreamReader(inputStream));
            int size = jsonArray.size();

            for (int i = 0; i < size; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Set keys = jsonObject.keySet();
                for (Object key: keys) {
                    if ("user".equals(key)) {
                        JSONObject user = (JSONObject) jsonObject.get(key);
                        Set keys2 = user.keySet();
                        for (Object key2: keys2) {
                            map.put("user." + key2, user.get(key2));
                        }
                    } else {
                        map.put(key.toString(), jsonObject.get(key));
                    }
                }

                result.add(map);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
