package ru.javaguru.service;

import ru.javaguru.db.entity.Category;
import ru.javaguru.db.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public boolean findById(Long id) {
        return categoryRepository.findById(id).isPresent();
    }

    public Optional<Category> findByNameOrAlias(String nameOrAlias) {
        return categoryRepository.findByName(nameOrAlias)
            .or(() -> categoryRepository.findByAlias(nameOrAlias));
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public boolean existsByAlias(String alias) {
        return categoryRepository.existsByAlias(alias);
    }

    public Optional<Category> updateName(Long id, String name) {
       Optional<Category> category = categoryRepository.findById(id);
       if (category.isPresent()) {
           category.get().setName(name);
           categoryRepository.saveAndFlush(category.get());
       }
        return category;
    }

    public Optional<Category> updateAlias(Long id, String alias) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            category.get().setAlias(alias);
            categoryRepository.saveAndFlush(category.get());
        }
        return category;
    }

    public Category create(Category category) {
           return categoryRepository.save(category);
    }



}
