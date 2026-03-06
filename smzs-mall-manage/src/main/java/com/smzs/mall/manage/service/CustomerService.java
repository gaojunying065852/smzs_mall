package com.smzs.mall.manage.service;

import com.smzs.mall.entity.MiniProgramCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 客户管理服务接口
 */
public interface CustomerService {
    
    /**
     * 分页查询客户列表
     */
    Page<MiniProgramCustomer> getCustomers(Pageable pageable);
    
    /**
     * 根据ID获取客户详情
     */
    MiniProgramCustomer getCustomerById(Long id);
    
    /**
     * 根据手机号查询客户
     */
    MiniProgramCustomer getCustomerByPhone(String phone);
    
    /**
     * 根据openId查询客户
     */
    MiniProgramCustomer getCustomerByOpenId(String openId);
    
    /**
     * 创建客户
     */
    MiniProgramCustomer createCustomer(MiniProgramCustomer customer);
    
    /**
     * 更新客户信息
     */
    MiniProgramCustomer updateCustomer(Long id, MiniProgramCustomer customer);
    
    /**
     * 删除客户（软删除）
     */
    void deleteCustomer(Long id);
    
    /**
     * 批量删除客户
     */
    void batchDeleteCustomers(List<Long> ids);
    
    /**
     * 启用/禁用客户
     */
    void toggleCustomerStatus(Long id, Integer status);
    
    /**
     * 统计客户总数
     */
    long countCustomers();
    
    /**
     * 统计活跃客户数
     */
    long countActiveCustomers();
}