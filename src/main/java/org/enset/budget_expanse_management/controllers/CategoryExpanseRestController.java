package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Transactional
@RestController
@RequestMapping(path = "/api")
public class CategoryExpanseRestController {

    private CategoryExpanseRepository categoryExpanseRepository;

    public CategoryExpanseRestController(CategoryExpanseRepository categoryExpanseRepository) {
        this.categoryExpanseRepository = categoryExpanseRepository;
    }

    @GetMapping(path = "/categoryExpanses")
    public List<CategoryExpanse> getAllCategoryExpansesController(){
        return categoryExpanseRepository.findAll();
    }

    @GetMapping(path = "/categoryExpanses/{id}")
    public CategoryExpanse getCategoryExpansesByIdController(@PathVariable(name = "id") String id){
        if (categoryExpanseRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("CategoryExpanse was NOT Found !");
        }
        return categoryExpanseRepository.findById(Integer.parseInt(id)).get();
    }

}
