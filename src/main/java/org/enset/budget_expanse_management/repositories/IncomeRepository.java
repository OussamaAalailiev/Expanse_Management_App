package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.ResultDTOIncomesGoals;
import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    /**Get Incomes By Page based on title of Income + page N° + Size N° && UserID: */
    public Page<Income> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);

    /**Inner Joint 'Income(s) + Goal(s)': the following function works well! :) */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping.ResultDTOIncomesGoals(" +
            "i.id, i.amount, i.createdDate, i.title, i.categoryIncome.id, i.user.id," +
            "g.id, g.amount, g.amountAchieved, g.goalAchieved, g.dateDebut, g.description," +
            "g.endDate, g.title, g.categoryIncome.id, g.user.id)" +
            "FROM Income i INNER JOIN Goal g " +
            "ON (i.user.id=g.user.id) AND i.id=:y AND (i.categoryIncome.id = g.categoryIncome.id)\n" +
            "   AND ( i.createdDate BETWEEN g.dateDebut AND g.endDate)")
    public List<ResultDTOIncomesGoals> onOneIncomeComputeOnCommonGoals(@Param("y") Long incomeId);
}
