package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 管理后台用户实体
 * 专门用于管理后台登录认证
 */
@Data
@Entity
@Table(name = "admin_user")
public class AdminUser extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String username;  // 登录用户名（通常是手机号）
    
    @Column(nullable = false)
    private String password;  // 加密后的密码
    
    @Column(nullable = false)
    private String nickname;  // 显示昵称
    
    private String avatar;    // 头像URL
    
    @Column(nullable = false)
    private String role;      // 角色：admin/super_admin
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;  // 最后登录时间
    
    private Integer status;   // 状态：0禁用，1正常，2锁定
    
    // 使用Lombok @Data 自动生成构造函数
}