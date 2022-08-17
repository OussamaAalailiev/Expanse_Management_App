package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class ExpanseRestController {

    private final ExpanseRepository expanseRepository;
    private final BudgetExpanseManagementService managementService;

    public ExpanseRestController(ExpanseRepository expanseRepository,
                                 BudgetExpanseManagementService managementService) {
        this.expanseRepository = expanseRepository;
        this.managementService = managementService;
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

    @PostMapping(path = "/expanses/admin")
    public Expanse addNewExpanseController(@RequestBody Expanse expanse){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is added Successfully ----------");
        Expanse savedExpanse = expanseRepository.save(expanse);
        managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
        return savedExpanse;
    }

    @PutMapping(path = "/expanses/admin/{id}")
    public Expanse editExpanseController(@PathVariable(name = "id") String id ,@RequestBody Expanse expanse){
        boolean isExpansePresent = expanseRepository.findById(Long.valueOf(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is updated Successfully ----------");
        if (!isExpansePresent){
            throw new RuntimeException("Expanse is not found, please edit an existing Expanse!");
        }
        expanse.setId(Long.valueOf(id));
        Expanse savedExpanse = expanseRepository.save(expanse);
        managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
        return savedExpanse;
    }

    @DeleteMapping(path = "/expanses/admin/delete/{id}")
    public void deleteExpanse(@PathVariable(name = "id") String id){
        boolean isExpansePresent = expanseRepository.findById(Long.valueOf(id)).isPresent();
        if (!isExpansePresent){
            throw new RuntimeException("Expanse is not found, please delete an existing Expanse!");
        }
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is deleted Successfully ----------");
        Expanse expanseToBeDeleted = expanseRepository.findById(Long.valueOf(id)).get();
        expanseRepository.delete(expanseToBeDeleted);
    }


}
