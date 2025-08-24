package ru.javaguru.repository;

import ru.javaguru.AppRunner;
import ru.javaguru.db.entity.Category;
import ru.javaguru.db.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ContextConfiguration(classes = AppRunner.class)
@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findAll() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSizeGreaterThan(0);
    }

    @Test
    public void findByName() {
        Optional<Category> categories = categoryRepository.findByName("тест");
        assertTrue(categories.isPresent());
    }

    @Test
    public void findByAlias() {
        Optional<Category> categories = categoryRepository.findByAlias("тес");
        assertTrue(categories.isPresent());
    }

    @Test
    public void findById() {
        Optional<Category> category = categoryRepository.findById(1L);
        assertTrue(category.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void shouldCreateNewCategory() {
        // given
        String categoryName = "Транспорт";
        Category category = Category.builder()
                .name(categoryName)
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        Optional<Category> optionalCategory = Optional.of(savedCategory);

        // then
        assertThat(optionalCategory.get())
                .isNotNull()
                .satisfies(cat -> {
                    assertThat(cat.getId()).isNotNull();
                    assertThat(cat.getName()).isEqualTo(categoryName);
                });

        // Проверяем, что категория существует в текущей сессии
        Category cat = categoryRepository.findById(optionalCategory.get().getId());
        assertThat(cat).isNotNull();

        // После завершения теста данные откатятся автоматически
    }

}
