package com.smzs.mall.service;

import com.smzs.mall.entity.Category;
import com.smzs.mall.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 获取分类列表
     */
    public List<Category> getCategoryList() {
        return categoryRepository.findByStatusOrderBySortOrderAsc(1);
    }
}