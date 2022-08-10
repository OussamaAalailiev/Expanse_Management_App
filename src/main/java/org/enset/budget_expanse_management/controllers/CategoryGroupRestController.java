package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.CategoryGroup;
import org.enset.budget_expanse_management.repositories.CategoryGroupExpansesRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Transactional
@RestController
@RequestMapping(path = "/api")
public class CategoryGroupRestController {

    private final CategoryGroupExpansesRepository categoryGroupExpansesRepository;

    public CategoryGroupRestController(CategoryGroupExpansesRepository categoryGroupExpansesRepository) {
        this.categoryGroupExpansesRepository = categoryGroupExpansesRepository;
    }

    @GetMapping(path = "/categoryGroups")
    public List<CategoryGroup> getAllCategoryGroupsController(){
        return categoryGroupExpansesRepository.findAll();
    }

    @GetMapping(path = "/categoryGroups/{id}")
    public CategoryGroup getCategoryExpansesByIdController(@PathVariable(name = "id") String id){
        if (categoryGroupExpansesRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("CategoryGroup was NOT Found !");
        }
        return categoryGroupExpansesRepository.findById(Integer.parseInt(id)).get();
    }

}
