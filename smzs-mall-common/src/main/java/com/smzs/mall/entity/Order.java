package com.smzs.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 */
@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private MiniProgramCustomer customer;
    
    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo;
    
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "pay_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal payAmount;
    
    @Column(name = "pay_type")
    private Integer payType; // 1微信支付 2支付宝 3银行卡
    
    @Column(name = "pay_status", nullable = false)
    private Integer payStatus; // 0未支付 1已支付 2已退款
    
    @Column(name = "order_status", nullable = false)
    private Integer orderStatus; // 1待付款 2待发货 3待收货 4已完成 5已取消
    
    @Column(name = "receiver_name")
    private String receiverName;
    
    @Column(name = "receiver_phone")
    private String receiverPhone;
    
    @Column(name = "receiver_address")
    private String receiverAddress;
    
    @Column(name = "remark")
    private String remark;
    
    @Column(name = "pay_time")
    private LocalDateTime payTime;
    
    @Column(name = "ship_time")
    private LocalDateTime shipTime;
    
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    
    // 使用Lombok @Data 自动生成构造函数
}