package com.looppan.looppan.config.globalException;

import com.looppan.looppan.controller.TestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
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
        logger.error("An error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "redis插入失败 或 mybatis plus插入失败",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "文件体积过大",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

