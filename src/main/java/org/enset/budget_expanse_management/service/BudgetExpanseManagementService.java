package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;

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
    /**Get Expanses By Page based on title of expanse + page N° + Size N° from Service: */
    public Page<Expanse> getExpansesByPageAndSizeAndTitleService(String title, int page, int size);
    /**Inner Joint that get all common Expanses(if exists) with one new Budget added
     *  by a user at each query:
     *  */
    public void calculateExpansesOnAddBudgetService(Budget budget);
    public void updateBudgetService(Budget budget);
    public void deleteBudgetService(Budget budget);

    /**Inner Joint that get all common Goals(if exists) with one new Income added
     *  by a user at each query:*/
    public void calculateGoalsOnAddIncomeService(Income income);
}
