package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.formModel.GoalFormSubmission;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.repositories.GoalRepository;
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
public class GoalRestController {

    private final GoalRepository goalRepository;
    private final BudgetExpanseManagementService managementService;

    public GoalRestController(GoalRepository goalRepository,
                              BudgetExpanseManagementService managementService) {
        this.goalRepository = goalRepository;
        this.managementService = managementService;

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

    @PutMapping(path = "/goals/edit/{id}")
    public void editGoalController(@PathVariable(name = "id") String id ,@RequestBody GoalFormSubmission goalFormSubmission){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Goal is updated Successfully ----------");
        Goal goal = managementService.mapNewFormGoalObjToGoalObj(goalFormSubmission);
        goal.setId(Integer.parseInt(id));
        managementService.updateGoalService(goal);
    }

    //TODO: Method not tested yet!
    @DeleteMapping(path = "/goals/delete/{id}")
    public void deleteGoal(@PathVariable(name = "id") String id){
       managementService.deleteGoalService(Integer.valueOf(id));
    }

}
