package com.Applemango_Backend.search.controller;

import com.Applemango_Backend.search.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/search/{keyword}/{classify}")
    public List<JSONObject> searchforCategory(@PathVariable String keyword, @PathVariable String classify) throws Exception {
        return categoryService.categorization(keyword, classify);
    }
}
