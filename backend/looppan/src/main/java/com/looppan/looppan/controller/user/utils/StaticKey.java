package com.looppan.looppan.controller.user.utils;


public enum StaticKey {

    // 图片验证码
    PIC_CHECK_CODE_KEY("pic_check_code_key"),
    CHECK_CODE_LENGTH (5),

    // 邮箱验证码是否可用
    EMAIL_CODE_OK(1),
    EMAIL_CODE_FAIL(0);

    private final String stringValue;
    private final Integer integerValue;


    StaticKey(String stringValue) {
        this.stringValue = stringValue;
        this.integerValue = null;
    }

    StaticKey(Integer integerValue) {
        this.integerValue = integerValue;
        this.stringValue = null;
    }

    public String toStringValue() {
        return stringValue;
    }
    public Integer toIntegerValue() {
        return integerValue;
    }
}
