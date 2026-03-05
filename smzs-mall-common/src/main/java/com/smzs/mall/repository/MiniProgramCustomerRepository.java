package com.smzs.mall.repository;

import com.smzs.mall.entity.MiniProgramCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MiniProgramCustomerRepository extends JpaRepository<MiniProgramCustomer, Long> {
    
    /**
     * 根据openId查找客户
     */
    Optional<MiniProgramCustomer> findByOpenId(String openId);
    
    /**
     * 根据手机号查找客户
     */
    Optional<MiniProgramCustomer> findByPhone(String phone);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 检查openId是否存在
     */
    boolean existsByOpenId(String openId);
}