package com.looppan.looppan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.looppan.looppan.pojo.Email;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService extends IService<Email> {

    public void sendEmailCode(String email, Integer type) throws MessagingException;


}
