package com.smzs.mall.app.service;

import com.smzs.mall.entity.MiniProgramCustomer;
import com.smzs.mall.repository.MiniProgramCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private MiniProgramCustomerRepository customerRepository;
    
    /**
     * 微信code登录
     */
    public MiniProgramCustomer loginByWxCode(String code, String nickName, String avatarUrl) {
        // 这里应该调用微信code2Session接口获取openid
        // 暂时用code作为openid的简化处理
        String openId = "openid_" + code; // 模拟openid
        
        // 查找或创建用户
        MiniProgramCustomer user = customerRepository.findByOpenId(openId).orElse(null);
        if (user == null) {
            // 创建新用户
            user = new MiniProgramCustomer();
            user.setOpenId(openId);
            user.setStatus(1);
        }
        
        // 更新用户信息
        if (nickName != null && !nickName.isEmpty()) {
            user.setNickname(nickName);
        }
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            user.setAvatar(avatarUrl);
        }
        
        return customerRepository.save(user);
    }
    
    /**
     * 获取用户信息
     */
    public MiniProgramCustomer getUserInfo(Long userId) {
        return customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}