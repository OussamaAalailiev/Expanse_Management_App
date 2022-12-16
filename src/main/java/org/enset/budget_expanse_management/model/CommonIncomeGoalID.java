package org.enset.budget_expanse_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor @AllArgsConstructor
@Embeddable
public class CommonIncomeGoalID implements Serializable {
    private Long incomeId;
    private Integer goalId;

}
