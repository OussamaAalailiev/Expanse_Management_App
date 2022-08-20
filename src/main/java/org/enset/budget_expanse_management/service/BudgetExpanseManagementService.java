package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;

import java.util.List;

public interface BudgetExpanseManagementService {
  //  public void addExpanseToOneOrZeroBudgetService(Expanse expanse, Budget budget);
   // public void addExpanseToBudgetServiceInit();

    public void getAllExpAndBudWithSameUserDateAndCatExpService();

    //return type changed from 'Budget' To 'void':

   // public void checkIfBudgetIsRespectedOnAddExpanse(Expanse expanse);
   // public void checkIfBudgetIsRespectedOnAddExpanse2(Expanse expanse, Budget budget);

    //public List<Budget> getAllBudgetsFromDBService();
    //public Budget checkIfBudgetIsRespectedByCalculation();
  //  public void checkIfBudgetIsRespectedByCalculation();
   // public void checkIfBudgetIsRespectedByCalculationSumAmountExp();

    /**Inner Joint that get all common Budgets with one Expanse at each query:
     * The following function works well! :) */
    public void calculateBudgetsOnUpdateExpanseService(Expanse expanse);
    public void calculateBudgetsOnAddExpanseService(Expanse expanse);
    public void calculateBudgetsOnDeleteExpanseService(Expanse expanse);
    /**Inner Joint that get all common Expanses(if exists) with one new Budget added
     *  by a user at each query:
     *  */
    public void calculateExpansesOnAddBudgetService(Budget budget);
    public void updateBudgetService(Budget budget);
    public void deleteBudgetService(Budget budget);
}
