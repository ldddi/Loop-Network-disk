package com.looppan.looppan.service.impl.user;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.JwtUtil;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseEntity<Map<String, String>> login(
            String email, String password, String picCheckCode, HttpSession session)
    {
        String value = redisTemplate.opsForValue().get(session.getId());
        if (value != null) {
            redisTemplate.delete(session.getId());
        }

        if (value == null || !value.equalsIgnoreCase(picCheckCode)) {
            throw new MyException("图片验证码错误");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        User user = userDetails.getUser();

        String jwt = JwtUtil.createJWT(user.getUserId());
        Map<String, String> mp = new HashMap<>();

        mp.put("userId", user.getUserId());
        mp.put("nickName", user.getNickName());
        mp.put("email", user.getEmail());
        mp.put("totalSpace", String.valueOf(user.getTotalSpace()));
        mp.put("useSpace", String.valueOf(user.getUseSpace()));
        mp.put("avatar", user.getAvatar());
        mp.put("token", jwt);
        mp.put("message", "登陆成功");
        return ResponseEntity.ok().body(mp);
    }
}
