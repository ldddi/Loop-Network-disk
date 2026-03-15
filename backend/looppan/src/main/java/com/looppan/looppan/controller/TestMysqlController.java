package com.looppan.looppan.controller;

import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestMysqlController {

    private final FileInfoMapper fileInfoMapper;

    @GetMapping("/test/mysql")
    public List<FileInfo> testConnection() {
        return fileInfoMapper.selectList(null);
    }
}
