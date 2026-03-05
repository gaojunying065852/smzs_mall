package com.smzs.mall.repository;

import com.smzs.mall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据分类ID查找商品
     */
    Page<Product> findByCategoryIdAndStatus(Long categoryId, Integer status, Pageable pageable);
    
    /**
     * 查找所有上架商品
     */
    Page<Product> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 查找推荐商品
     */
    @Query("SELECT p FROM Product p WHERE p.isRecommend = true AND p.status = 1 ORDER BY p.createAt DESC")
    List<Product> findRecommendProducts();
    
    /**
     * 查找热门商品
     */
    @Query("SELECT p FROM Product p WHERE p.isHot = true AND p.status = 1 ORDER BY p.createAt DESC")
    List<Product> findHotProducts();
    
    /**
     * 根据状态查找商品
     */
    List<Product> findByStatusOrderByCreateAtDesc(Integer status);
}