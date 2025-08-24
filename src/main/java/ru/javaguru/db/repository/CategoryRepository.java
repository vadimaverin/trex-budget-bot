package ru.javaguru.db.repository;

import ru.javaguru.db.entity.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @NotNull
    List<Category> findAll();

    Optional<Category> findByName(String name);

    Category findById(int id);

    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE c.alias =:alias")
    Optional<Category> findByAlias(@Param("alias") String alias);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.alias =:alias")
    boolean existsByAlias(@Param("alias") String alias);

}
