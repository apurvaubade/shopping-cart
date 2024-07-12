package com.example.demo.controller;

import com.example.demo.dto.ProductSearchRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Section;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.CategoryRepository;

import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired(required=true)
    private ProductSearchRequest productSearchRequest;



    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


@GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        try {
            Product product = productService.findById(id);
            if (product != null) {
                ProductResponse response = new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getDiscount(),
                        product.getCategory().getId().intValue() // Cast Long to Integer
                );
                return ResponseEntity.ok(response);
            } else {
                throw new ProductNotFoundException("Product not found with id " + id);
            }
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDiscount(productRequest.getDiscount());
        int categoryId = productRequest.getCategory_id();
        System.out.println("categoryId"+categoryId);
        // Fetch category from repository
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        product.setCategory(category);

        // Save product using service
        Product createdProduct = productService.save(product);

        // Prepare response in ProductResponse format
        ProductResponse response = new ProductResponse(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getDescription(),
                createdProduct.getPrice(),
                createdProduct.getQuantity(),
                createdProduct.getDiscount(),
                categoryId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestBody ProductSearchRequest searchRequest) {
        List<Product> products = productService.searchProducts(
                searchRequest.getCategoryName(),
                searchRequest.getSectionName(),
                searchRequest.getName(),
                searchRequest.getPrice(),
                searchRequest.getDiscount()
        );

        List<ProductResponse> responses = products.stream().map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getDiscount(),
                product.getCategory().getId()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductController.ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody ProductController.ProductRequest productRequest) {
       Optional<Product>  optionalProduct = Optional.ofNullable(productService.findById(id));
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setQuantity(productRequest.getQuantity());
            product.setDiscount(productRequest.getDiscount());

            Integer categoryId = productRequest.getCategory_id();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Section not found with id: " + categoryId));
            product.setCategory(category);

            Product updatedProduct = productService.save(product);

            ProductController.ProductResponse response = new ProductController.ProductResponse(
                    updatedProduct.getId(),
                    updatedProduct.getName(),
                    updatedProduct.getDescription(),
                    updatedProduct.getPrice(),
                    updatedProduct.getQuantity(),
                    updatedProduct.getDiscount(),
                    categoryId
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    static class ProductResponse {
        private int id;
        private String name;
        private String description;
        private BigDecimal price;
        private int quantity;
        private BigDecimal discount;
        private Integer category_id;

        public ProductResponse(int id, String name, String description, BigDecimal price, int quantity, BigDecimal discount, Integer category_id) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.discount = discount;
            this.category_id = category_id;
        }

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public Integer getCategory_id() {
            return category_id;
        }

        public void setCategory_id(Integer category_id) {
            this.category_id = category_id;
        }
    }

    // Request DTO for JSON input
    static class ProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
        private int quantity;
        private BigDecimal discount;
        private int category_id;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }
    }

}


