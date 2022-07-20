package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
}
