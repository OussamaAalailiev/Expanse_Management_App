package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//@Data
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "Users")
public class User {
    //Class fields:
    @Id
    @GeneratedValue(generator = "uuid2")// String by UUID across all app portions gives us
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(length = 55, unique = true)
    private String name;
    private Double totalSold;
    private Boolean active;//To check if User's account is active or inactive:
    @Column(length = 80, unique = true)
    private String email;
//    @Column(length = 35)
//    private String password;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private UserCurrency currency;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    //Class fields Relations to Other Classes Via JPA Annotations '@...(..)':
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    private List<Income> incomeList;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    private List<Expanse>  expanseList;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    private List<Goal> goalList;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    private List<Budget> budgetList;


    public User(UUID id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Double totalSold) {
        this.totalSold = totalSold;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(UserCurrency currency) {
        this.currency = currency;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    public List<Expanse> getExpanseList() {
        return expanseList;
    }

    public void setExpanseList(List<Expanse> expanseList) {
        this.expanseList = expanseList;
    }

    public List<Goal> getGoalList() {
        return goalList;
    }

    public void setGoalList(List<Goal> goalList) {
        this.goalList = goalList;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List<Budget> budgetList) {
        this.budgetList = budgetList;
    }
}
