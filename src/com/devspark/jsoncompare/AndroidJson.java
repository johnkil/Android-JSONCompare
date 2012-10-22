package com.devspark.jsoncompare;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 
 * @author e.shishkin
 *
 */
public class AndroidJson implements JsonHandler {

    public String getName() {
        return "Android";
    }

    @SuppressWarnings("rawtypes")
	public List<Map<String, Object>> parsePublicTimeline(InputStream inputStream) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        try {
            String json = convertStreamToString(inputStream);
            JSONArray jsonArray = new JSONArray(json);
            int length = jsonArray.length();

            for (int i = 0; i < length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if ("user".equals(key)) {
                        JSONObject user = jsonObject.getJSONObject(key);
                        Iterator iterator2 = user.keys();
                        while (iterator2.hasNext()) {
                            String key2 = (String) iterator2.next();
                            map.put("user." + key2, user.get(key2));
                        }
                    } else {
                        map.put(key, jsonObject.get(key));
                    }
                }

                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String convertStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int n = 0;
        try {
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
        } finally {
            out.close();
            in.close();
        }
        return out.toString("UTF-8");
    }

}
