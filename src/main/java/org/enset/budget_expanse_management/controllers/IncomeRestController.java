package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.IncomeFormSubmission;
import org.enset.budget_expanse_management.mapping.IncomesByCategory;
import org.enset.budget_expanse_management.mapping.TotalIncomesPerMonthDTO;
import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.repositories.IncomeRepository;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class IncomeRestController {

    private final IncomeRepository incomeRepository;
    private final BudgetExpanseManagementService managementService;

    public IncomeRestController(IncomeRepository incomeRepository, BudgetExpanseManagementService managementService) {
        this.incomeRepository = incomeRepository;
        this.managementService = managementService;
    }

    @GetMapping(path = "/incomes")
    public List<Income> getAllIncomesController(){
        return incomeRepository.findAll();
    }

    @GetMapping(path = "/incomesByUserId")
    public Page<Income> getGoalsByPageAndSizeAndUserIdControllerV2(@RequestParam Optional<String> title,
                                                                 @RequestParam Optional<String> userId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size){
        return managementService
                .getIncomesByPageAndSizeAndTitleAndUserIdService(
                        title.orElse(""), userId.orElse(""),
                        page.orElse(0), size.orElse(2));
    }

    @GetMapping(path = "/incomes/{id}")
    public Income getIncomeByIdController(@PathVariable(name = "id") String id){
        if (incomeRepository.findById(Long.valueOf(id)).isEmpty()){
            throw new RuntimeException("Income was NOT Found !");
        }
        return incomeRepository.findById(Long.valueOf(id)).get();
    }

    @PostMapping(path = "/incomes/admin")
    public Income addNewIncomeController(@RequestBody Income income){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is added Successfully ----------");
        return incomeRepository.save(income);
    }

//    @PutMapping(path = "/incomes/admin/{id}")
//    public Income editIncomeController(@PathVariable(name = "id") String id ,@RequestBody Income income){
//        boolean isIncomePresent = incomeRepository.findById(Long.valueOf(id)).isPresent();
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Income is updated Successfully ----------");
//        if (!isIncomePresent){
//            throw new RuntimeException("Income is not found, please edit an existing income!");
//        }
//        income.setId(Long.valueOf(id));
//        return incomeRepository.save(income);
//    }

//    @DeleteMapping(path = "/incomes/admin/delete/{id}")
//    public void deleteIncome(@PathVariable(name = "id") String id){
//        boolean isIncomePresent = incomeRepository.findById(Long.valueOf(id)).isPresent();
//        if (!isIncomePresent){
//            throw new RuntimeException("Income is not found, please delete an existing income!");
//        }
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Income is deleted Successfully ----------");
//        Income incomeToBeDeleted = incomeRepository.findById(Long.valueOf(id)).get();
//        incomeRepository.delete(incomeToBeDeleted);
//    }

    @GetMapping(path = "/incomes/incomesSumByUser/{id}")
    public List<TotalIncomesPerMonthDTO> getSumOfIncomesByYearMonthUserId(
            @PathVariable(name = "id") Optional<String> userId){
        System.out.println("Inside Controller -> incomesSumByUser");
        return managementService.getTotalIncomesPerYearMonthAndUserService(userId.orElse(""));
    }

    @GetMapping(path = "/incomes/incomesBalanceByUser/{id}")
    public TotalIncomesPerMonthDTO getSumOfIncomesForLifeTimeByUserId(
            @PathVariable(name = "id") Optional<String> userId){
        System.out.println("Inside Controller -> incomes Balance ByUser");
        return managementService.getTotalIncomesPerLifeTimeAndUserService(userId.orElse(""));
    }

    @GetMapping(path = "/incomes/incomesByCategoryAndUserIdAmountDesc/{id}")
    public List<IncomesByCategory> getIncomesByCategoryAndUserId(
            @PathVariable(name = "id") Optional<String> userId){
        return managementService.getIncomesSumByCategoryAndUserIdService(userId.orElse(""));
    }
    @GetMapping(path = "/incomes/incomesByCategoryAndUserIdDateDesc/{id}")
    public List<IncomesByCategory> getIncomesByCategoryAndUserIdDateDesc(
            @PathVariable(name = "id") Optional<String> userId){
        return managementService.getIncomesSumByCategoryAndUserIdDateDescService(userId.orElse(""));
    }

    @PostMapping(path = "/incomes/addIncome")
    public void addNewIncomeController(@RequestBody IncomeFormSubmission incomeFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is added Successfully ----------");

        Income income = managementService.mapNewFormIncomeObjToIncomeObj(incomeFormSubmission);
        managementService.calculateGoalsOnAddIncomeServiceV2(income);
    }

    @DeleteMapping(path = "/incomes/delete/{id}")
    public void deleteIncome(@PathVariable(name = "id") String id){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is deleted Successfully ----------");
        managementService.deleteIncomeService(Long.valueOf(id));
    }

    @PutMapping(path = "/incomes/edit/{id}")
    public void editIncomeController(@PathVariable(name = "id") String id ,
                                     @RequestBody IncomeFormSubmission incomeFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is updated Successfully ----------");
        Income income = managementService.mapNewFormIncomeObjToIncomeObj(incomeFormSubmission);
        income.setId(Long.valueOf(id));//In order to preserve the old 'Id' & Not be overwritten by a new one created.
        managementService.updateIncomeServiceV2(income);
    }



}
