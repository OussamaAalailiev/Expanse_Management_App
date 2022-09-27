package org.enset.budget_expanse_management.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class ResultDTOExpansesBudgets {

    private Long id;
    private Double amountExpanse;
    private Date createdDate;
    private String title;
//    private Integer budgetIdExp;
    private Integer category_expanse_idEXP;
    private UUID userIdEXP;

    private Integer idBudget;
    private Double amountBudget;
    private Double amountRemains;
    private Double amountSpent;
    private Date dateDebut;
    private String description;
    private LocalDate endDate;
    private String titleBudget;
    private Integer category_expanse_idBUD;
    private UUID userIdBUD;
    private Double amountExpanseSum;

    public ResultDTOExpansesBudgets(Long id, Double amountExpanse, Date createdDate,
                                    String title, Integer category_expanse_idEXP,
                                    UUID userIdEXP, Integer idBudget,Double amountBudget,
                                    Double amountRemains, Double amountSpent, Date dateDebut,
                                    String description, LocalDate endDate, String titleBudget,
                                    Integer category_expanse_idBUD, UUID userIdBUD) {
        this.id = id;
        this.amountExpanse = amountExpanse;
        this.createdDate = createdDate;
        this.title = title;
//        this.budgetIdExp = budgetIdExp;
        this.category_expanse_idEXP = category_expanse_idEXP;
        this.userIdEXP = userIdEXP;
        this.idBudget = idBudget;
        this.amountBudget = amountBudget;
        this.amountRemains = amountRemains;
        this.amountSpent = amountSpent;
        this.dateDebut = dateDebut;
        this.description = description;
        this.endDate = endDate;
        this.titleBudget = titleBudget;
        this.category_expanse_idBUD = category_expanse_idBUD;
        this.userIdBUD = userIdBUD;
    }

    //private CategoryExpanse categoryExpanse;
    //private User user;

}
