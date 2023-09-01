package com.example.crudspringboot.controller.admin;

import com.example.crudspringboot.entity.ProductsEntity;
import com.example.crudspringboot.request.ProductRequest;
import com.example.crudspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class CRUDController {
    private final ProductService productService;
    @GetMapping("")
    public ResponseEntity<List<ProductsEntity>> listProducts(Model model){
        List<ProductsEntity> products= productService.find();
        model.addAttribute("products", products);
        return ResponseEntity.ok(products);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request){
        return ResponseEntity.ok(productService.createProduct(request));
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest request, @PathVariable Integer productId){
        return ResponseEntity.ok(productService.updateProduct(request,productId));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam(name = "id") Integer productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
