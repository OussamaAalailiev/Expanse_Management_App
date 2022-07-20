package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.GoalCategoryType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 55)
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateDebut;//This 'dateDebut' refer to the current date.
    private LocalDate endDate;
//    @Column(length = 30)
//    @Enumerated(EnumType.STRING)//This annotation will let the DataBase to save the enum value in 'goalCategory' TEXT:
//    private GoalCategoryType goalCategoryType;
    private Double amount;//This field was just added, Not tested yet! it is bound to User's positive sold:

//    @ManyToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private CategoryIncome categoryIncome;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "goal")
    private List<Income> incomes;

    public Goal(Integer id, String title, String description, Date dateDebut, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.endDate = endDate;
    }
}
