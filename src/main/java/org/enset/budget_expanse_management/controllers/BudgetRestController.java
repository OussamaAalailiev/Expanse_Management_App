package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.repositories.BudgetRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Transactional
@RestController
@RequestMapping(path = "/api")
public class BudgetRestController {

    private BudgetRepository budgetRepository;

    public BudgetRestController(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @GetMapping(path = "/budgets")
    public List<Budget> getAllBudgetsController(){
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
        budgetRepository.delete(budgetToBeDeleted);
    }


}
