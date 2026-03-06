package com.smzs.mall.manage.service.impl;

import com.smzs.mall.entity.Order;
import com.smzs.mall.manage.service.OrderService;
import com.smzs.mall.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订单管理服务实现类
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByOrderNo(String orderNo) {
        Optional<Order> order = orderRepository.findByOrderNo(orderNo);
        return order.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(Integer orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus);
    }
    
    @Override
    public Order createOrder(Order order) {
        // 检查订单号是否已存在
        if (order.getOrderNo() != null && orderRepository.existsByOrderNo(order.getOrderNo())) {
            throw new RuntimeException("订单号已存在");
        }
        
        // 设置默认状态
        if (order.getOrderStatus() == null) {
            order.setOrderStatus(1); // 待付款
        }
        if (order.getPayStatus() == null) {
            order.setPayStatus(0); // 未支付
        }
        
        return orderRepository.save(order);
    }
    
    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);
        if (existingOrder == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 更新基本信息
        if (order.getReceiverName() != null) {
            existingOrder.setReceiverName(order.getReceiverName());
        }
        if (order.getReceiverPhone() != null) {
            existingOrder.setReceiverPhone(order.getReceiverPhone());
        }
        if (order.getReceiverAddress() != null) {
            existingOrder.setReceiverAddress(order.getReceiverAddress());
        }
        if (order.getRemark() != null) {
            existingOrder.setRemark(order.getRemark());
        }
        
        existingOrder.setUpdater(1L); // TODO: 获取当前管理员ID
        existingOrder.setUpdateAt(LocalDateTime.now());
        
        return orderRepository.save(existingOrder);
    }
    
    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 软删除
        order.setDelete(1);
        order.setUpdater(1L); // TODO: 获取当前管理员ID
        order.setUpdateAt(LocalDateTime.now());
        orderRepository.save(order);
    }
    
    @Override
    public void batchDeleteOrders(List<Long> ids) {
        for (Long id : ids) {
            deleteOrder(id);
        }
    }
    
    @Override
    public Order updateOrderStatus(Long id, Integer orderStatus) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        order.setOrderStatus(orderStatus);
        order.setUpdater(1L); // TODO: 获取当前管理员ID
        order.setUpdateAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    @Override
    public Order shipOrder(Long id, String shippingNo) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getOrderStatus() != 2) { // 待发货
            throw new RuntimeException("订单状态不允许发货");
        }
        
        order.setOrderStatus(3); // 待收货
        order.setShipTime(LocalDateTime.now());
        // 这里可以添加物流单号字段
        order.setUpdater(1L); // TODO: 获取当前管理员ID
        order.setUpdateAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Override
    public Order completeOrder(Long id) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getOrderStatus() != 3) { // 待收货
            throw new RuntimeException("订单状态不允许完成");
        }
        
        order.setOrderStatus(4); // 已完成
        order.setReceiveTime(LocalDateTime.now());
        order.setUpdater(1L); // TODO: 获取当前管理员ID
        order.setUpdateAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Override
    public Order cancelOrder(Long id, String reason) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getOrderStatus() == 4 || order.getOrderStatus() == 5) { // 已完成或已取消
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setOrderStatus(5); // 已取消
        order.setRemark(reason);
        order.setUpdater(1L); // TODO: 获取当前管理员ID
        order.setUpdateAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.findByCreateTimeBetween(startTime, endTime);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getOrderStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", orderRepository.count());
        statistics.put("pendingPayment", orderRepository.countByOrderStatus(1)); // 待付款
        statistics.put("pendingShipment", orderRepository.countByOrderStatus(2)); // 待发货
        statistics.put("pendingReceipt", orderRepository.countByOrderStatus(3)); // 待收货
        statistics.put("completed", orderRepository.countByOrderStatus(4)); // 已完成
        statistics.put("cancelled", orderRepository.countByOrderStatus(5)); // 已取消
        return statistics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countOrdersByStatus() {
        Map<Integer, Long> statusCount = new HashMap<>();
        statusCount.put(1, orderRepository.countByOrderStatus(1)); // 待付款
        statusCount.put(2, orderRepository.countByOrderStatus(2)); // 待发货
        statusCount.put(3, orderRepository.countByOrderStatus(3)); // 待收货
        statusCount.put(4, orderRepository.countByOrderStatus(4)); // 已完成
        statusCount.put(5, orderRepository.countByOrderStatus(5)); // 已取消
        return statusCount;
    }
}