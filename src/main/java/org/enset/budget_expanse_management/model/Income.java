package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;

import javax.persistence.*;
import java.util.Date;

//@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @Column(length = 55)
    private String title;
    @Temporal(TemporalType.DATE)//To format the date properly in Date format before being saved into DB:
    private Date createdDate;
//    @Enumerated(EnumType.STRING) @Column(length = 30)
//    private CategoryIncomeType categoryIncomeType;

    //@OneToOne(cascade = CascadeType.ALL)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne//Modified from "@OneToOne" to "@ManyToOne":
    private CategoryIncome categoryIncome;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Goal goal;

    public Income(Long id, Double amount, String title, Date createdDate){
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.createdDate = createdDate;
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

    public CategoryIncome getCategoryIncome() {
        return categoryIncome;
    }

    public void setCategoryIncome(CategoryIncome categoryIncome) {
        this.categoryIncome = categoryIncome;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
