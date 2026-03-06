package com.smzs.mall.manage.controller;

import com.smzs.mall.common.Result;
import com.smzs.mall.entity.Category;
import com.smzs.mall.manage.service.CategoryService;
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
 * 分类管理控制器
 */
@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 分页获取分类列表
     */
    @GetMapping
    public Result<Page<Category>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Category> categories = categoryService.getCategories(pageable);
        return Result.success(categories);
    }
    
    /**
     * 获取所有分类（不分页）
     */
    @GetMapping("/all")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    /**
     * 获取启用的分类列表
     */
    @GetMapping("/active")
    public Result<List<Category>> getActiveCategories() {
        List<Category> categories = categoryService.getActiveCategories();
        return Result.success(categories);
    }
    
    /**
     * 根据ID获取分类详情
     */
    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    /**
     * 根据名称获取分类
     */
    @GetMapping("/name/{name}")
    public Result<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    /**
     * 创建分类
     */
    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return Result.success("分类创建成功", createdCategory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新分类信息
     */
    @PutMapping("/{id}")
    public Result<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return Result.success("分类更新成功", updatedCategory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return Result.success("分类删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除分类
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteCategories(@RequestBody List<Long> ids) {
        try {
            categoryService.batchDeleteCategories(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 启用/禁用分类
     */
    @PutMapping("/{id}/status")
    public Result<String> toggleCategoryStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            categoryService.toggleCategoryStatus(id, status);
            return Result.success("状态更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新分类排序
     */
    @PutMapping("/{id}/sort")
    public Result<String> updateCategorySort(@PathVariable Long id, @RequestParam Integer sortOrder) {
        try {
            categoryService.updateCategorySort(id, sortOrder);
            return Result.success("排序更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量更新分类排序
     */
    @PutMapping("/batch-sort")
    public Result<String> batchUpdateCategorySort(@RequestBody List<Category> categories) {
        try {
            categoryService.batchUpdateCategorySort(categories);
            return Result.success("批量排序更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查分类名称是否存在
     */
    @GetMapping("/exists/{name}")
    public Result<Boolean> checkCategoryExists(@PathVariable String name) {
        boolean exists = categoryService.existsByName(name);
        return Result.success(exists);
    }
    
    /**
     * 获取分类统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getCategoryStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", categoryService.countCategories());
        statistics.put("active", categoryService.countActiveCategories());
        return Result.success(statistics);
    }
}