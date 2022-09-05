package org.enset.budget_expanse_management.repositories;

// import org.enset.budget_expanse_management.model.Category;
import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//This repository is Unused yet!

public interface CategoryExpanseRepository extends JpaRepository<CategoryExpanse, Integer> {
    public CategoryExpanse findByCategoryExpanseTypeContains(Enum categoryExpanseType);
    @Query("FROM CategoryExpanse c WHERE c.categoryExpanseType=:x")
    public CategoryExpanse getCategoryExpanseTypeContains(@Param(value = "x") String categoryExpanseType);
}
