package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
}
