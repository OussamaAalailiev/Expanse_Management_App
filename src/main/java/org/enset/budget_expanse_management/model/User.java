package org.enset.budget_expanse_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "Users")
public class User {
    //Class fields:
    @Id
    @GeneratedValue(generator = "uuid2")// String by UUID across all app portions gives us
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(length = 55, unique = true)
    private String name;
    private Double totalSold;
    private Boolean active;//To check if User's account is active or inactive:
    @Column(length = 80, unique = true)
    private String email;
//    @Column(length = 35)
//    private String password;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private UserCurrency currency;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    //Class fields Relations to Other Classes Via JPA Annotations '@...(..)':
    @OneToMany(mappedBy = "user")
    private List<Income> incomeList;
    @OneToMany(mappedBy = "user")
    private List<Expanse>  expanseList;
    @OneToMany(mappedBy = "user")
    private List<Goal> goalList;
    @OneToMany(mappedBy = "user")
    private List<Budget> budgetList;


    public User(UUID id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
