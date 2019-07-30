package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder;

    private Integer status;

    // 使用Lombok @Data 自动生成构造函数
}