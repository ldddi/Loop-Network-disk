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


        return mp;
    }

}
