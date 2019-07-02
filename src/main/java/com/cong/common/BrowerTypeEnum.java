package com.cong.common;

/**
 * @Description TODO
 * @Author zheng cong
 * @Date 2019-07-02
 */
public enum BrowerTypeEnum {

    GOOGLE_CHROME("chrome"),

    PHANTOM_JS("phantom_js");

    private String code;

    public String getCode() {
        return code;
    }

    BrowerTypeEnum(String code) {
        this.code = code;
    }
}
