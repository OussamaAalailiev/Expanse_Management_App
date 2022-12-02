package org.enset.budget_expanse_management.formModel;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/** Class created to represent the data sent by user in Json Format While submitting a form: */

@Data
public class GoalFormSubmission {
    private Double amount;
    private String title;
    private String description;
    private Date dateDebut;
    private LocalDate endDate;
    private Integer categoryIncome;//It represents 'Id' of 'categoryIncome'.
    private String userId;
}
