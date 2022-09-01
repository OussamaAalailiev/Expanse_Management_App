package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.mapping.TotalExpansePerMonthDTO;
import org.enset.budget_expanse_management.model.Expanse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
            "e.id, e.amount, e.createdDate, e.title, e.budget.id, e.categoryExpanse.id, e.user.id," +
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


}
