package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true)
    private String phone;
    
    @Column(name = "open_id", unique = true)
    private String openId;

    private String nickname;

    private String avatar;

    private Integer gender;

    private java.time.LocalDateTime birthday;

    private Integer status;

    // 使用Lombok @Data 自动生成构造函数
}