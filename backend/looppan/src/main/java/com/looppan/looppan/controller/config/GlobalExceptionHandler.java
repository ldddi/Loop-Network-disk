package com.looppan.looppan.controller.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<MyErrorMessage> handleBusinessException(MyException ex) {
        // 构建错误响应
        MyErrorMessage errorResponse = new MyErrorMessage(ex.getMessage());
        // 返回包含错误信息的响应实体
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 可以添加其他异常处理方法
}
