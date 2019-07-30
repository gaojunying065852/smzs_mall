package com.smzs.mall.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    
    /**
     * 发送短信验证码
     * 实际项目中应该集成真实的短信服务商SDK
     */
    public boolean sendSms(String phone, String code) {
        // 模拟发送短信
        System.out.println("【商盟助手】您的验证码是：" + code + "，5分钟内有效。");
        return true;
    }
}