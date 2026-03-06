package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单项实体类
 */
@Data
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "product_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal productPrice;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "product_snapshot")
    private String productSnapshot; // 商品快照信息
    
    // 使用Lombok @Data 自动生成构造函数
}