package com.devspark.jsoncompare;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author e.shishkin
 *
 */
public class GsonJson implements JsonHandler {

    public String getName() {
        return "Gson";
    }

    public List<Map<String, Object>> parsePublicTimeline(InputStream inputStream) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            reader.beginArray();
            while (reader.hasNext()) {
                Map<String, Object> map = new HashMap<String, Object>();

                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if ("user".equals(name)) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String name2 = reader.nextName();
                            map.put("user." + name2, getValue(reader));
                        }
                        reader.endObject();
                    } else {
                        map.put(name, getValue(reader));
                    }
                }

                reader.endObject();

                result.add(map);
            }
            reader.endArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    static Object getValue(JsonReader r) throws IOException {
        Object value = null;
        JsonToken token = r.peek();

        switch (token) {
            case NULL:
                r.nextNull();
                break;
            case BOOLEAN:
                value = r.nextBoolean();
                break;
            default:
                value = r.nextString();
        }

        return value;
    }

}
