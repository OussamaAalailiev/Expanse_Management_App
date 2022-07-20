package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryGroupExpanseType;

import javax.persistence.*;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryGroup {
    @Id
    private Integer id;
    @Column(length = 35)
    @Enumerated(EnumType.STRING)
    private CategoryGroupExpanseType categoryGroupExpanseType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "categoryGroup")
    private List<CategoryExpanse> categoryExpanse;
}
