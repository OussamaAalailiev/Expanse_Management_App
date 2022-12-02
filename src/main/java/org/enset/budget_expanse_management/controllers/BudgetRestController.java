package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.BudgetFormSubmission;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.BudgetRepository;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.enset.budget_expanse_management.repositories.UserRepository;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class BudgetRestController {

    private final BudgetRepository budgetRepository;

    private final CategoryExpanseRepository categoryExpanseRepository;

    private final UserRepository userRepository;
    private final BudgetExpanseManagementService managementService;

    public BudgetRestController(BudgetRepository budgetRepository,
                                CategoryExpanseRepository categoryExpanseRepository,
                                UserRepository userRepository,
                                BudgetExpanseManagementService managementService) {
        this.budgetRepository = budgetRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
        this.userRepository = userRepository;
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

    /*
    @GetMapping(path = "/budgets")
    public List<Budget> getAllBudgetsController(){
        //managementService.checkIfBudgetIsRespectedByCalculation();//this triggers a Budgets Amount spent Calculation:
        //managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
        return budgetRepository.findAll();
    }
     */

    /**Get Budgets By Page based on title of budget + page N° + Size N° && UserID: */
    @GetMapping(path = "/budgetsByUser")
    public Page<Budget> getBudgetsByPageAndSizeAndUserIdControllerV3(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> userId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size){
        return managementService
                .getBudgetsByPageAndSizeAndTitleAndUserIdService(
                        title.orElse(""), userId.orElse(""),page.orElse(0), size.orElse(5)
                );
    }

    /**Get Budgets By 'page', 'size' and 'title': */
    @GetMapping(path = "/budgets")
    public Page<Budget> getBudgetsByPageAndSizeControllerV2(@RequestParam Optional<String> title,
                                                              @RequestParam Optional<Integer> page,
                                                              @RequestParam Optional<Integer> size){
        return managementService
                .getBudgetsByPageAndSizeAndTitleService(
                        title.orElse(""), page.orElse(0), size.orElse(4)
                );
    }

    @GetMapping(path = "/budgets/{id}")
    public Budget getBudgetsByIdController(@PathVariable(name = "id") String id){
        if (budgetRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("Budget was NOT Found !");
        }
        return budgetRepository.findById(Integer.parseInt(id)).get();
    }

//    @PostMapping(path = "/budgets/admin")
//    public Budget addNewBudgetController(@RequestBody Budget budget){
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Budget is added Successfully ----------");
//        Budget budgetSaved = budgetRepository.save(budget);
//        return budgetSaved;
//    }
    @PostMapping(path = "/budgets/admin")
    public void addNewBudgetController(@RequestBody BudgetFormSubmission budgetFormSubmission){
       System.out.println(" -----------------------------------");
       System.out.println(" ------------- Budget is added Successfully ----------");
       Budget budget = managementService.mapNewFormBudgetObjToBudgetObj(budgetFormSubmission);
       managementService.calculateExpansesOnAddBudgetService(budget);
    }


    @PutMapping(path = "/budgets/admin/{id}")
    public void editBudgetController(@PathVariable(name = "id") String id ,@RequestBody Budget budgetUpdated){
        boolean isBudgetPresent = budgetRepository.findById(Integer.parseInt(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Budget is updated Successfully ----------");
        if (!isBudgetPresent){
            throw new RuntimeException("Budget is not found, please edit an existing Budget!");
        }
        /**I sent just 'Id, Amount, Title & Description of the 'budgetUpdated' from Frontend THEN
         *   i get the other Fields from DB THEN I set them to 'budgetUpdated' object
         *   before calling the Service Method: */
        Budget budgetFromDB = budgetRepository.findById(Integer.parseInt(id)).get();
        budgetUpdated.setDateDebut(budgetFromDB.getDateDebut());
        budgetUpdated.setEndDate(budgetFromDB.getEndDate());
        budgetUpdated.setAmountRemains(budgetFromDB.getAmountRemains());
        budgetUpdated.setAmountSpent(budgetFromDB.getAmountSpent());
        User user = userRepository.findById(budgetFromDB.getUser().getId()).get();
        budgetUpdated.setUser(user);
        CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(budgetFromDB.getCategoryExpanse().getId()).get();
        budgetUpdated.setCategoryExpanse(categoryExpanse);
        budgetUpdated.setId(Integer.parseInt(id));
        managementService.updateBudgetService(budgetUpdated);
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
