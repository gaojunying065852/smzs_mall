package com.smzs.mall.app.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.MiniProgramCustomer;
import com.smzs.mall.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public Result<MiniProgramCustomer> getProfile(HttpServletRequest request) {
        // 从请求头中获取用户ID（简化处理）
        String userIdStr = request.getHeader("User-ID");
        if (userIdStr == null) {
            return Result.error("未登录");
        }
        
        try {
            Long userId = Long.parseLong(userIdStr);
            MiniProgramCustomer user = authService.getUserInfo(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }
}