package com.smzs.mall.repository;

import com.smzs.mall.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    
    /**
     * 根据用户名查找管理员
     */
    Optional<AdminUser> findByUsername(String username);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 根据状态查找管理员
     */
    java.util.List<AdminUser> findByStatus(Integer status);
}