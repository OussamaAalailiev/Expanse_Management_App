package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.ExpensesByCategory;
import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.mapping.TotalExpansePerMonthDTO;
import org.enset.budget_expanse_management.model.Expanse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:4090/")
//@Transactional
public interface ExpanseRepository extends JpaRepository<Expanse, Long> {

    @Query("SELECT e, b FROM Expanse e, Budget b WHERE e.user.id=b.user.id " +
            "AND (e.categoryExpanse.id=:x AND b.categoryExpanse.id=:x) " +
            "AND (e.createdDate BETWEEN b.dateDebut AND b.endDate)")
   public List<Object> getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp(@Param("x") Integer categoryExpanseId);

    //After 'ON' Keyword there is the 'Joint Condition' of the two tables:
//   @Query("SELECT e FROM Expanse e INNER JOIN Budget b " +
//           "ON (e.user.id=b.user.id) AND e.id=:y AND (e.categoryExpanse.id =:x AND b.categoryExpanse.id =:x)\n" +
//           "   AND ( e.createdDate BETWEEN b.dateDebut AND b.endDate)")
//   public List<ResultDTOExpansesBudgets> checkIfBudgetsAreRespectedOnAdd(
//           @Param("x") Integer categoryOfExpanseId, @Param("y") Long expanseId);

    /**Inner Joint: the following function works well! :) */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets(" +
            "e.id, e.amount, e.createdDate, e.title, e.categoryExpanse.id, e.user.id," +
            "b.id, b.amount, b.amountRemains, b.amountSpent, b.dateDebut, b.description," +
            "b.endDate, b.title, b.categoryExpanse.id, b.user.id)" +
            "FROM Expanse e INNER JOIN Budget b " +
            "ON (e.user.id=b.user.id) AND e.id=:y AND (e.categoryExpanse.id =:x AND b.categoryExpanse.id =:x)\n" +
            "   AND ( e.createdDate BETWEEN b.dateDebut AND b.endDate)")
    public List<ResultDTOExpansesBudgets> onOneExpanseComputeOnCommonBudgets(
            @Param("x") Integer categoryOfExpanseId, @Param("y") Long expanseId);
//    @Query("SELECT YEAR(e.createdDate) AS year, MONTH(e.createdDate) AS month, SUM(e.amount) As totalExpanse\n" +
//            "FROM Expanse e\n" +
//            "GROUP BY YEAR(e.createdDate), MONTH(e.createdDate)")

    /**-- Query to get Total Amount of Expanses per Month: */
 /*
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping.TotalExpansePerMonthDTO(" +
            "                      YEAR(e.createdDate), MONTH(e.createdDate), SUM(e.amount))" +
            "FROM Expanse e \n" +
            "GROUP BY YEAR(e.createdDate), MONTH(e.createdDate)")
    public List<TotalExpansePerMonthDTO> getTotalAmountExpansesOnEveryMonth();

  */

   /**-- Query to get Total Amount of Expanses per Month By UserID: */
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping" +
            ".TotalExpansePerMonthDTO(DATE_FORMAT(e.createdDate, '%Y')," +
            " DATE_FORMAT(e.createdDate, '%m'), SUM(e.amount), e.user.id, e.user.name)" +
            "FROM Expanse e WHERE e.user.id=:x " +
            "GROUP BY DATE_FORMAT(e.createdDate, '%Y'), DATE_FORMAT(e.createdDate, '%m') " +
            "ORDER BY DATE_FORMAT(e.createdDate, '%Y'), DATE_FORMAT(e.createdDate, '%m') DESC")
   // public Page<TotalExpansePerMonthDTO> getTotalAmountExpansesOnEveryMonthV2(@Param("x") UUID userId, Pageable pageable);
    List<TotalExpansePerMonthDTO> getTotalAmountExpansesOnEveryMonthV2(@Param("x") UUID userId);

    /**Get Expanses By Page based on title of expanse + page N° + Size N°: */
    Page<Expanse> findByTitleContaining(String title, Pageable pageable);

    /**Get Expanses By Page based on title of expanse + page N° + Size N° && UserID: */
    public Page<Expanse> findByTitleContainingAndUserId(String title, UUID userId, Pageable pageable);

    /** Select Total Expanses By Category & UserID :*/
    @Query("SELECT NEW org.enset.budget_expanse_management.mapping" +
            ".ExpensesByCategory(e.id, e.amount, e.createdDate, e.title, " +
            " e.categoryExpanse.id, e.user.id, SUM(e.amount)) FROM Expanse e " +
            "WHERE e.user.id=:x GROUP BY e.categoryExpanse.id ORDER BY SUM(e.amount)")
    List<ExpensesByCategory> getTotalExpensesByCategoryAndUser(@Param("x") UUID userId);


}
