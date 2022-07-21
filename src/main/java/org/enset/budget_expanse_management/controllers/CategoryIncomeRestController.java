package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.CategoryIncome;
import org.enset.budget_expanse_management.repositories.CategoryIncomeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Transactional
@RestController
@RequestMapping(path = "/api")
public class CategoryIncomeRestController {

    private CategoryIncomeRepository categoryIncomeRepository;

    public CategoryIncomeRestController(CategoryIncomeRepository categoryIncomeRepository) {
        this.categoryIncomeRepository = categoryIncomeRepository;
    }

    @GetMapping(path = "/categoryIncomes")
    public List<CategoryIncome> getAllCategoryIncomesController(){
        return categoryIncomeRepository.findAll();
    }

    @GetMapping(path = "/categoryIncomes/{id}")
    public CategoryIncome getCategoryIncomesByIdController(@PathVariable(name = "id") String id){
        if (categoryIncomeRepository.findById(Integer.parseInt(id)).isEmpty()){
            throw new RuntimeException("CategoryIncome was NOT Found !");
        }
        return categoryIncomeRepository.findById(Integer.parseInt(id)).get();
    }

}
