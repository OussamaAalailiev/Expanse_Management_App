package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 55)
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateDebut;//This 'dateDebut' or System Date refer to the current date.
    private LocalDate endDate;
//    @Enumerated(EnumType.STRING) @Column(length = 30)
//    private CategoryExpanseType categoryExpanseType;
    private Double amount;//New field added!

//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private CategoryExpanse categoryExpanse;
    //Weekly & Monthly variables aren't added yet !
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    @OneToMany(mappedBy = "budget")
    private List<Expanse> expanses;

    public Budget(Integer id, String title, String description, Date dateDebut, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.endDate = endDate;
    }
}
