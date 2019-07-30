package com.smzs.mall.app.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.User;
import com.smzs.mall.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 微信授权登录
     */
    @PostMapping("/wx-login")
    public Result<Map<String, Object>> wxLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String nickName = request.get("nickName");
        String avatarUrl = request.get("avatarUrl");
        
        if (code == null || code.isEmpty()) {
            return Result.error("code不能为空");
        }
        
        try {
            User user = authService.loginByWxCode(code, nickName, avatarUrl);
            
            // 生成token（简化处理，实际应该使用JWT）
            String token = "Bearer " + System.currentTimeMillis(); // 模拟token
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}