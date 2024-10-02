package com.looppan.looppan.controller.homefile.utils;

import lombok.Getter;

public enum FileCategoryEnums {
    VIDEO(1, "video", "视频"),
    MUSIC(2, "audio", "音频"),
    IMAGE(3, "image", "图片"),
    DOC(4, "doc", "文档"),
    OTHERS(5, "others", "其他");

    @Getter
    private Integer code;
    private final String categoryName;
    private final String desc;

    FileCategoryEnums(Integer code, String categoryName, String desc) {
        this.code = code;
        this.categoryName = categoryName;
        this.desc = desc;
    }

    public static FileCategoryEnums getByCategoryName(String categoryName) {
        for (FileCategoryEnums fileCategoryEnums : FileCategoryEnums.values()) {
            if (fileCategoryEnums.categoryName.equals(categoryName)) {
                return fileCategoryEnums;
            }
        }
        return null;
    }
}
