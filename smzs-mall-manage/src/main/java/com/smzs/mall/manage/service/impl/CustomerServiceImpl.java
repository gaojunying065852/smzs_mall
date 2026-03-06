package com.smzs.mall.manage.service.impl;

import com.smzs.mall.entity.MiniProgramCustomer;
import com.smzs.mall.manage.service.CustomerService;
import com.smzs.mall.repository.MiniProgramCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 客户管理服务实现类
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private MiniProgramCustomerRepository customerRepository;
    
    @Override
    public Page<MiniProgramCustomer> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MiniProgramCustomer getCustomerById(Long id) {
        Optional<MiniProgramCustomer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MiniProgramCustomer getCustomerByPhone(String phone) {
        Optional<MiniProgramCustomer> customer = customerRepository.findByPhone(phone);
        return customer.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MiniProgramCustomer getCustomerByOpenId(String openId) {
        Optional<MiniProgramCustomer> customer = customerRepository.findByOpenId(openId);
        return customer.orElse(null);
    }
    
    @Override
    public MiniProgramCustomer createCustomer(MiniProgramCustomer customer) {
        // 检查手机号是否已存在
        if (customer.getPhone() != null && customerRepository.existsByPhone(customer.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 检查openId是否已存在
        if (customer.getOpenId() != null && customerRepository.existsByOpenId(customer.getOpenId())) {
            throw new RuntimeException("openId已存在");
        }
        
        // 设置默认状态
        if (customer.getStatus() == null) {
            customer.setStatus(1); // 默认启用
        }
        
        return customerRepository.save(customer);
    }
    
    @Override
    public MiniProgramCustomer updateCustomer(Long id, MiniProgramCustomer customer) {
        MiniProgramCustomer existingCustomer = getCustomerById(id);
        if (existingCustomer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        // 更新基本信息
        if (customer.getNickname() != null) {
            existingCustomer.setNickname(customer.getNickname());
        }
        if (customer.getAvatar() != null) {
            existingCustomer.setAvatar(customer.getAvatar());
        }
        if (customer.getGender() != null) {
            existingCustomer.setGender(customer.getGender());
        }
        if (customer.getBirthday() != null) {
            existingCustomer.setBirthday(customer.getBirthday());
        }
        if (customer.getStatus() != null) {
            existingCustomer.setStatus(customer.getStatus());
        }
        
        existingCustomer.setUpdater(1L); // TODO: 获取当前管理员ID
        existingCustomer.setUpdateAt(LocalDateTime.now());
        
        return customerRepository.save(existingCustomer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        MiniProgramCustomer customer = getCustomerById(id);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        // 软删除
        customer.setDelete(1);
        customer.setUpdater(1L); // TODO: 获取当前管理员ID
        customer.setUpdateAt(LocalDateTime.now());
        customerRepository.save(customer);
    }
    
    @Override
    public void batchDeleteCustomers(List<Long> ids) {
        for (Long id : ids) {
            deleteCustomer(id);
        }
    }
    
    @Override
    public void toggleCustomerStatus(Long id, Integer status) {
        MiniProgramCustomer customer = getCustomerById(id);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        customer.setStatus(status);
        customer.setUpdater(1L); // TODO: 获取当前管理员ID
        customer.setUpdateAt(LocalDateTime.now());
        customerRepository.save(customer);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countCustomers() {
        return customerRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveCustomers() {
        // 这里需要自定义查询，暂时返回总数量
        return customerRepository.count();
    }
}