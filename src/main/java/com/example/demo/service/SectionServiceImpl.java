package com.example.demo.service;

import com.example.demo.entity.Section;
import com.example.demo.exception.SectionNotFoundException;
import com.example.demo.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service
//@Component
//public class SectionServiceImpl implements SectionService {
//
//    @Autowired
//    private SectionRepository sectionRepository;
//
//    @Override
//    public List<Section> getAllSections() {
//        return sectionRepository.findAll();
//    }
//
//    @Override
//    public Optional<Section> getSectionById(Integer id) {
//        return sectionRepository.findById(Math.toIntExact(id));
//    }
//
//
//
//    @Override
//    public Section createSection(Section section) {
//        return sectionRepository.save(section);
//    }
//
//    @Override
//    public Section updateSection(Long id, Section section) {
//        Optional<Section> existingSection = sectionRepository.findById(Math.toIntExact(id));
//        if (existingSection.isPresent()) {
//            Section updatedSection = existingSection.get();
//            updatedSection.setName(section.getName());
//            return sectionRepository.save(updatedSection);
//        } else {
//            return null; // Or throw an exception
//        }
//    }
//
//    @Override
//    public void deleteSection(Long id) {
//        sectionRepository.deleteById(Math.toIntExact(id));
//    }
//    @Override
//    public Optional<Section> findById(Integer id) {
//        return sectionRepository.findById(id);
//    }
//
//}
@Component
@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    public Optional<Section> getSectionById(Integer id) {
        return sectionRepository.findById(id);
    }

    @Override
    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public Section updateSection(Integer id, Section section) {
        Optional<Section> existingSection = sectionRepository.findById(id);
        if (existingSection.isPresent()) {
            Section updatedSection = existingSection.get();
            updatedSection.setName(section.getName());
            return sectionRepository.save(updatedSection);
        } else {
            throw new SectionNotFoundException("Section not found with id: " + id);
        }
    }

    @Override
    public boolean deleteSection(Integer id) {
        if (sectionRepository.existsById(id)) {
            sectionRepository.deleteById(id);
            return true;
        } else {
            throw new SectionNotFoundException("Section not found with id: " + id);
        }
    }
}
