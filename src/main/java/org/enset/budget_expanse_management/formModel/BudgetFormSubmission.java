package org.enset.budget_expanse_management.formModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
public class BudgetFormSubmission {
    private Double amount;
    private String title;
    private String description;
    private Date dateDebut;
    private LocalDate endDate;
    private Integer categoryExpanse;//It represents 'Id' of 'categoryExpanse'.
    private String userId;
}
