package com.looppan.looppan.controller.user.utils;

import java.util.List;

public class StaticKey {
    public static List<String> AVATAR_RANDOM;

    public final static int AVATAR_RANDOM_LENGTH = 10;

    public final static String MESSAGE_SUCCESS = "message : success";

    public final static String PIC_CHECK_CODE_KEY = "pic_check_code_key";

    public final static String EMAIL_CHECK_CODE_KEY = "email_check_code_key";

    public final static Integer CHECK_CODE_LENGTH = 5;

    public final static String USER_ID_KEY = "user_id_key";

    // 邮箱验证码是否可用
    public final static Integer EMAIL_CODE_OK = 1;
    public final static Integer EMAIL_CODE_FAIL = 0;

    // redis
    public final static String REDIS_EMAIL_TITLE_KEY = "redis_email_title_key";
    public final static String REDIS_EMAIL_CONTENT_KEY = "redis_email_content_key";
    public final static String REDIS_EMAIL_TITLE = "loop网盘 验证码";
    public final static String REDIS_EMAIL_CONTENT = "你的验证码是：%s\n";
}
