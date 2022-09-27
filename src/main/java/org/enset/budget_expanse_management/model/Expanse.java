package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;

import javax.persistence.*;
import java.util.Date;
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Expanse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @Column(length = 55)
    private String title;
    @Temporal(TemporalType.DATE)//To format the date properly in Date format before being saved into DB:
    private Date createdDate;
//    @Enumerated(EnumType.STRING) @Column(length = 30)
//    private CategoryExpanseType categoryExpanseType;

//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private CategoryExpanse categoryExpanse;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private User user;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @ManyToOne
//    private Budget budget;

    public Expanse(Long id, Double amount, String title, Date createdDate){
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.createdDate = createdDate;
    }
}
