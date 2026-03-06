package com.smzs.mall.manage.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.Order;
import com.smzs.mall.manage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 订单管理控制器
 */
@RestController
@RequestMapping("/api/admin/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 分页获取订单列表
     */
    @GetMapping
    public Result<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? 
            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.getOrders(pageable);
        return Result.success(orders);
    }
    
    /**
     * 根据ID获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    /**
     * 根据订单号获取订单
     */
    @GetMapping("/no/{orderNo}")
    public Result<Order> getOrderByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    /**
     * 根据客户ID获取订单列表
     */
    @GetMapping("/customer/{customerId}")
    public Result<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return Result.success(orders);
    }
    
    /**
     * 根据订单状态获取订单列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Order>> getOrdersByStatus(@PathVariable Integer status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return Result.success(orders);
    }
    
    /**
     * 创建订单
     */
    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return Result.success("订单创建成功", createdOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新订单信息
     */
    @PutMapping("/{id}")
    public Result<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return Result.success("订单更新成功", updatedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return Result.success("订单删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除订单
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteOrders(@RequestBody List<Long> ids) {
        try {
            orderService.batchDeleteOrders(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public Result<Order> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return Result.success("订单状态更新成功", updatedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 发货
     */
    @PutMapping("/{id}/ship")
    public Result<Order> shipOrder(@PathVariable Long id, @RequestParam String shippingNo) {
        try {
            Order shippedOrder = orderService.shipOrder(id, shippingNo);
            return Result.success("订单发货成功", shippedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 完成订单
     */
    @PutMapping("/{id}/complete")
    public Result<Order> completeOrder(@PathVariable Long id) {
        try {
            Order completedOrder = orderService.completeOrder(id);
            return Result.success("订单完成成功", completedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<Order> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id, reason);
            return Result.success("订单取消成功", cancelledOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据时间范围查询订单
     */
    @GetMapping("/time-range")
    public Result<List<Order>> getOrdersByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            
            List<Order> orders = orderService.getOrdersByTimeRange(start, end);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("时间格式错误：" + e.getMessage());
        }
    }
    
    /**
     * 获取订单统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> statistics = orderService.getOrderStatistics();
        return Result.success(statistics);
    }
    
    /**
     * 获取各状态订单数量统计
     */
    @GetMapping("/status-count")
    public Result<Map<Integer, Long>> getOrderStatusCount() {
        Map<Integer, Long> statusCount = orderService.countOrdersByStatus();
        return Result.success(statusCount);
    }
}