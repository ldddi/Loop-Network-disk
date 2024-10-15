package com.looppan.looppan.controller.homefile.utils;

public enum FileStaticKey {
    FILE_ID_LENGTH(12),
    FOLDER_TYPE_FOLDER(1),
    FOLDER_TYPE_FILE(0),
    FILE_CATEGORY_FOLDER(0),
    FILE_CATEGORY_VIDEO(1),
    FILE_CATEGORY_AUDIO(2),
    FILE_CATEGORY_IMAGE(3),
    FILE_CATEGORY_DOC(4),
    FILE_CATEGORY_OTHER(5),
    DEL_FLAG_DELETE(0),
    DEL_FLAG_RECOVERY(1),
    DEL_FLAG_NORMAL(2),
    LENGTH_SHARE_CODE(5);

    final Integer integerValue;
    final String stringValue;

    FileStaticKey(Integer integerValue) {
        this.integerValue = integerValue;
        this.stringValue = null;
    }

    FileStaticKey(String stringValue) {
        this.stringValue = stringValue;
        this.integerValue = null;
    }

    public String toStringValue() {
        return stringValue;
    }

    public Integer toIntegerValue() {
        return integerValue;
    }
}
