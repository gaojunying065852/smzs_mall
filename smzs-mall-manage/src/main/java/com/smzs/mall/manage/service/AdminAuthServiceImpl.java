package com.smzs.mall.manage.service;

import com.smzs.mall.entity.AdminUser;
import com.smzs.mall.repository.AdminUserRepository;
import com.smzs.mall.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 管理后台专用认证服务
 */
@Service
public class AdminAuthServiceImpl {

    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public Map<String, Object> adminLogin(String username, String password) {
        // 查找管理员用户
        Optional<AdminUser> userOpt = adminUserRepository.findByUsername(username);
        
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        AdminUser user = userOpt.get();
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 验证账户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new RuntimeException("账户已被禁用");
        }
        
        // 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        adminUserRepository.save(user);
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), username);
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        
        return result;
    }
    
    /**
     * 创建初始管理员账户
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     */
    public void createAdminUser(String username, String password, String nickname) {
        // 检查用户是否已存在
        if (adminUserRepository.existsByUsername(username)) {
            throw new RuntimeException("该用户名已被注册");
        }
        
        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setNickname(nickname);
        admin.setRole("admin");
        admin.setStatus(1);
        
        adminUserRepository.save(admin);
    }
    
    /**
     * 验证token有效性
     * @param token JWT token
     * @return 用户信息
     */
    public AdminUser validateToken(String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            return adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } catch (Exception e) {
            throw new RuntimeException("token无效");
        }
    }
}