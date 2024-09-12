package com.looppan.looppan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.service.FileInfoService;
import org.springframework.stereotype.Service;

@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

}
