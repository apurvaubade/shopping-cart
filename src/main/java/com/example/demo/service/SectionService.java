package com.example.demo.service;

import com.example.demo.entity.Section;

import java.util.List;
import java.util.Optional;
public interface SectionService {
    List<Section> getAllSections();
    Optional<Section> getSectionById(Integer id);
    Section createSection(Section section);
    Section updateSection(Integer id, Section section);
    boolean deleteSection(Integer id);
}
