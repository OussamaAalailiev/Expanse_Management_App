package org.enset.budget_expanse_management.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Entity
public class CommonIncomeGoal implements Serializable {
    @EmbeddedId
    private CommonIncomeGoalID id;
    
}
