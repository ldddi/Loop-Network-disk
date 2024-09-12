package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UpdateUserAvatar {
    @RequestMapping(value = "/updateUserAvatar", method = RequestMethod.POST)
    public Map<String, String> updateUserAvatar(

            @RequestParam("avatar") MultipartFile file
    ) throws IOException {
        Map<String, String> mp = new HashMap<>();

        if (file.isEmpty()) {
            mp.put("message", "文件不能为空");
            return mp;
        }

        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpg")) {
            mp.put("message", "仅支持 .jpg 文件");
            return mp;
        }

        ClassPathResource resource = new ClassPathResource("static/images/user");

        File dir = resource.getFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
        String random = null;
        for (int i = 0; i < 10; i ++ ) {
            random = RandomUtils.generateRandomString(StaticKey.AVATAR_RANDOM_LENGTH);
            if (!StaticKey.AVATAR_RANDOM.contains(random)) {
                StaticKey.AVATAR_RANDOM.add(random);
                break;
            }
        }

        String filePath = dir.getAbsolutePath() + random + ".jpg";
        File file1 = new File(filePath);

        file.transferTo(file1);

        mp.put("message", "success");
        return mp;
    }

}
