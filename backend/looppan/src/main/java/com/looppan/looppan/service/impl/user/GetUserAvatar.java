package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetUserAvatar {

    @GetMapping("/getUserAvatar")
    public ResponseEntity<Map> getUserAvatar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Map<String, String> mp = new HashMap<>();
        mp.put("avatar", user.getAvatar());
        return ResponseEntity.ok().body(mp);
    }
}
