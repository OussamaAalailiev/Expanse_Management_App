package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.ExpanseFormSubmission;
import org.enset.budget_expanse_management.mapping.ExpensesByCategory;
import org.enset.budget_expanse_management.mapping.TotalExpansePerMonthDTO;
import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
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

//    @GetMapping(path = "/expanses")
//    public List<Expanse> getAllExpansesControllerV1(){
//        return expanseRepository.findAll();
//    }
    /**Get Expanses By Page based on title of expanse + page N° + Size N° && UserID: */
    @GetMapping(path = "/expansesByUser")
    public Page<Expanse> getExpansesByPageAndSizeAndUserIdControllerV3(
                                                              @RequestParam Optional<String> title,
                                                              @RequestParam Optional<String> userId,
                                                              @RequestParam Optional<Integer> page,
                                                              @RequestParam Optional<Integer> size){
        return managementService
                .getExpansesByPageAndSizeAndTitleAndUserIdService(
                        title.orElse(""), userId.orElse(""),page.orElse(0), size.orElse(5)
                );
    }

//    @GetMapping(path = "/expansesSumByUser")
//    public Page<TotalExpansePerMonthDTO> getSumOfExpansesByYearMonthUserIdPageAndSize(
//            @RequestParam Optional<String> userId,
//            @RequestParam Optional<Integer> page,
//            @RequestParam Optional<Integer> size){
//        return managementService
//                .getTotalExpansesPerYearMonthAndUserService(
//                        userId.orElse(""),page.orElse(0), size.orElse(2)
//                );
//    }

    @GetMapping(path = "/expansesSumByUser/{id}")
    public List<TotalExpansePerMonthDTO> getSumOfExpansesByYearMonthUserIdPageAndSize(
            @PathVariable(name = "id") Optional<String> userId){
        System.out.println("Inside Controller -> expansesSumByUser");
        return managementService.getTotalExpansesPerYearMonthAndUserService(userId.orElse(""));
    }

    @GetMapping(path = "/expensesSumByCategoryAndUserId/{id}")
    public List<ExpensesByCategory> getExpensesByCategoryAndUserId(
            @PathVariable(name = "id") Optional<String> userId){
        return managementService.getExpensesSumByCategoryAndUserIdService(userId.orElse(""));
    }
    @GetMapping(path = "/expensesSumByCategoryAndUserIdAmountDesc/{id}")
    public List<ExpensesByCategory> getExpensesByCategoryAndUserIdAmountDesc(
            @PathVariable(name = "id") Optional<String> userId){
        return managementService.getExpensesSumByCategoryAndUserIdAmountDescService(userId.orElse(""));
    }

    @GetMapping(path = "/expanses")
    public Page<Expanse> getExpansesByPageAndSizeControllerV2(@RequestParam Optional<String> title,
                                                            @RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> size){
        return managementService
                .getExpansesByPageAndSizeAndTitleService(
                        title.orElse(""), page.orElse(0), size.orElse(5)
                                                        );
    }

    @GetMapping(path = "/expanses/{id}")
    public Expanse getExpansesByIdController(@PathVariable(name = "id") String id){
        if (expanseRepository.findById(Long.valueOf(id)).isEmpty()){
            throw new RuntimeException("expanse was NOT Found !");
        }
        return expanseRepository.findById(Long.valueOf(id)).get();
    }

//    @PostMapping(path = "/expanses/admin")
//    public Expanse addNewExpanseControllerV1(@RequestBody Expanse expanse){
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Expanse is added Successfully ----------");
//        Expanse savedExpanse = expanseRepository.save(expanse);
//        //managementService.checkIfBudgetIsRespectedByCalculationSumAmountExp();
//        return savedExpanse;
//    }

    @PostMapping(path = "/expanses/admin")
    public void addNewExpanseControllerV2(@RequestBody ExpanseFormSubmission expanseFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is added Successfully ----------");
        Expanse expanse = new Expanse();
        expanse.setTitle(expanseFormSubmission.getTitle());
        expanse.setAmount(expanseFormSubmission.getAmount());
        expanse.setCreatedDate(expanseFormSubmission.getCreatedDate());
//        CategoryExpanse categoryExpanse = categoryExpanseRepository
//                .findById(expanseFormSubmission.getCategoryExpanse()).get();
//        User user = userRepository
//                .findById(UUID.fromString(expanseFormSubmission.getUserId())).get();
        CategoryExpanse categoryExpanse = categoryExpanseRepository
                .findById(expanseFormSubmission.getCategoryExpanse()).orElseThrow(
                        () -> {
                            throw new RuntimeException("No such 'CategoryExpanse' was found!");
                        }
                );
        User user = userRepository
                .findById(UUID.fromString(expanseFormSubmission.getUserId())).orElseThrow(
                        () -> {
                            throw new RuntimeException("No such 'User' was found!");
                        }
                );
        expanse.setCategoryExpanse(categoryExpanse);
        expanse.setUser(user);

        managementService.calculateBudgetsOnAddExpanseService(expanse);
    }

    @PutMapping(path = "/expanses/admin/{id}")
    public void editExpanseController(@PathVariable(name = "id") String id ,
                                      @RequestBody Expanse expanseUpdated) {
        /**I sent just 'Id, Amount, Title of the 'expanseUpdated' from Frontend THEN
         *   i get the other Fields from DB THEN I set them to 'expanseUpdated' object before
         *   calling the Service Method: */
        boolean isExpansePresent = expanseRepository.findById(Long.valueOf(id)).isPresent();
        Expanse expanseFromDB = expanseRepository.findById(Long.valueOf(id)).get();
        expanseUpdated.setCategoryExpanse(expanseFromDB.getCategoryExpanse());
        expanseUpdated.setUser(expanseFromDB.getUser());
        expanseUpdated.setCreatedDate(expanseFromDB.getCreatedDate());
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Expanse is updated Successfully ----------");
        if (!isExpansePresent){
            throw new RuntimeException("Expanse is not found, please edit an existing Expanse!");
        }
        expanseUpdated.setId(Long.valueOf(id));
        managementService.calculateBudgetsOnUpdateExpanseService(expanseUpdated);
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
        managementService.calculateBudgetsOnDeleteExpanseService(expanseToBeDeleted);
    }


}
