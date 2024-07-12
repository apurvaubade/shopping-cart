package com.example.demo.controller;

import com.example.demo.dto.CategoryRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Section;
import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.SectionNotFoundException;
import com.example.demo.repository.SectionRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.SectionService;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

@Autowired
private CategoryRequest categoryRequest;
    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
//        Category category = categoryService.findById(id);
//
//        if (category != null) {
//            CategoryResponse response = new CategoryResponse(
//                    category.getId(),
//                    category.getName(),
//                    category.getSection().getId().intValue() // Cast Long to Integer
//            );
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.notFound().build();
//
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        try {
            Category category = categoryService.findById(id);
            if (category != null) {
                CategoryResponse response = new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getSection().getId().intValue() // Cast Long to Integer
                );
                return ResponseEntity.ok(response);
            } else {
                throw new CategoryNotFoundException("Category not found with id " + id);
            }
        } catch (CategoryNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }





//    @PostMapping
//    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
//        Category category = new Category();
//        category.setName(categoryRequest.getName());
//        Integer sectionId = categoryRequest.getSection_id();
//
//        // Fetch section from repository
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
//
//        category.setSection(section);
//
//        // Save category using service
//        Category createdCategory = categoryService.save(category);
//
//        // Prepare response in CategoryResponse format
//        CategoryResponse response = new CategoryResponse(
//                createdCategory.getId(),
//                createdCategory.getName(),
//                sectionId
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }


    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category category = new Category();
            category.setName(categoryRequest.getName());
            Integer sectionId = categoryRequest.getSection_id();

            // Fetch section from repository
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with id: " + sectionId));

            category.setSection(section);

            // Save category using service
            Category createdCategory = categoryService.save(category);

            // Prepare response in CategoryResponse format
            CategoryResponse response = new CategoryResponse(
                    createdCategory.getId(),
                    createdCategory.getName(),
                    sectionId
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category: " + e.getMessage());
        }
    }

    // Update category by id
    @PutMapping("/{id}")
//    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
//        Optional<Category> optionalCategory = Optional.ofNullable(categoryService.findById(id));
//
//        if (optionalCategory.isPresent()) {
//            Category category = optionalCategory.get();
//            category.setName(categoryRequest.getName());
//
//
//            Integer sectionId = categoryRequest.getSection_id();
//            Section section = sectionRepository.findById(sectionId)
//                    .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
//            category.setSection(section);
//
//            Category updatedCategory = categoryService.save(category);
//
//            CategoryResponse response = new CategoryResponse(
//                    updatedCategory.getId(),
//                    updatedCategory.getName(),
//                    sectionId
//            );
//
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
        try {
            Category category = categoryService.findById(id);
            if (category == null) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }

            category.setName(categoryRequest.getName());

            Integer sectionId = categoryRequest.getSection_id();
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with id: " + sectionId));

            category.setSection(section);

            Category updatedCategory = categoryService.save(category);

            CategoryResponse response = new CategoryResponse(
                    updatedCategory.getId(),
                    updatedCategory.getName(),
                    sectionId
            );

            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException | SectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category: " + e.getMessage());
        }
    }

    // Delete category by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        try {
            Category category = categoryService.findById(id);
            if (category == null) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }

            categoryService.deleteById(id);
            return ResponseEntity.ok("Category deleted successfully");

        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting category: " + e.getMessage());
        }
    }

    static class CategoryResponse {
        private int id;
        private String name;
        private Integer section_id;

        public CategoryResponse(int id, String name, Integer section_id) {
            this.id = id;
            this.name = name;
            this.section_id = section_id;
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

        public Integer getSection_id() {
            return section_id;
        }

        public void setSection_id(Integer section_id) {
            this.section_id = section_id;
        }
    }


}