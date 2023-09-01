package com.example.crudspringboot.service;

import com.example.crudspringboot.entity.ProductsEntity;
import com.example.crudspringboot.repository.ProductsRepository;
import com.example.crudspringboot.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductsRepository productsRepository;
    public String createProduct(ProductRequest request){
        try {
            var product= ProductsEntity.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .image(request.getImage())
                    .build();
            productsRepository.save(product);
            return "create product success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "failed create product";
    }

    public List<ProductsEntity> find() {
        List<ProductsEntity> products = productsRepository.findAll();
        if (products != null) {
            return products;
        }
        return null;
    }
    @Transactional
    public String updateProduct(ProductRequest request, Integer productId){
        ProductsEntity product= ProductsEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .image(request.getImage())
                .build();
        Optional<ProductsEntity> prod= productsRepository.findById(productId);
        if(prod.get()==null){
            return "product is not found";
        }
        ProductsEntity existingProduct= prod.get();
        try{
            if(product.getName() !=null){
                existingProduct.setName(product.getName());
            }
            if(product.getPrice() !=null){
                existingProduct.setPrice(product.getPrice());
            }
            if(product.getDescription() !=null){
                existingProduct.setDescription(product.getDescription());
            }
            if(product.getImage() !=null){
                existingProduct.setImage(product.getImage());
            }
        }catch (Exception e){
            e.printStackTrace();
            return "update failed";
        }
        productsRepository.save(existingProduct);
        return "update is success";
    }
    public String deleteProduct(Integer id){
        Optional<ProductsEntity> isProduct= productsRepository.findById(id);
        try {
            if(isProduct.get()!=null){
                productsRepository.deleteById(id);
            }else{
                return "product is exist";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "delete is failed";
        }
        return "delete is success";
    }
}