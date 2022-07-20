package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.IncomeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public class IncomeRestController {

    private IncomeRepository incomeRepository;

    public IncomeRestController(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @GetMapping(path = "/incomes")
    public List<Income> getAllIncomesController(){
        return incomeRepository.findAll();
    }

    @GetMapping(path = "/incomes/{id}")
    public Income getIncomeByIdController(@PathVariable(name = "id") String id){
        if (incomeRepository.findById(Long.valueOf(id)).isEmpty()){
            throw new RuntimeException("Income was NOT Found !");
        }
        return incomeRepository.findById(Long.valueOf(id)).get();
    }

}
