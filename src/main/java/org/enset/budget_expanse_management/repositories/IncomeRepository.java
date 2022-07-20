package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
