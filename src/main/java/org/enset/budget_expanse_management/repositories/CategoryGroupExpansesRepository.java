package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryGroupExpansesRepository extends JpaRepository<CategoryGroup, Integer> {
}
