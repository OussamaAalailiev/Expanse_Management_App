package org.enset.budget_expanse_management.controllers;

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
        Income savedIncome = incomeRepository.save(income);
        return savedIncome;
    }

    @PutMapping(path = "/incomes/admin/{id}")
    public Income editIncomeController(@PathVariable(name = "id") String id ,@RequestBody Income income){
        boolean isIncomePresent = incomeRepository.findById(Long.valueOf(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is updated Successfully ----------");
        if (!isIncomePresent){
            throw new RuntimeException("Income is not found, please edit an existing income!");
        }
        income.setId(Long.valueOf(id));
        return incomeRepository.save(income);
    }

    @DeleteMapping(path = "/incomes/admin/delete/{id}")
    public void deleteIncome(@PathVariable(name = "id") String id){
        boolean isIncomePresent = incomeRepository.findById(Long.valueOf(id)).isPresent();
        if (!isIncomePresent){
            throw new RuntimeException("Income is not found, please delete an existing income!");
        }
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Income is deleted Successfully ----------");
        Income incomeToBeDeleted = incomeRepository.findById(Long.valueOf(id)).get();
        incomeRepository.delete(incomeToBeDeleted);
    }

}
