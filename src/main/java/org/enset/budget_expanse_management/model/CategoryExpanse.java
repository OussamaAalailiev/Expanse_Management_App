package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.GoalCategoryType;

import javax.persistence.*;
import java.util.List;


//@Data
//@NoArgsConstructor
 //@AllArgsConstructor
@Entity
public class CategoryExpanse {

    @Id //'id' will be generated manually by an Algorithm when an category will be added:
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryExpanseType getCategoryExpanseType() {
        return categoryExpanseType;
    }

    public void setCategoryExpanseType(CategoryExpanseType categoryExpanseType) {
        this.categoryExpanseType = categoryExpanseType;
    }

    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public List<Expanse> getExpanses() {
        return expanses;
    }

    public void setExpanses(List<Expanse> expanses) {
        this.expanses = expanses;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public CategoryExpanse() {

    }

    public CategoryExpanse(Integer id, CategoryExpanseType categoryExpanseType,
                           CategoryGroup categoryGroup, List<Expanse> expanses, List<Budget> budgets) {
        this.id = id;
        this.categoryExpanseType = categoryExpanseType;
        this.categoryGroup = categoryGroup;
        this.expanses = expanses;
        this.budgets = budgets;
    }
}
