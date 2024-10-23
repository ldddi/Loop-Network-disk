package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.GetUserUseSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetUserUseSpaceServiceImpl implements GetUserUseSpaceService {

    @Override
    public ResponseEntity<Map> getUserUseSpace() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        Map<String, String> mp = new HashMap<>();
        mp.put("data", String.valueOf(user.getUseSpace()));
        return ResponseEntity.ok().body(mp);
    }
}
