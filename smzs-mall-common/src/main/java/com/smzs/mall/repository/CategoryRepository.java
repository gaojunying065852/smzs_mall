package com.smzs.mall.repository;

import com.smzs.mall.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 查找启用的分类，按排序字段升序排列
     */
    List<Category> findByStatusOrderBySortOrderAsc(Integer status);
}