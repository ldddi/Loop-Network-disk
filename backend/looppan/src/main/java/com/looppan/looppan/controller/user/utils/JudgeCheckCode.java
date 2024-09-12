package com.looppan.looppan.controller.user.utils;

import com.looppan.looppan.mapper.EmailMapper;
import com.looppan.looppan.pojo.Email;
import jakarta.servlet.http.HttpSession;

import java.util.Objects;

public class JudgeCheckCode {

    public static boolean isOkPicCheckCode(String picCode, HttpSession session) {
        String code = (String) session.getAttribute(StaticKey.PIC_CHECK_CODE_KEY);
        if (picCode == null || !picCode.equalsIgnoreCase(code)) {
            return false;
        }
        return true;
    };

    public static boolean isOkEmailCheckCode(EmailMapper emailMapper, String email, String emailCode) {
        Email searchedEmail = emailMapper.getCodeByEmail(email);
        if (emailCode == null || Objects.equals(searchedEmail.getStatus(), StaticKey.EMAIL_CODE_FAIL) || !emailCode.trim().equalsIgnoreCase(searchedEmail.getCode())) {
            return false;
        }
        return true;
    };
}
