package org.enset.budget_expanse_management.mapping;

import lombok.Data;

import java.util.UUID;

@Data //@NoArgsConstructor //@AllArgsConstructor
public class TotalIncomesPerMonthDTO {
//    private Date year;
//    private Date month;
    private String year;
    private String month;
    private Double totalIncomes;
    private UUID userId;
    private String userName;
    private Double amountInterval;
    private Double percentOfAmountInterval;


    public TotalIncomesPerMonthDTO(String year, String month, Double totalIncomes, UUID userId, String userName) {
        this.year = year;
        this.month = month;
        this.totalIncomes = totalIncomes;
        this.userId = userId;
        this.userName = userName;
    }

    public TotalIncomesPerMonthDTO() {

    }

}
