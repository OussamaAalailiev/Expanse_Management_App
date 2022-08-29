package org.enset.budget_expanse_management.formModel;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/** Class created to represent the data sent by user in Json Format While submitting a form: */

@Data
public class ExpanseFormSubmission {
    private Double amount;
    private String title;
    private Date createdDate;
    private Integer categoryExpanse;//It represents 'Id' of 'categoryExpanse'.
    private String userId;
}
