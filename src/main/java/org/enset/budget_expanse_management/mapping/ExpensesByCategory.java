package org.enset.budget_expanse_management.mapping;

import java.util.Date;
import java.util.UUID;

public class ExpensesByCategory {
    private Long id;
    private Double amount;
    private Date createdDate;
    private String title;
    private Integer category_expanse_id;
    private UUID userId;
    private Double SumExpensesByCategory;
    //private Integer NumberOfExpenses;
    private Double percentOfExpensesPerMonth;

    public ExpensesByCategory(Long id, Double amount, Date createdDate, String title,
                              Integer category_expanse_id, UUID userId, Double sumExpensesByCategory,
                              //,Integer numberOfExpenses,
                              Double percentOfExpensesPerMonth) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
        this.title = title;
        this.category_expanse_id = category_expanse_id;
        this.userId = userId;
        SumExpensesByCategory = sumExpensesByCategory;
        //NumberOfExpenses = numberOfExpenses;
        this.percentOfExpensesPerMonth = percentOfExpensesPerMonth;
    }

    public ExpensesByCategory(Long id, Double amount, Date createdDate, String title,
                              Integer category_expanse_id, UUID userId, Double sumExpensesByCategory
                              //,Integer numberOfExpenses
    ) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
        this.title = title;
        this.category_expanse_id = category_expanse_id;
        this.userId = userId;
        SumExpensesByCategory = sumExpensesByCategory;
      //  NumberOfExpenses = numberOfExpenses;
    }

//    public ExpensesByCategory(Long id, Double amount, Date createdDate, String title,
//                              Integer category_expanse_id, UUID userId, Double sumExpensesByCategory
//                              //,Integer numberOfExpenses
//    ) {
//        this.id = id;
//        this.amount = amount;
//        this.createdDate = createdDate;
//        this.title = title;
//        this.category_expanse_id = category_expanse_id;
//        this.userId = userId;
//        SumExpensesByCategory = sumExpensesByCategory;
//        //NumberOfExpenses = numberOfExpenses;
//    }

    public ExpensesByCategory() {

    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCategory_expanse_id() {
        return category_expanse_id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getSumExpensesByCategory() {
        return SumExpensesByCategory;
    }

    public Double getPercentOfExpensesPerMonth() {
        return percentOfExpensesPerMonth;
    }

    public void setPercentOfExpensesPerMonth(Double percentOfExpensesPerMonth) {
        this.percentOfExpensesPerMonth = percentOfExpensesPerMonth;
    }
}
