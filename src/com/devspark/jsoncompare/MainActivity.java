package com.devspark.jsoncompare;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 
 * @author e.shishkin
 *
 */
public class MainActivity extends Activity {
	
	private static final String SAMPLE_JSON_FILE_NAME = "public_timeline.json";

    private LinearLayout mLayout;
    private LinearLayout.LayoutParams mLayoutParams;

    private final Runnable mTestTask = new Runnable() {
        public void run() {

            final Map<String, Long> results = new HashMap<String, Long>();

            testImpl(new AndroidJson(), results);
            testImpl(new SimpleJson(), results);
            testImpl(new GsonJson(), results);
            testImpl(new JacksonJson(), results);

            runOnUiThread(new Runnable() {
                public void run() {

                    mLayout.removeAllViews();

                    List<String> keys = new ArrayList<String>(results.keySet());
                    Collections.sort(keys);

                    for (String key: keys) {
                        TextView textView = new TextView(MainActivity.this);
                        textView.setText(key + ": " + results.get(key) + "ms");
                        mLayout.addView(textView, mLayoutParams);
                    }

                }
            });

        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mLayout = (LinearLayout) findViewById(R.id.layout);
        mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        TextView textView = new TextView(MainActivity.this);
        textView.setText("Running tests...");
        mLayout.addView(textView, mLayoutParams);

        new Thread(mTestTask).start();
	}

    private void testImpl(JsonHandler testJson, Map<String, Long> results) {
    	try {
	        warmUp(testJson);
	        long duration = test(testJson, 1);
	        results.put("[1 run] " + testJson.getName(), duration);
	        duration = test(testJson, 5);
	        results.put("[5 runs] " + testJson.getName(), duration);
	        duration = test(testJson, 100);
	        results.put("[100 runs] " + testJson.getName(), duration);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    private void warmUp(final JsonHandler testJson) throws IOException {
        InputStream inputStream;
        for (int i = 0; i < 5; i++) {
            inputStream = getAssets().open(SAMPLE_JSON_FILE_NAME);
            testJson.parsePublicTimeline(inputStream);
        }
    }

    private long test(final JsonHandler testJson, int repeats) throws IOException {
        InputStream inputStream = getAssets().open(SAMPLE_JSON_FILE_NAME);

        List<Map<String, Object>> result = testJson.parsePublicTimeline(inputStream);
        verify(result);

        long duration = 0;

        for (int i = 0; i < repeats; i++) {
            inputStream = getAssets().open(SAMPLE_JSON_FILE_NAME);
            long start = System.currentTimeMillis();
            testJson.parsePublicTimeline(inputStream);
            duration += (System.currentTimeMillis() - start);
        }

        return duration;
    }

    private static void verify(List<Map<String, Object>> result) {
        if (result.size() != 20) {
            throw new IllegalStateException("Expected 20 but was " + result.size());
        }
        for (Map<String, Object> map: result) {
            if (map.size() != 52) {
                throw new IllegalStateException("Expected 52 but was " + result.size());
            }

        }
    }

}
