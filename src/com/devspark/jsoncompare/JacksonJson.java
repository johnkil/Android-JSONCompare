package com.devspark.jsoncompare;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


/**
 * 
 * @author e.shishkin
 *
 */
public class JacksonJson implements JsonHandler {

    private static JsonFactory sJsonFactory = new JsonFactory();

    public String getName() {
        return "Jackson";
    }

    public List<Map<String, Object>> parsePublicTimeline(InputStream inputStream) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        try {
            JsonParser p = sJsonFactory.createJsonParser(inputStream);

            p.nextToken();

            while (p.nextToken() != JsonToken.END_ARRAY) {

                Map<String, Object> map = new HashMap<String, Object>();

                while (p.nextToken() != JsonToken.END_OBJECT) {

                    String key = p.getCurrentName();
                    p.nextToken(); // move to value, or START_OBJECT/START_ARRAY

                    if (p.getCurrentToken() == JsonToken.START_OBJECT) {
                        while (p.nextToken() != JsonToken.END_OBJECT) {
                            String key2 = p.getCurrentName();
                            p.nextToken(); // move to value, or START_OBJECT/START_ARRAY
                            map.put("user." + key2, p.getText());
                        }
                    } else {
                        map.put(key, p.getText());
                    }

                }

                result.add(map);
            }

            p.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
