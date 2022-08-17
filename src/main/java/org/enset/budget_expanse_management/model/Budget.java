package org.enset.budget_expanse_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//@Data
@NoArgsConstructor @AllArgsConstructor
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
//    private Date endDate;
    private LocalDate endDate;
//    @Enumerated(EnumType.STRING) @Column(length = 30)
//    private CategoryExpanseType categoryExpanseType;
    private Double amount;//New field added!

    private Double amountSpent;
    private Double amountRemains; //At the beginning 'amount' is equal to 'amountRemains' (Modified at 13/08/2022):

//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private CategoryExpanse categoryExpanse;
    //Weekly & Monthly variables aren't added yet !
    @ManyToOne
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    @OneToMany(mappedBy = "budget")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Expanse> expanses;

    public Budget(Integer id, String title, String description, Date dateDebut, LocalDate endDate, Double amount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.endDate = endDate;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public Double getAmountRemains() {
        return amountRemains;
    }

    public void setAmountRemains(Double amountRemains) {
        this.amountRemains = amountRemains;
    }

    public CategoryExpanse getCategoryExpanse() {
        return categoryExpanse;
    }

    public void setCategoryExpanse(CategoryExpanse categoryExpanse) {
        this.categoryExpanse = categoryExpanse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Expanse> getExpanses() {
        return expanses;
    }

    public void setExpanses(List<Expanse> expanses) {
        this.expanses = expanses;
    }
}
