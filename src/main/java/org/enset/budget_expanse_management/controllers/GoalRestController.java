package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.repositories.GoalRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4090")
@Transactional
@RestController
@RequestMapping(path = "/api")
public class GoalRestController {

    private final GoalRepository goalRepository;

    public GoalRestController(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @GetMapping(path = "/goals")
    public List<Goal> getAllGoalsController(){
        return goalRepository.findAll();
    }

    @GetMapping(path = "/goals/{id}")
    public Goal getGoalsByIdController(@PathVariable(name = "id") String id){
        if (goalRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("Goal was NOT Found !");
        }
        return goalRepository.findById(Integer.parseInt(id)).get();
    }

    @PostMapping(path = "/goals/admin")
    public Goal addNewGoalController(@RequestBody Goal goal){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Goal is added Successfully ----------");
        Goal savedGoal = goalRepository.save(goal);
        return savedGoal;
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

    @DeleteMapping(path = "/goals/admin/delete/{id}")
    public void deleteGoal(@PathVariable(name = "id") String id){
        boolean isGoalPresent = goalRepository.findById(Integer.parseInt(id)).isPresent();
        if (!isGoalPresent){
            throw new RuntimeException("Goal is not found, please delete an existing Goal!");
        }
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- Goal is deleted Successfully ----------");
        Goal goalToBeDeleted = goalRepository.findById(Integer.parseInt(id)).get();
        goalRepository.delete(goalToBeDeleted);
    }

}
