package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.JwtUtil;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.GetUserInfoService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Service
public class GetUserInfoServiceImpl implements GetUserInfoService {
    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseEntity<Map> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Map<String, String> mp = new HashMap<>();
        mp.put("nickName", user.getNickName());
        mp.put("avatar", user.getAvatar());
        mp.put("totalSpace", String.valueOf(user.getTotalSpace()));
        mp.put("useSpace", String.valueOf(user.getUseSpace()));
        return ResponseEntity.ok().body(mp);
    }
}
