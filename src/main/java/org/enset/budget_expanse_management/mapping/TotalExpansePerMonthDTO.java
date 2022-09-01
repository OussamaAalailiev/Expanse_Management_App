package org.enset.budget_expanse_management.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data //@NoArgsConstructor //@AllArgsConstructor
public class TotalExpansePerMonthDTO {
//    private Date year;
//    private Date month;
    private short year;
    private short month;
    private Long totalExpanse;

//    public TotalExpansePerMonthDTO(Date year, Date month, Long totalExpanse) {
//        this.year = year;
//        this.month = month;
//        this.totalExpanse = totalExpanse;
//    }

    public TotalExpansePerMonthDTO(short year, byte month, Long totalExpanse) {
        this.year = year;
        this.month = month;
        this.totalExpanse = totalExpanse;
    }

    public TotalExpansePerMonthDTO() {

    }
}
