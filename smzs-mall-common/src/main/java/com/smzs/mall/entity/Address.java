package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "is_default")
    private Boolean isDefault;

    // 使用Lombok @Data 自动生成构造函数
}