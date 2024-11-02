package com.looppan.looppan;

import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

@SpringBootTest
class LooppanApplicationTests {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileShareMapper fileShareMapper;

    @Test
    void contextLoads() {


    }

}
