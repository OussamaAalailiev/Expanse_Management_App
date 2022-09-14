package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    /**Get Incomes By Page based on title of Income + page N° + Size N° && UserID: */
    public Page<Income> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);
}
