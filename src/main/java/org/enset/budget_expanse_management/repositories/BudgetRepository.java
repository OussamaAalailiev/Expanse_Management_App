package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    //Find budgets that have the 'same categoryExpanse' and
    // 'same Date Period Interval' as the Expanse added by user:
    public List<Budget> findByCategoryExpanse(CategoryExpanseType categoryExpanseType);

//    @Query("SELECT b FROM Budget b " +
//            "WHERE b.user.id=:x " +
//            "AND (b.categoryExpanse.id=:y)")
//    public List<Budget> getSameBudgetsOnAddExpanseWithSameCatDateUserFromDB(
//            @Param("x") UUID userIdFromExpanse, @Param("y") Integer categoryExpanseIdFromExp );
//
//    @Query("SELECT b FROM Budget b " +
//            "WHERE b.user.id=:x " +
//            "AND (b.categoryExpanse.id=:y)")
//    public List<Budget> getSameBudgetsOnAddExpanseWithSameCatDateUserFromDBTest(
//            @Param("x") UUID userIdFromBudget, @Param("y") Integer categoryExpanseIdFromBudget );


    /**This produces an error:
     Query Result should be mapped into fields of 'ResultDTOExpansesBudgets': */
//    @Query("SELECT e.categoryExpanse as categoryExpanse, b.user.id as userId,b.amount as amountBudget, SUM(e.amount) as amountExpanseSum" +
//            " FROM Expanse e, Budget b WHERE e.user.id=b.user.id " +
//            "AND (e.categoryExpanse.id=b.categoryExpanse.id) " +
//            "AND (e.createdDate BETWEEN b.dateDebut AND b.endDate)" +
//            "GROUP BY b.user.id, e.user.id")
//    public List<ResultDTOExpansesBudgets> getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp2();

    @Query("SELECT new " +
            "org.enset.budget_expanse_management.mapping." +
            "ResultDTOExpansesBudgets( " +
            "e.id, e.amount, e.createdDate, e.title, e.budget.id, e.categoryExpanse.id, e.user.id," +
            "b.id, b.amount, b.amountRemains, b.amountSpent, b.dateDebut, b.description," +
            "b.endDate, b.title, b.categoryExpanse.id, b.user.id, SUM(e.amount)) " +
            "FROM Expanse e, Budget b " +
            "WHERE e.user.id=b.user.id \n" +
            "AND (e.categoryExpanse.id =  b.categoryExpanse.id) \n" +
            "AND (e.createdDate BETWEEN b.dateDebut AND b.endDate)" +
            "GROUP BY b.user.id, e.user.id ")//GROUP BY e.id
    public List<ResultDTOExpansesBudgets> getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp3();

    @Query("SELECT new " +
            "org.enset.budget_expanse_management.mapping." +
            "ResultDTOExpansesBudgets( " +
            "e.id, e.amount, e.createdDate, e.title, e.budget.id, e.categoryExpanse.id, e.user.id," +
            "b.id, b.amount, b.amountRemains, b.amountSpent, b.dateDebut, b.description," +
            "b.endDate, b.title, b.categoryExpanse.id, b.user.id, SUM(e.amount)) " +
            "FROM Budget b " +
            "INNER JOIN Expanse e " +
            "ON (e.user.id=b.user.id) AND b.id= :x" +
            "           AND (e.categoryExpanse.id = :y AND b.categoryExpanse.id= :y)" +
            "           AND (e.createdDate BETWEEN b.dateDebut AND b.endDate)")
    public List<ResultDTOExpansesBudgets> onAddBudgetComputeOnCommonExpanses
            (@Param("x") Integer budgetId, @Param("y") Integer categoryExpanseId);

    ///**Get Budgets By Page based on title of expanse + page N° + Size N°: */
    //    Page<Expanse> findByTitleContaining(String title, Pageable pageable);
    Page<Budget> findByTitleContaining(String titleBudget, Pageable page);

    /**Get Budgets By Page based on title of Budget + page N° + Size N° && UserID: */
    public Page<Budget> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);



}
