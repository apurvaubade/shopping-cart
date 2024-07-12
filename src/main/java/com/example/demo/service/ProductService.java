package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Section;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Integer id);
    Product save(Product product);

    Product createProduct(Product product);
    Product updateProduct(Integer id, Product product);
    void deleteProduct(Integer id);
    Product findById(Integer id);
    List<Product> searchProducts(String categoryName, String sectionName, String name, BigDecimal price, BigDecimal discount);


}
