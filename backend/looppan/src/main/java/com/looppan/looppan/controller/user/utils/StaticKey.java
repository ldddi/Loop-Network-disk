package com.looppan.looppan.controller.user.utils;


public enum StaticKey {

    // 图片验证码
    PIC_CHECK_CODE_KEY("pic_check_code_key"),
    PIC_CHECK_CODE_LENGTH (5),

    // 邮箱验证码
    EMAIL_CHECK_CODE_LENGTH(5),
    EMAIL_CHECK_CODE_VALID_TIME(5),

    // space
    ALL_SPACE(524288000),

    // Avatar
    AVATAR_UUID_LENGTH(10);

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
