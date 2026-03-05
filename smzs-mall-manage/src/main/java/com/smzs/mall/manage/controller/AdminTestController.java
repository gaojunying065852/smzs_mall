package com.smzs.mall.manage.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.AdminUser;
import com.smzs.mall.manage.service.AdminAuthServiceImpl;
import com.smzs.mall.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台测试控制器（仅用于开发测试）
 */
@RestController
@RequestMapping("/api/admin/test")
@CrossOrigin
public class AdminTestController {

    @Autowired
    private AdminAuthServiceImpl adminAuthService;
    
    @Autowired
    private AdminUserRepository adminUserRepository;

    /**
     * 初始化管理员账户（仅用于测试）
     * @return 初始化结果
     */
    @PostMapping("/init-admin")
    public Result<String> initAdmin() {
        try {
            // 检查是否已存在管理员
            if (adminUserRepository.existsByUsername("admin")) {
                return Result.success("管理员账户已存在");
            }
            
            // 创建初始管理员账户
            adminAuthService.createAdminUser("13800138000", "admin123", "系统管理员");
            return Result.success("管理员账户创建成功");
            
        } catch (Exception e) {
            return Result.error("初始化失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试登录接口
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login-test")
    public Result<Object> loginTest(@RequestBody java.util.Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            java.util.Map<String, Object> result = adminAuthService.adminLogin(username, password);
            return Result.success("测试登录成功", result);
            
        } catch (Exception e) {
            return Result.error("测试登录失败：" + e.getMessage());
        }
    }
}