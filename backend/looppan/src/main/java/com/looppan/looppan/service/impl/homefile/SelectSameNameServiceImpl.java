package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.SelectSameNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelectSameNameServiceImpl implements SelectSameNameService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> selectSameName(String filePId, String fileName, Integer type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        List<FileInfo> fileInfos = null;
        fileName = "%" + fileName + "%";
        try {
            if (type == FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue()) {
                fileInfos = fileInfoMapper.selectByFileNameFolder(filePId, userId, fileName, FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue());
            } else {
                fileInfos = fileInfoMapper.selectByFileNameFile(userId, fileName, FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("搜索失败");
        }

        if (fileInfos == null || fileInfos.isEmpty()) {
            throw new MyException("不存在目标文件或文件夹");
        }

        Map<String, Object> mp = new HashMap<>();
        mp.put("message", "搜索成功");
        mp.put("data", fileInfos);

        return ResponseEntity.ok().body(mp);
    }
}
