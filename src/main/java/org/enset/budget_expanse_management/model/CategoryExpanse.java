package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.GoalCategoryType;

import javax.persistence.*;
import java.util.List;

/**This class is replaced by enum field in each other class (ex: Income, etc ..)
 *   because the user cannot choose a Category by name (just by id which is not convenient): */



@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class CategoryExpanse {

    @Id //Id will be generated manually by an Algorithm when an category will be added:
    private Integer id;
//    @Column(length = 30)
//    @Enumerated(EnumType.STRING)//This annotation will let the DataBase to save the enum value in 'goalCategory' TEXT:
//    private GoalCategoryType goalCategoryType;
//    @Enumerated(EnumType.STRING) @Column(length = 30)
//    private CategoryIncomeType categoryIncomeType;
    @Enumerated(EnumType.STRING) @Column(length = 30)
    private CategoryExpanseType categoryExpanseType;//It can be used also in category inside 'Budget' class.

    @ManyToOne
    private CategoryGroup categoryGroup;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "categoryExpanse")
    private List<Expanse> expanses;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "categoryExpanse")
    private List<Budget> budgets;

    public CategoryExpanse(Integer id, CategoryExpanseType categoryExpanseType) {
        this.id = id;
        this.categoryExpanseType = categoryExpanseType;
    }

    public CategoryExpanse(Integer id, CategoryExpanseType categoryExpanseType, CategoryGroup categoryGroup) {
        this.id = id;
        this.categoryExpanseType = categoryExpanseType;
        this.categoryGroup = categoryGroup;
    }
}
