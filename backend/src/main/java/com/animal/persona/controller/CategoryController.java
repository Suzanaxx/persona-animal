package com.animal.persona.controller;

import com.animal.persona.model.Category;
import com.animal.persona.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Helper metoda za preverjanje admin pravic
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        String email = auth.getName();
        return "admin@gmail.com".equalsIgnoreCase(email);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Only admin can create categories");
        }

        if (categoryService.findByNameIgnoreCase(category.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Category already exists");
        }

        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category updatedCategory) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Only admin can update categories");
        }

        return categoryService.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setImageUrl(updatedCategory.getImageUrl());
                    Category saved = categoryService.save(category);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Only admin can delete categories");
        }

        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
