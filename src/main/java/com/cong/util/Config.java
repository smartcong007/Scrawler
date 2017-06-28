package com.cong.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class Config {

    private static Map<String, String> selenium_config = new HashMap<String, String>();

    static {

        try {
            Properties properties = new Properties();
            InputStream in = ClassLoader.getSystemResourceAsStream("selenium.properties");
            properties.load(in);

            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {

                String key = (String) keys.nextElement();
                String val = properties.getProperty(key);
                selenium_config.put(key, val);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String getVal(String key) {

        return selenium_config.get(key);

    }

}
