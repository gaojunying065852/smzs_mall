package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 基础实体类，包含所有表的公共字段
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creater")
    private Long creater;

    @Column(name = "updater")
    private Long updater;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "delete")
    private Integer delete;

    // Lifecycle callbacks 保持不变

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        if (this.delete == null) {
            this.delete = 0;
        }
    }
}