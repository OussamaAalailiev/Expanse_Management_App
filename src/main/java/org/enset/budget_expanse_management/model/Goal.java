package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.GoalCategoryType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 55)
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateDebut;//This 'dateDebut' refer to the current date.
    private LocalDate endDate;
//    @Column(length = 30)
//    @Enumerated(EnumType.STRING)//This annotation will let the DataBase to save the enum value in 'goalCategory' TEXT:
//    private GoalCategoryType goalCategoryType;
    private Double amount;//This field was just added, Not tested yet! it is bound to User's positive sold:

    private Double amountAchieved;//New Column added!
    private Boolean goalAchieved;//True or False.
//    @ManyToOne(cascade = CascadeType.ALL)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private CategoryIncome categoryIncome;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "goal")
    private List<Income> incomes;

    public Goal(Integer id, String title, String description, Date dateDebut, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountAchieved() {
        return amountAchieved;
    }

    public void setAmountAchieved(Double amountAchieved) {
        this.amountAchieved = amountAchieved;
    }

    public Boolean getGoalAchieved() {
        return goalAchieved;
    }

    public void setGoalAchieved(Boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
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

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }
}
