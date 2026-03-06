package com.smzs.mall.manage.service;

import com.smzs.mall.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单管理服务接口
 */
public interface OrderService {
    
    /**
     * 分页查询订单列表
     */
    Page<Order> getOrders(Pageable pageable);
    
    /**
     * 根据ID获取订单详情
     */
    Order getOrderById(Long id);
    
    /**
     * 根据订单号获取订单
     */
    Order getOrderByOrderNo(String orderNo);
    
    /**
     * 根据客户ID查询订单
     */
    List<Order> getOrdersByCustomerId(Long customerId);
    
    /**
     * 根据订单状态查询订单
     */
    List<Order> getOrdersByStatus(Integer orderStatus);
    
    /**
     * 创建订单
     */
    Order createOrder(Order order);
    
    /**
     * 更新订单信息
     */
    Order updateOrder(Long id, Order order);
    
    /**
     * 删除订单（软删除）
     */
    void deleteOrder(Long id);
    
    /**
     * 批量删除订单
     */
    void batchDeleteOrders(List<Long> ids);
    
    /**
     * 更新订单状态
     */
    Order updateOrderStatus(Long id, Integer orderStatus);
    
    /**
     * 发货
     */
    Order shipOrder(Long id, String shippingNo);
    
    /**
     * 完成订单
     */
    Order completeOrder(Long id);
    
    /**
     * 取消订单
     */
    Order cancelOrder(Long id, String reason);
    
    /**
     * 根据时间范围查询订单
     */
    List<Order> getOrdersByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取订单统计信息
     */
    Map<String, Object> getOrderStatistics();
    
    /**
     * 统计各状态订单数量
     */
    Map<Integer, Long> countOrdersByStatus();
}