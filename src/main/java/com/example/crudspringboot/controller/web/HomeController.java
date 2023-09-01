package com.example.crudspringboot.controller.web;

import com.example.crudspringboot.entity.ProductsEntity;
import com.example.crudspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    @GetMapping("")
    public String listProducts(Model model){
        List<ProductsEntity> products= productService.find();
        model.addAttribute("products", products);
        return "web/home";
    }

}
