package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    //Find budgets that have the 'same categoryExpanse' and
    // 'same Date Period Interval' as the Expanse added by user:
    public List<Budget> findByCategoryExpanse(CategoryExpanseType categoryExpanseType);
}
