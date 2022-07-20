package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;

import javax.persistence.*;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryIncome {
    @Id
    private Integer id;
    @Column(length = 40)
    @Enumerated(EnumType.STRING)
    private CategoryIncomeType categoryIncomeType;

    public CategoryIncome(Integer id, CategoryIncomeType categoryIncomeType) {
        this.id = id;
        this.categoryIncomeType = categoryIncomeType;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "categoryIncome")
    private Income income;
    @OneToMany(mappedBy = "categoryIncome")
    private List<Goal> goalList;

//    public static void initCategoryIncomeWithCategoriesData(){
//        for (int i = 0; i < CategoryIncomeType.values().length; i++) {
//            CategoryIncomeType[] categoryIncomeTypes = CategoryIncomeType.values();
//            CategoryIncome categoryIncome = new CategoryIncome();
//            categoryIncome.setId(Integer.MAX_VALUE-i);
//            categoryIncome.setCategoryIncomeType(categoryIncomeTypes[i]);
//        }
//    }

}
