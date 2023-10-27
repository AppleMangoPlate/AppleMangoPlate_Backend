package com.Applemango_Backend.search.controller;

import com.Applemango_Backend.search.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/search/{keyword}/{classify}")
    public String searchforCategory(@PathVariable String keyword, String classify) throws Exception {
        return categoryService.categorization(keyword, classify);
    }
}
