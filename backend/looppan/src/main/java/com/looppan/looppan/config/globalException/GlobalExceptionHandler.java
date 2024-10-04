package com.looppan.looppan.config.globalException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义异常
    // -------------------------------------------

    // 处理自定义异常
    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(MyException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    // 自带的异常
    // -------------------------------------------

    // 用户名或密码错误
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(BadCredentialsException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "用户名或密码错误",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 邮箱验证码发送失败
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(MailException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "邮箱验证码发送失败, 请检查邮箱是否正确",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // redis插入失败 或 mybatis plus 插入失败
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(DataAccessException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "邮箱验证码发送失败, 请稍后尝试!~",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

