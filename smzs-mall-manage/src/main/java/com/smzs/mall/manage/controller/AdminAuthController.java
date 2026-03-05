package com.smzs.mall.manage.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.AdminUser;
import com.smzs.mall.manage.service.AdminAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 管理后台认证控制器
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminAuthController {

    @Autowired
    private AdminAuthServiceImpl adminAuthService;

    /**
     * 管理员登录
     * @param request 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            if (username == null || username.isEmpty()) {
                return Result.error("用户名不能为空");
            }
            
            if (password == null || password.isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            Map<String, Object> result = adminAuthService.adminLogin(username, password);
            return Result.success("登录成功", result);
            
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前管理员信息
     * @param request HTTP请求
     * @return 管理员信息
     */
    @GetMapping("/profile")
    public Result<AdminUser> getProfile(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return Result.error("未登录");
            }
            
            AdminUser user = adminAuthService.validateToken(token);
            return Result.success(user);
            
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<Object> logout() {
        // JWT是无状态的，客户端清除token即可
        return Result.success("退出成功", null);
    }
    
    /**
     * 从请求头中提取token
     * @param request HTTP请求
     * @return token字符串
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}