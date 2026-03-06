package com.smzs.mall.manage.service;

import com.smzs.mall.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 分类管理服务接口
 */
public interface CategoryService {
    
    /**
     * 分页查询分类列表
     */
    Page<Category> getCategories(Pageable pageable);
    
    /**
     * 获取所有分类（不分页）
     */
    List<Category> getAllCategories();
    
    /**
     * 获取启用的分类列表
     */
    List<Category> getActiveCategories();
    
    /**
     * 根据ID获取分类详情
     */
    Category getCategoryById(Long id);
    
    /**
     * 根据名称查找分类
     */
    Category getCategoryByName(String name);
    
    /**
     * 创建分类
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类信息
     */
    Category updateCategory(Long id, Category category);
    
    /**
     * 删除分类（软删除）
     */
    void deleteCategory(Long id);
    
    /**
     * 批量删除分类
     */
    void batchDeleteCategories(List<Long> ids);
    
    /**
     * 启用/禁用分类
     */
    void toggleCategoryStatus(Long id, Integer status);
    
    /**
     * 更新分类排序
     */
    void updateCategorySort(Long id, Integer sortOrder);
    
    /**
     * 批量更新分类排序
     */
    void batchUpdateCategorySort(List<Category> categories);
    
    /**
     * 检查分类名称是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 统计分类总数
     */
    long countCategories();
    
    /**
     * 统计启用的分类数
     */
    long countActiveCategories();
}