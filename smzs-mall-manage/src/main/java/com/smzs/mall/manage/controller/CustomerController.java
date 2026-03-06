package com.smzs.mall.manage.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.MiniProgramCustomer;
import com.smzs.mall.manage.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/api/admin/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * 分页获取客户列表
     */
    @GetMapping
    public Result<Page<MiniProgramCustomer>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? 
            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MiniProgramCustomer> customers = customerService.getCustomers(pageable);
        return Result.success(customers);
    }
    
    /**
     * 根据ID获取客户详情
     */
    @GetMapping("/{id}")
    public Result<MiniProgramCustomer> getCustomerById(@PathVariable Long id) {
        MiniProgramCustomer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }
    
    /**
     * 根据手机号获取客户
     */
    @GetMapping("/phone/{phone}")
    public Result<MiniProgramCustomer> getCustomerByPhone(@PathVariable String phone) {
        MiniProgramCustomer customer = customerService.getCustomerByPhone(phone);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }
    
    /**
     * 根据openId获取客户
     */
    @GetMapping("/openid/{openId}")
    public Result<MiniProgramCustomer> getCustomerByOpenId(@PathVariable String openId) {
        MiniProgramCustomer customer = customerService.getCustomerByOpenId(openId);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }
    
    /**
     * 创建客户
     */
    @PostMapping
    public Result<MiniProgramCustomer> createCustomer(@RequestBody MiniProgramCustomer customer) {
        try {
            MiniProgramCustomer createdCustomer = customerService.createCustomer(customer);
            return Result.success("客户创建成功", createdCustomer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新客户信息
     */
    @PutMapping("/{id}")
    public Result<MiniProgramCustomer> updateCustomer(@PathVariable Long id, 
                                                     @RequestBody MiniProgramCustomer customer) {
        try {
            MiniProgramCustomer updatedCustomer = customerService.updateCustomer(id, customer);
            return Result.success("客户更新成功", updatedCustomer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return Result.success("客户删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除客户
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteCustomers(@RequestBody List<Long> ids) {
        try {
            customerService.batchDeleteCustomers(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 启用/禁用客户
     */
    @PutMapping("/{id}/status")
    public Result<String> toggleCustomerStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            customerService.toggleCustomerStatus(id, status);
            return Result.success("状态更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取客户统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getCustomerStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", customerService.countCustomers());
        statistics.put("active", customerService.countActiveCustomers());
        return Result.success(statistics);
    }
}