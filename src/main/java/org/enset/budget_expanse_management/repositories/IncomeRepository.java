package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.*;
import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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

    /**Inner Joint 'Income(s) + Goal(s)':
     *   provide 3 Params (userId, categoryIncomeId and createdDate) instead of 1 (userId): */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping.ResultDTOIncomesGoals(" +
            "i.id, i.amount, i.createdDate, i.title, i.categoryIncome.id, i.user.id," +
            "g.id, g.amount, g.amountAchieved, g.goalAchieved, g.dateDebut, g.description," +
            "g.endDate, g.title, g.categoryIncome.id, g.user.id)" +
            "FROM Income i INNER JOIN Goal g " +
            "ON (i.user.id=g.user.id) AND i.id=:u AND ( g.categoryIncome.id=:c )\n" +
            "   AND ( g.dateDebut<=:d AND g.endDate>=:d)")
    public List<ResultDTOIncomesGoals> onOneIncomeComputeOnCommonGoalsV2(@Param("u") Long incomeId,
                                                                         @Param("c") Integer categoryIncomeId,
                                                                         @Param("d") Date createdDate);

    /**-- Query to get Total Amount of Incomes per Month By UserID: */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping" +
            ".TotalIncomesPerMonthDTO(DATE_FORMAT(i.createdDate, '%Y')," +
            " DATE_FORMAT(i.createdDate, '%m'), SUM(i.amount), i.user.id, i.user.name)" +
            "FROM Income i WHERE i.user.id=:x " +
            "GROUP BY DATE_FORMAT(i.createdDate, '%Y'), DATE_FORMAT(i.createdDate, '%m') " +
            "ORDER BY DATE_FORMAT(i.createdDate, '%Y'), DATE_FORMAT(i.createdDate, '%m') DESC")
    List<TotalIncomesPerMonthDTO> getTotalAmountIncomesOnEveryMonth(@Param("x") UUID userId);

    /** Select Total Incomes By Category & UserID Ordered by Amount:*/
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping" +
            ".IncomesByCategory(i.id, i.amount, i.createdDate, i.title, " +
            " i.categoryIncome.id, i.user.id, SUM(i.amount)) FROM Income i" +
            " WHERE i.user.id=:x GROUP BY i.categoryIncome.id ORDER BY SUM(i.amount) DESC")
    List<IncomesByCategory> getTotalIncomesByCategoryAndUser(@Param("x") UUID userId);
    /** Select Total Incomes By Category & UserID Ordered by Date DESC:*/
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping" +
            ".IncomesByCategory(i.id, i.amount, i.createdDate, i.title, " +
            " i.categoryIncome.id, i.user.id, SUM(i.amount)) FROM Income i" +
            " WHERE i.user.id=:x " +
            " GROUP BY i.categoryIncome.id " +
            " ORDER BY YEAR(i.createdDate) DESC, MONTH(i.createdDate) DESC, DAY(i.createdDate) DESC")
    List<IncomesByCategory> getTotalIncomesByCategoryAndUserOrderedByDate(@Param("x") UUID userId);
}
