package org.enset.budget_expanse_management.mapping;

import java.util.Date;
import java.util.UUID;

public class IncomesByCategory {
    private Long id;
    private Double amount;
    private Date createdDate;
    private String title;
    private Integer category_income_id;
    private UUID userId;
    private Double totalIncomesByCategory;
    //private Integer numberOfIncomes;
    private Double percentOfIncomesPerMonth;

    public IncomesByCategory(Long id, Double amount, Date createdDate, String title,
                             Integer category_income_id, UUID userId, Double totalIncomesByCategory,
                             //,Integer numberOfExpenses,
                             Double percentOfIncomesPerMonth) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
        this.title = title;
        this.category_income_id = category_income_id;
        this.userId = userId;
        this.totalIncomesByCategory = totalIncomesByCategory;
        this.percentOfIncomesPerMonth = percentOfIncomesPerMonth;
    }

    public IncomesByCategory(Long id, Double amount, Date createdDate, String title,
                             Integer category_income_id, UUID userId, Double totalIncomesByCategory

    ) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
        this.title = title;
        this.category_income_id = category_income_id;
        this.userId = userId;
        this.totalIncomesByCategory = totalIncomesByCategory;
    }

    public IncomesByCategory() {

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

    public Integer getCategory_income_id() {
        return category_income_id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getTotalIncomesByCategory() {
        return totalIncomesByCategory;
    }

    public Double getPercentOfIncomesPerMonth() {
        return percentOfIncomesPerMonth;
    }

    public void setPercentOfIncomesPerMonth(Double percentOfIncomesPerMonth) {
        this.percentOfIncomesPerMonth = percentOfIncomesPerMonth;
    }
}
