package com.looppan.looppan.controller.user.utils;


public enum StaticKey {

    // 图片验证码
    PIC_CHECK_CODE_KEY("pic_check_code_key"),
    PIC_CHECK_CODE_LENGTH (5),

    // 邮箱验证码
    EMAIL_CHECK_CODE_LENGTH(5),
    EMAIL_CHECK_CODE_VALID_TIME(5),

    // space
    ALL_SPACE(10),

    // Avatar
    AVATAR_URL("https://images.squarespace-cdn.com/content/v1/58ed33aeb8a79b05bed202aa/1496332556337-3976XQLXIFXJ3SBGPRAA/Loop-Logo-black.png"),
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
