package com.devspark.jsoncompare;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author e.shishkin
 *
 */
public interface JsonHandler {

	/**
	 * Getting name of JSON handler.
	 * 
	 * @return name
	 */
    String getName();

    /**
     * Parse simple JSON.
     * 
     * @param inputStream
     * @return
     */
    List<Map<String, Object>> parsePublicTimeline(InputStream inputStream);

}
