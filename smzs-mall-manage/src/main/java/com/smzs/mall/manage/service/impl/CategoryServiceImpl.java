package com.smzs.mall.manage.service.impl;

import com.smzs.mall.entity.Category;
import com.smzs.mall.manage.service.CategoryService;
import com.smzs.mall.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 分类管理服务实现类
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        return categoryRepository.findByStatusOrderBySortOrderAsc(1);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        // 这里需要自定义查询方法
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Category createCategory(Category category) {
        // 检查分类名称是否已存在
        if (category.getName() != null && existsByName(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 设置默认状态和排序
        if (category.getStatus() == null) {
            category.setStatus(1); // 默认启用
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0); // 默认排序
        }
        
        return categoryRepository.save(category);
    }
    
    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        if (existingCategory == null) {
            throw new RuntimeException("分类不存在");
        }
        
        // 检查名称是否重复（排除自己）
        if (category.getName() != null && !category.getName().equals(existingCategory.getName())) {
            if (existsByName(category.getName())) {
                throw new RuntimeException("分类名称已存在");
            }
            existingCategory.setName(category.getName());
        }
        
        // 更新其他字段
        if (category.getIcon() != null) {
            existingCategory.setIcon(category.getIcon());
        }
        if (category.getSortOrder() != null) {
            existingCategory.setSortOrder(category.getSortOrder());
        }
        if (category.getStatus() != null) {
            existingCategory.setStatus(category.getStatus());
        }
        
        existingCategory.setUpdater(1L); // TODO: 获取当前管理员ID
        existingCategory.setUpdateAt(LocalDateTime.now());
        
        return categoryRepository.save(existingCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        // 软删除
        category.setDelete(1);
        category.setUpdater(1L); // TODO: 获取当前管理员ID
        category.setUpdateAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    @Override
    public void batchDeleteCategories(List<Long> ids) {
        for (Long id : ids) {
            deleteCategory(id);
        }
    }
    
    @Override
    public void toggleCategoryStatus(Long id, Integer status) {
        Category category = getCategoryById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        category.setStatus(status);
        category.setUpdater(1L); // TODO: 获取当前管理员ID
        category.setUpdateAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    @Override
    public void updateCategorySort(Long id, Integer sortOrder) {
        Category category = getCategoryById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        category.setSortOrder(sortOrder);
        category.setUpdater(1L); // TODO: 获取当前管理员ID
        category.setUpdateAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    @Override
    public void batchUpdateCategorySort(List<Category> categories) {
        for (Category category : categories) {
            if (category.getId() != null) {
                updateCategorySort(category.getId(), category.getSortOrder());
            }
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().anyMatch(c -> name.equals(c.getName()));
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countCategories() {
        return categoryRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveCategories() {
        return categoryRepository.findByStatusOrderBySortOrderAsc(1).size();
    }
}