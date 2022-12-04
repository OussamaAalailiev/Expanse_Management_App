package org.enset.budget_expanse_management.mapping;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class CommonGoal {
    private Integer id;
    private Double amount;
    private String title;
    private Date dateDebut;
    private LocalDate endDate;
    private Integer categoryIncomeId;
    private UUID userId;
    private Double amountAchieved;
    private Boolean goalAchieved;

    public CommonGoal(Integer id, Double amount, Date dateDebut, LocalDate endDate, Boolean goalAchieved, String title,
                      Integer categoryIncomeId, UUID userId, Double amountAchieved) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.dateDebut = dateDebut;
        this.endDate = endDate;
        this.categoryIncomeId = categoryIncomeId;
        this.userId = userId;
        this.amountAchieved = amountAchieved;
        this.goalAchieved = goalAchieved;
    }



}
