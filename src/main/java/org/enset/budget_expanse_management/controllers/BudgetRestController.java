package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.repositories.BudgetRepository;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class BudgetRestController {

    private final BudgetRepository budgetRepository;
    private final BudgetExpanseManagementService managementService;

    public BudgetRestController(BudgetRepository budgetRepository,
                                BudgetExpanseManagementService managementService) {
        this.budgetRepository = budgetRepository;
        this.managementService = managementService;
    }

//    @GetMapping(path = "/budgets")
//    public List<Budget> getAllBudgetsController(){
//        //Algorithm To check If a Budget is respected On Add an Expanse,
//        // by getting from Exp 'user.id' + 'amount' + 'CategoryExpanse' + 'createdDate':
////        List<Budget> allBudgetList = budgetRepository.findAll();
//
//        List<Budget> allBudgetList = budgetRepository.findAll();
//        if (!allBudgetList.isEmpty()){
//
//            for (Budget budget : allBudgetList) {
//                Double totalAmountSpent;
//                Double amountRemainsCalculated;
//
//                Boolean isUserIsNotNull = expanse.getUser()!=null && budget.getUser()!=null;
//                Boolean sameUserId = (expanse.getUser().getId() == budget.getUser().getId());
//                Boolean sameCategoryExpanse = (expanse.getCategoryExpanse().getId().equals(budget.getId()));
//                Boolean betweenSameDate = (expanse.getCreatedDate().compareTo(budget.getDateDebut()) == 0)
//                        || (expanse.getCreatedDate().compareTo(Date.valueOf(budget.getEndDate())) == 0)
//                        && (expanse.getCreatedDate().compareTo(Date.valueOf(budget.getEndDate())) <= 0);
//                Boolean isBudgetAmountSpentStillLess = budget.getAmountSpent() < budget.getAmount();
//
//                if (sameUserId && sameCategoryExpanse && betweenSameDate
//                        && isBudgetAmountSpentStillLess && isUserIsNotNull) {
//                    totalAmountSpent = budget.getAmountSpent() + budget.getAmount();
//                    amountRemainsCalculated = budget.getAmountRemains() - expanse.getAmount();
//                    budget.setAmountSpent(totalAmountSpent);
//                    budget.setAmountRemains(amountRemainsCalculated);
//                    budgetRepository.save(budget);
//                }
//            }
//        }
//        //return ;
//        return budgetRepository.findAll();
//    }

    @GetMapping(path = "/budgets")
    public List<Budget> getAllBudgetsController(){
        //managementService.checkIfBudgetIsRespectedByCalculation();//this triggers a Budgets Amount spent Calculation:
        //managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
        return budgetRepository.findAll();
    }

    @GetMapping(path = "/budgets/{id}")
    public Budget getBudgetsByIdController(@PathVariable(name = "id") String id){
        if (budgetRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("Budget was NOT Found !");
        }
        return budgetRepository.findById(Integer.parseInt(id)).get();
    }

    @PostMapping(path = "/budgets/admin")
    public Budget addNewBudgetController(@RequestBody Budget budget){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Budget is added Successfully ----------");
        Budget budgetSaved = budgetRepository.save(budget);
        return budgetSaved;
    }

    @PutMapping(path = "/budgets/admin/{id}")
    public Budget editBudgetController(@PathVariable(name = "id") String id ,@RequestBody Budget budget){
        boolean isBudgetPresent = budgetRepository.findById(Integer.parseInt(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Budget is updated Successfully ----------");
        if (!isBudgetPresent){
            throw new RuntimeException("Budget is not found, please edit an existing Budget!");
        }
        budget.setId(Integer.parseInt(id));
        return budgetRepository.save(budget);
    }

    @DeleteMapping(path = "/budgets/admin/delete/{id}")
    public void deleteBudget(@PathVariable(name = "id") String id){
        boolean isBudgetPresent = budgetRepository.findById(Integer.parseInt(id)).isPresent();
        if (!isBudgetPresent){
            throw new RuntimeException("Budget is not found, please delete an existing Budget!");
        }
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Budget is deleted Successfully ----------");
        Budget budgetToBeDeleted = budgetRepository.findById(Integer.parseInt(id)).get();
       // budgetRepository.delete(budgetToBeDeleted);
        managementService.deleteBudgetService(budgetToBeDeleted);
    }


}
