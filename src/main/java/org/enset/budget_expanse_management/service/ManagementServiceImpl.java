package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.repositories.BudgetRepository;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ManagementServiceImpl implements BudgetExpanseManagementService {

    private final ExpanseRepository expanseRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryExpanseRepository categoryExpanseRepository;

    public ManagementServiceImpl(ExpanseRepository expanseRepository,
                                 BudgetRepository budgetRepository,
                                 CategoryExpanseRepository categoryExpanseRepository) {
        this.expanseRepository = expanseRepository;
        this.budgetRepository = budgetRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
    }

    @Override
    public void addExpanseToOneOrZeroBudgetService(Expanse expanse, Budget budget) {
        if(expanse != null && budget==null){//If Expanse added doesn't belong to any Budget:
            expanseRepository.save(expanse);
        } else if (expanse != null && budget!=null) {//Algorithm Calculation ...


//            if (expanse.getCategoryExpanse().getCategoryExpanseType() ==
//                    budget.getCategoryExpanse().getCategoryExpanseType()){
//
//                if (expanse.getCreatedDate().compareTo(budget.getDateDebut()) >= 0 &&
//                    expanse.getCreatedDate().compareTo(budget.getEndDate()) <=0){
//
//                }
//
//            }
            List<Budget> budgets = new ArrayList<>();
            //budgets = budgetRepository.
        } else if (expanse==null) {
            throw new RuntimeException("Expanse cannot be null !");
        }

    }

    @Override
    public void addExpanseToBudgetServiceInit() {
        List<Expanse> expanseList = expanseRepository.findAll();
        List<Budget> budgetList = budgetRepository.findAll();

            for (int i = 0; i < expanseList.size(); i++) {
                if ((i+1) % 2!=0){
                    expanseList.get(i).setBudget(budgetList.get(1));
            }
        }

    }

    public void getAllExpAndBudWithSameUserDateAndCatExpService(){

        List<Object> objectList = expanseRepository
                .getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp(20);
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof Expanse){
                Expanse expanse = new Expanse(((Expanse) objectList.get(i)).getId(),
                        ((Expanse) objectList.get(i)).getAmount(), ((Expanse) objectList.get(i)).getTitle(),
                        ((Expanse) objectList.get(i)).getCreatedDate());
                expanse.setUser(((Expanse) objectList.get(i)).getUser());
                System.out.println("Expanse: " +expanse.getId()+" " + expanse.getAmount() +" " + expanse.getTitle()
                        + " " + expanse.getCreatedDate() + " " + expanse.getUser().getId());
                System.out.println("---------------------------------------");
            } else if (objectList.get(i) instanceof Budget) {
                Budget budget = new Budget(((Budget) objectList.get(i)).getId(),
                        ((Budget) objectList.get(i)).getTitle(), ((Budget) objectList.get(i)).getDescription(),
                        ((Budget) objectList.get(i)).getDateDebut(),((Budget) objectList.get(i)).getEndDate() ,
                        ((Budget) objectList.get(i)).getAmount());

                budget.setUser(((Budget) objectList.get(i)).getUser());
                System.out.println("Budget: " +budget.getId()+" " + budget.getAmount() +" " + budget.getTitle()
                        + " " + budget.getDateDebut() +" "+ budget.getEndDate()+" " + budget.getUser().getId());
            }
        }
    }



}
