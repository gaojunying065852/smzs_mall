package com.smzs.mall.repository;

import com.smzs.mall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单号查找订单
     */
    Optional<Order> findByOrderNo(String orderNo);
    
    /**
     * 根据客户ID查找订单
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * 根据订单状态查找订单
     */
    List<Order> findByOrderStatus(Integer orderStatus);
    
    /**
     * 根据支付状态查找订单
     */
    List<Order> findByPayStatus(Integer payStatus);
    
    /**
     * 检查订单号是否存在
     */
    boolean existsByOrderNo(String orderNo);
    
    /**
     * 根据时间范围查找订单
     */
    @Query("SELECT o FROM Order o WHERE o.createAt BETWEEN ?1 AND ?2")
    List<Order> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计订单总数
     */
    long countByOrderStatus(Integer orderStatus);
    
    /**
     * 根据客户ID和订单状态查找订单
     */
    List<Order> findByCustomerIdAndOrderStatus(Long customerId, Integer orderStatus);
}