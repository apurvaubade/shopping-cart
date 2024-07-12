package com.example.demo.controller;

import com.example.demo.entity.Section;
import com.example.demo.exception.SectionNotFoundException;
import com.example.demo.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Section> getSectionById(@PathVariable Integer id) {
//        Section section = sectionService.getSectionById(id)
//                .orElseThrow(() -> new SectionNotFoundException("Section not found with id " + id));
//        return ResponseEntity.ok(section);
//
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSectionById(@PathVariable Integer id) {
        try {
            Section section = sectionService.getSectionById(id)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with id " + id));
            return ResponseEntity.ok(section);
        } catch (SectionNotFoundException e) {
            System.out.println("error mesg"+e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        Section createdSection = sectionService.createSection(section);
        return ResponseEntity.ok(createdSection);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateSection(@PathVariable Integer id, @RequestBody Section section) {
        try {
            sectionService.updateSection(id, section);
            return ResponseEntity.ok("Section updated successfully");
        } catch (SectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Integer id) {
        try {
            sectionService.deleteSection(id);
            return ResponseEntity.ok("Section deleted successfully");
        } catch (SectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

