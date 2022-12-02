package org.enset.budget_expanse_management.mapping;

import org.enset.budget_expanse_management.model.CategoryIncome;
import org.enset.budget_expanse_management.model.User;

import java.util.Date;
import java.util.UUID;

public class CommonIncome {
    private Long id;
    private Double amount;
    private String title;
    private Date createdDate;
    private Integer categoryIncomeId;
    private UUID userId;
    private Double amountSum;

    public CommonIncome(Long id, Double amount, String title, Date createdDate, Integer categoryIncomeId, UUID userId) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.createdDate = createdDate;
        this.categoryIncomeId = categoryIncomeId;
        this.userId = userId;
    }
    public CommonIncome(Long id, Double amount, String title, Date createdDate, Integer categoryIncomeId, UUID userId, Double amountSum) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.createdDate = createdDate;
        this.categoryIncomeId = categoryIncomeId;
        this.userId = userId;
        this.amountSum = amountSum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCategoryIncomeId() {
        return categoryIncomeId;
    }

    public void setCategoryIncomeId(Integer categoryIncomeId) {
        this.categoryIncomeId = categoryIncomeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Double getAmountSum() {
        return amountSum;
    }

    public void setAmountSum(Double amountSum) {
        this.amountSum = amountSum;
    }
}
