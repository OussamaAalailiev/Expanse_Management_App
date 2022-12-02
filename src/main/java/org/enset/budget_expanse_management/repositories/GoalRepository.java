package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.CommonIncome;
import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.mapping.ResultDTOGoalAndIncomes;
import org.enset.budget_expanse_management.mapping.ResultDTOIncomesGoals;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
    /**Get Goals By Page based on title of expanse + page N° + Size N° && UserID: */
    public Page<Goal> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);
    public Page<Goal> findByTitleContainingAndUserIdOrderByDateDebutDesc(String title, UUID userId, Pageable pageable);
    public Page<Goal> findByTitleContainingAndUserIdOrderByDateDebutDescEndDateDesc(String title, UUID userId, Pageable pageable);

    /*
    -- Inner Join for Common Goal & Incomes provided by 1 Param (userId):
SELECT * FROM goal g INNER JOIN income i ON (i.user_id=g.user_id) AND g.id=2
                                                             AND (i.category_income_id = g.category_income_id)
                                                             AND (g.date_debut<=i.created_date AND g.end_date>=i.created_date);
     */
    @Query("SELECT new " +
            "org.enset.budget_expanse_management.mapping." +
            "ResultDTOGoalAndIncomes(" +
            "g.id, g.amount, g.amountAchieved, g.goalAchieved, g.dateDebut, g.description," +
            "g.endDate, g.title, g.categoryIncome.id, g.user.id, " +
            "i.id, i.amount, i.createdDate, i.title, i.categoryIncome.id, i.user.id, SUM(i.amount))" +
            "FROM Goal g" +
            " INNER JOIN Income i " +
            "ON (g.user.id=i.user.id) AND g.id=:x AND (g.categoryIncome.id =i.categoryIncome.id)\n" +
            "   AND (i.createdDate>=:d AND i.createdDate<=:e)")
    public ResultDTOGoalAndIncomes onAddGoalComputeOnCommonIncomes(
            @Param("x") Integer goalId,
            @Param("d") Date dateDebut,
            @Param("e") Date endDate);
    /**Request down below gets Common Incomes Based on 3 Common Params (userId + catIncome + dateRange)
     *  that they will be filled on Add new Goal:
     *We get Only Common mapped incomes Instead of InnerJoin with Goal: */
    /*
    SELECT * FROM db_expanse_management.income i
       WHERE i.user_id='3a300bc8-8954-4e93-9136-2b11ad2461b1'
       AND i.category_income_id=10
       AND (i.created_date>='2022-09-28' AND i.created_date<='2022-09-28');
     */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping.CommonIncome(" +
            "i.id, i.amount, i.title, i.createdDate, i.categoryIncome.id, i.user.id, SUM(i.amount))" +
            "FROM Income i WHERE i.user.id=:u AND i.categoryIncome.id=:c " +
            " AND (i.createdDate>=:d AND i.createdDate<=:e)")
    List<CommonIncome> getCommonIncomesOnAddNewGoal(@Param("u") UUID userId, @Param("c") Integer categoryOfIncomeId,
                                                    @Param("d") Date dateDebut, @Param("e") Date endDate);

    //g.dateDebut<=:d AND g.endDate>=:d
    //i.createdDate BETWEEN g.dateDebut AND g.endDate
}
