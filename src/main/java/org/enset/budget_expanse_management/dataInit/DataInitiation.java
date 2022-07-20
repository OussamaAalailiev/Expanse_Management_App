package org.enset.budget_expanse_management.dataInit;

public interface DataInitiation {
    public void initCategoryIncomeWithCategoriesData();
    public void initCategoryExpanseWithCategoriesData();
    public void initCategoryExpanseGroupWithCategoriesData();
    public void initUsers();
    public void getUsersAfterDataInit();
    public void initIncomes();
    public void initExpanses();
    public void initGoals();
    public void initBudgets();
}
