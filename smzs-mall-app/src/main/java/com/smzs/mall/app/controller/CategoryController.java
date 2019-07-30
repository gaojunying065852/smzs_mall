package com.smzs.mall.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.Category;
import com.smzs.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 分类列表
     */
    @GetMapping
    public Result<List<Category>> getCategoryList() {
        try {
            List<Category> categories = categoryService.getCategoryList();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error("获取分类列表失败：" + e.getMessage());
        }
    }
}