package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
    /**Get Goals By Page based on title of expanse + page N° + Size N° && UserID: */
    public Page<Goal> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);
    public Page<Goal> findByTitleContainingAndUserIdOrderByDateDebutDesc(String title, UUID userId, Pageable pageable);
    public Page<Goal> findByTitleContainingAndUserIdOrderByDateDebutDescEndDateDesc(String title, UUID userId, Pageable pageable);


}
