package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 小程序客户实体
 * 专门用于微信小程序用户管理
 */
@Data
@Entity
@Table(name = "app_customer")
public class MiniProgramCustomer extends BaseEntity {
    @Column(unique = true)
    private String phone;
    
    @Column(name = "open_id", unique = true)
    private String openId;

    private String nickname;

    private String avatar;

    private Integer gender;

    private LocalDateTime birthday;

    private Integer status;

    // 使用Lombok @Data 自动生成构造函数
}