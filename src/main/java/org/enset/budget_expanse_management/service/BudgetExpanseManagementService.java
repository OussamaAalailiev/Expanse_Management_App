package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;

public interface BudgetExpanseManagementService {
    public void addExpanseToOneOrZeroBudgetService(Expanse expanse, Budget budget);
    public void addExpanseToBudgetServiceInit();

    public void getAllExpAndBudWithSameUserDateAndCatExpService();
}
