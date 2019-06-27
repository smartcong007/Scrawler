package com.cong.common;

/**
 * @Description TODO
 * @Author zheng cong
 * @Date 2019-06-26
 */
public enum BrowerTypeEnum {

    /**
     * google浏览器
     */
    GOOGLE_CHROME("CHROME"),
    /**
     * phantomjs
     */
    PHANTOM_JS("PHANTOMJS");

    private String code;

    BrowerTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
