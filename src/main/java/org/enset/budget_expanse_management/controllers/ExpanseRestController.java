package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.ExpanseFormSubmission;
import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
import org.enset.budget_expanse_management.repositories.UserRepository;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class ExpanseRestController {

    private final ExpanseRepository expanseRepository;
    private final CategoryExpanseRepository categoryExpanseRepository;
    private final UserRepository userRepository;
    private final BudgetExpanseManagementService managementService;

    public ExpanseRestController(ExpanseRepository expanseRepository,
                                 CategoryExpanseRepository categoryExpanseRepository,
                                 UserRepository userRepository,
                                 BudgetExpanseManagementService managementService) {
        this.expanseRepository = expanseRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
        this.userRepository = userRepository;
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

//    @PostMapping(path = "/expanses/admin")
//    public Expanse addNewExpanseController(@RequestBody Expanse expanse){
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Expanse is added Successfully ----------");
//        Expanse savedExpanse = expanseRepository.save(expanse);
//        //managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
//        return savedExpanse;
//    }

    @PostMapping(path = "/expanses/admin")
    public void addNewExpanseController2(@RequestBody ExpanseFormSubmission expanseFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is added Successfully ----------");
        Expanse expanse = new Expanse();
        //expanse.setId(null);
        expanse.setTitle(expanseFormSubmission.getTitle());
        expanse.setAmount(expanseFormSubmission.getAmount());
        expanse.setCreatedDate(expanseFormSubmission.getCreatedDate());
        CategoryExpanse categoryExpanse = categoryExpanseRepository
                .findById(expanseFormSubmission.getCategoryExpanse()).get();
        User user = userRepository
                .findById(UUID.fromString(expanseFormSubmission.getUserId())).get();
        expanse.setCategoryExpanse(categoryExpanse);
        expanse.setUser(user);

//        Expanse savedExpanse = expanseRepository.save(expanse);
//        //managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
//        return savedExpanse;
        managementService.calculateBudgetsOnAddExpanseService(expanse);
    }

    @PutMapping(path = "/expanses/admin/{id}")
    public void editExpanseController(@PathVariable(name = "id") String id ,@RequestBody Expanse expanse){
        boolean isExpansePresent = expanseRepository.findById(Long.valueOf(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is updated Successfully ----------");
        if (!isExpansePresent){
            throw new RuntimeException("Expanse is not found, please edit an existing Expanse!");
        }
        expanse.setId(Long.valueOf(id));
       // Expanse savedExpanse = expanseRepository.save(expanse);
        managementService.calculateBudgetsOnUpdateExpanseService(expanse);
       // managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
        //return savedExpanse;
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
//        expanseRepository.delete(expanseToBeDeleted);
        managementService.calculateBudgetsOnDeleteExpanseService(expanseToBeDeleted);
    }


}
