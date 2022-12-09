package org.enset.budget_expanse_management.formModel;

import lombok.Data;

import java.util.Date;

/** Class created to represent the data sent by user in Json Format While submitting a form: */

@Data
public class IncomeFormSubmission {
    private Double amount;
    private String title;
    private Date createdDate;
    private Integer categoryIncome;//It represents 'Id' of 'categoryIncome'.
    private String userId;
}
