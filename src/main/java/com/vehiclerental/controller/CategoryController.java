package com.vehiclerental.controller;

import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.CategoryResponse;
import com.vehiclerental.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved", adminService.getAllCategories()));
    }
}
