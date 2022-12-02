package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.GoalFormSubmission;
import org.enset.budget_expanse_management.model.CategoryIncome;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.CategoryIncomeRepository;
import org.enset.budget_expanse_management.repositories.GoalRepository;
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
public class GoalRestController {

    private final GoalRepository goalRepository;
    private final BudgetExpanseManagementService managementService;

    private final CategoryIncomeRepository categoryIncomeRepository;
    private final UserRepository userRepository;

    public GoalRestController(GoalRepository goalRepository,
                              BudgetExpanseManagementService managementService,
                              CategoryIncomeRepository categoryIncomeRepository,
                              UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.managementService = managementService;
        this.categoryIncomeRepository = categoryIncomeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/goals")
    public List<Goal> getAllGoalsController(){
        return goalRepository.findAll();
    }

    @GetMapping(path = "/goalsByUserId")
    public Page<Goal> getGoalsByPageAndSizeAndUserIdControllerV2(@RequestParam Optional<String> title,
                                                                 @RequestParam Optional<String> userId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size){
        return managementService
                .getGoalsByPageAndSizeAndTitleAndUserIdService(
                        title.orElse(""), userId.orElse(""),
                        page.orElse(0), size.orElse(3));
    }

    @GetMapping(path = "/goals/{id}")
    public Goal getGoalsByIdController(@PathVariable(name = "id") String id){
        if (goalRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("Goal was NOT Found !");
        }
        return goalRepository.findById(Integer.parseInt(id)).get();
    }

//    @PostMapping(path = "/goals/admin")
//    public Goal addNewGoalController(@RequestBody Goal goal){
//        System.out.println(" -----------------------------------");
//        System.out.println(" ------------- Goal is added Successfully ----------");
//        Goal savedGoal = goalRepository.save(goal);
//        return savedGoal;
//    }
    @PostMapping(path = "/goals/addGoal")
    public void addNewGoalController(@RequestBody GoalFormSubmission goalFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Goal is added Successfully ----------");

        Goal goal = managementService.mapNewFormGoalObjToGoalObj(goalFormSubmission);
        //managementService.calculateIncomesOnAddGoalService(goal);
        managementService.calculateIncomesOnAddGoalServiceV2(goal);
    }

    @PutMapping(path = "/goals/admin/{id}")
    public Goal editGoalController(@PathVariable(name = "id") String id ,@RequestBody Goal goal){
        boolean isGoalPresent = goalRepository.findById(Integer.parseInt(id)).isPresent();
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Goal is updated Successfully ----------");
        if (!isGoalPresent){
            throw new RuntimeException("Goal is not found, please edit an existing goal!");
        }
        goal.setId(Integer.parseInt(id));
        return goalRepository.save(goal);
    }

    //TODO: Method not tested yet!
    @DeleteMapping(path = "/goals/delete/{id}")
    public void deleteGoal(@PathVariable(name = "id") String id){
       managementService.deleteGoalService(Integer.valueOf(id));
    }

}
