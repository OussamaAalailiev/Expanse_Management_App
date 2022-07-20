package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
import org.enset.budget_expanse_management.repositories.IncomeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ExpanseRestController {

    private ExpanseRepository expanseRepository;

    public ExpanseRestController(ExpanseRepository expanseRepository) {
        this.expanseRepository = expanseRepository;
    }

    @GetMapping(path = "/expanses")
    public List<Expanse> getAllExpansesController(){
        return expanseRepository.findAll();
    }

    @GetMapping(path = "/expanses/{id}")
    public Expanse getExpansesByIdController(@PathVariable(name = "id") String id){
        if (expanseRepository.findById(Long.valueOf(id)).isEmpty()){
            throw new RuntimeException("expanse was NOT Found !");
        }
        return expanseRepository.findById(Long.valueOf(id)).get();
    }

}
