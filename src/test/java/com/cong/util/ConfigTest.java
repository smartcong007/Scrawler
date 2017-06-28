package com.cong.util;

import org.junit.Test;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class ConfigTest {

    @Test
    public void test() {

        String val = Config.getVal("CHROME_PATH");
        System.out.println(val);

    }

}
