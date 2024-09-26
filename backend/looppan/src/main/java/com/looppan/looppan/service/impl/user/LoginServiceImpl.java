package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.JwtUtil;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Map<String, String>> login(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        User user = userDetails.getUser();

        String jwt = JwtUtil.createJWT(user.getUserId());
        Map<String, String> mp = new HashMap<>();

        mp.put("message", "success");
        mp.put("data", jwt);

        return ResponseEntity.ok(mp);
    }
}
