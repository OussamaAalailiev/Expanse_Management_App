package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.formModel.GoalFormSubmission;
import org.enset.budget_expanse_management.mapping.ExpensesByCategory;
import org.enset.budget_expanse_management.mapping.IncomesByCategory;
import org.enset.budget_expanse_management.mapping.TotalExpansePerMonthDTO;
import org.enset.budget_expanse_management.mapping.TotalIncomesPerMonthDTO;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.model.Income;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BudgetExpanseManagementService {

   // public void getAllExpAndBudWithSameUserDateAndCatExpService();

    /**Inner Joint that get all common Budgets with one Expanse at each query:
     * The following function works well! :) */
    public void calculateBudgetsOnUpdateExpanseService(Expanse expanse);
    public void calculateBudgetsOnAddExpanseService(Expanse expanse);
    public void calculateBudgetsOnDeleteExpanseService(Expanse expanse);
    /**Get Expanses By Page based on title of expanse + page N° + Size N° from Service: */
    public Page<Expanse> getExpansesByPageAndSizeAndTitleService(String title, int page, int size);
    /**Get Expanses By Page based on title of expanse + page N° + Size N° from Service: */
    public Page<Expanse> getExpansesByPageAndSizeAndTitleAndUserIdService(String title, String userId,
                                                                          int page, int size);
    /** Get Total Expenses Sum By Category & UserId:*/
    public List<ExpensesByCategory> getExpensesSumByCategoryAndUserIdService(String userId);
    public List<ExpensesByCategory> getExpensesSumByCategoryAndUserIdAmountDescService(String userId);

    /**Inner Joint that get all common Expanses(if exists) with one new Budget added
     *  by a user at each query:
     *  */
    public void calculateExpansesOnAddBudgetService(Budget budget);
    /** Query to get Total Amount of Expanses per Month By UserID: */
//    public Page<TotalExpansePerMonthDTO> getTotalExpansesPerYearMonthAndUserService(String userId,
//                                                                                    int page,
//                                                                                    int size);
    public List<TotalExpansePerMonthDTO> getTotalExpansesPerYearMonthAndUserService(String userId);
    /** Query to get Total Amount of Expenses so far: */
    public TotalExpansePerMonthDTO getTotalExpansesPerLifeTimeAndUserService(String userId);
    public void updateBudgetService(Budget budget);
    public void deleteBudgetService(Budget budget);
    /**Get Budgets By Page based on title of budget + page N° + Size N° from Service: */
    public Page<Budget> getBudgetsByPageAndSizeAndTitleService(String title, int page, int size);
    /**Get Budgets By Page based on title of expanse + page N° + Size N° from Service: */
    public Page<Budget> getBudgetsByPageAndSizeAndTitleAndUserIdService(String title, String userId,int page, int size);

    /**Get Goals By Page based on title of Goal + page N° + Size N° from Service: */
    public Page<Goal> getGoalsByPageAndSizeAndTitleAndUserIdService(String goalTitle, String userId, int page, int size);
    /**Get Income By Page based on title of Income + page N° + Size N° from Service: */
    public Page<Income> getIncomesByPageAndSizeAndTitleAndUserIdService(String incomeTitle, String userId, int page, int size);

    /**Inner Joint that get all common Goals(if exists) On Add new Income
     *  by a user at each query:*/
    public void calculateGoalsOnAddIncomeService(Income income);
    /**Compute all common Goals(if exists) ON Update an Income
     *  by a user at each query.
     *  Completed Algorithm to compute Goals On Update Only 'AMOUNT' of Income.*/
    public void calculateGoalsOnUpdateIncomeService(Income income);

    // TODO: Update All fields (of an Income) On Update Income Algorithm:
   // public void calculateGoalsOnUpdateFullIncomeService(Income income);
    public void calculateGoalsOnUpdateFullIncomeServiceV2(Income income);

    /**Compute all common Goals(if exists) ON Delete an Income
     *  by a user at each query:*/
    public void calculateGoalsOnDeleteIncomeService(Income income);

    /** Get Total Amount of Incomes per Month By UserID from Service: */
    List<TotalIncomesPerMonthDTO> getTotalIncomesPerYearMonthAndUserService(String userId);

    /** Get Total Amount of Incomes For LifeTime By UserID from Service: */
    TotalIncomesPerMonthDTO getTotalIncomesPerLifeTimeAndUserService(String userId);

    /** Get Total Incomes Sum By Category & UserId Ordered by Amount Desc:*/
    List<IncomesByCategory> getIncomesSumByCategoryAndUserIdService(String userId);
    /** Get Total Incomes Sum By Category & UserId Ordered by Date Desc:*/
    List<IncomesByCategory> getIncomesSumByCategoryAndUserIdDateDescService(String userId);
    /**Inner Joint that get all common Expanses(if exists) with one new Budget added
     *  by a user at each query:
     *  */
    void calculateIncomesOnAddGoalService(Goal goal);
    /**Getting Just Common Incomes without new Joined Goal: */
    void calculateIncomesOnAddGoalServiceV2(Goal goal);

    void deleteGoalService(Integer goalId);

    Goal mapNewFormGoalObjToGoalObj(GoalFormSubmission goalFormSubmission);


}
