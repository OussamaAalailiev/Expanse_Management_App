package org.enset.budget_expanse_management.mapping;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data @NoArgsConstructor //@AllArgsConstructor
public class ResultDTOGoalAndIncomes {
    private Integer idGoal;
    private Double amountGoal;
    private Double amountAchieved;
    private Boolean goalAchieved;
    private Date dateDebut;
    private String description;
    private LocalDate endDate;
    private String titleGoal;
    private Integer category_income_id_Goal;
    private UUID userIdGoal;

    private Long idIncome;
    private Double amountIncome;
    private Date createdDate;
    private String titleIncome;
    private Integer category_income_id_Income;
    private UUID userIdIncome;
    private Double amountIncomeSum;

    public ResultDTOGoalAndIncomes(
                                   Integer idGoal, Double amountGoal,
                                   Double amountAchieved, Boolean goalAchieved, Date dateDebut,
                                   String description, LocalDate endDate, String titleGoal,
                                   Integer category_income_id_Goal, UUID userIdGoal,
                                   Long idIncome, Double amountIncome, Date createdDate,
                                   String titleIncome, Integer category_income_id_Income,
                                   UUID userIdIncome, Double amountIncomeSum) {
        this.idIncome = idIncome;
        this.amountIncome = amountIncome;
        this.createdDate = createdDate;
        this.titleIncome = titleIncome;
        this.category_income_id_Income = category_income_id_Income;
        this.userIdIncome = userIdIncome;
        this.idGoal = idGoal;
        this.amountGoal = amountGoal;
        this.amountAchieved = amountAchieved;
        this.goalAchieved = goalAchieved;
        this.dateDebut = dateDebut;
        this.description = description;
        this.endDate = endDate;
        this.titleGoal = titleGoal;
        this.category_income_id_Goal = category_income_id_Goal;
        this.userIdGoal = userIdGoal;
        this.amountIncomeSum = amountIncomeSum;
    }

    public ResultDTOGoalAndIncomes(
                                   Integer idGoal, Double amountGoal,
                                   Double amountAchieved, Boolean goalAchieved, Date dateDebut,
                                   String description, LocalDate endDate, String titleGoal,
                                   Integer category_income_id_Goal, UUID userIdGoal,
                                   Long idIncome, Double amountIncome, Date createdDate,
                                   String titleIncome, Integer category_income_id_Income,
                                   UUID userIdIncome) {
        this.idIncome = idIncome;
        this.amountIncome = amountIncome;
        this.createdDate = createdDate;
        this.titleIncome = titleIncome;
        this.category_income_id_Income = category_income_id_Income;
        this.userIdIncome = userIdIncome;
        this.idGoal = idGoal;
        this.amountGoal = amountGoal;
        this.amountAchieved = amountAchieved;
        this.goalAchieved = goalAchieved;
        this.dateDebut = dateDebut;
        this.description = description;
        this.endDate = endDate;
        this.titleGoal = titleGoal;
        this.category_income_id_Goal = category_income_id_Goal;
        this.userIdGoal = userIdGoal;
    }

    //private CategoryExpanse categoryExpanse;
    //private User user;

}
