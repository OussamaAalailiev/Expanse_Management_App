package org.enset.budget_expanse_management.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data //@NoArgsConstructor //@AllArgsConstructor
public class TotalExpansePerMonthDTO {
//    private Date year;
//    private Date month;
    private String year;
    private String month;
    private Double totalExpanses;
    private UUID userId;
    private String userName;
    private Double amountInterval;
    private Double percentOfAmountInterval;


    public TotalExpansePerMonthDTO(String year, String month, Double totalExpanses, UUID userId, String userName) {
        this.year = year;
        this.month = month;
        this.totalExpanses = totalExpanses;
        this.userId = userId;
        this.userName = userName;
    }

    public TotalExpansePerMonthDTO() {

    }
}
