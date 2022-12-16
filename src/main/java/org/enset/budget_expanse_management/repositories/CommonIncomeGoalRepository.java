package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.CommonIncomeGoal;
import org.enset.budget_expanse_management.model.CommonIncomeGoalID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonIncomeGoalRepository extends JpaRepository<CommonIncomeGoal, CommonIncomeGoalID> {
    List<CommonIncomeGoal> findById_IncomeId(Long incomeId);
    List<CommonIncomeGoal> findById_GoalId(Integer goalId);
}
