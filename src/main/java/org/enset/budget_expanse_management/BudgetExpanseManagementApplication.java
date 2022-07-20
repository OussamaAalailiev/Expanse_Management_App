package org.enset.budget_expanse_management;

import org.enset.budget_expanse_management.dataInit.DataInitiation;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.GoalCategoryType;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.enset.budget_expanse_management.model.*;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class BudgetExpanseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetExpanseManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner start(UserRepository userRepository,
                            IncomeRepository incomeRepository,
                            ExpanseRepository expanseRepository,
                            GoalRepository goalRepository,
                            BudgetRepository budgetRepository,
                            DataInitiation dataInitiation
    ){
     return args -> {


//         //Creating users:
//         User userOussama = new User(null, "Oussama", "Oussama@XYZ.com");
//         userOussama.setActive(false); userOussama.setCurrency(UserCurrency.MAD); userOussama.setDateCreation(new Date());
//         User userZakaria = new User(null, "Zakaria", "Zakaria@XYZ.com");
//         userZakaria.setActive(true); userZakaria.setCurrency(UserCurrency.USD); userZakaria.setDateCreation(new Date());
//         User userSafoane = new User(null, "Safoane", "Safoane@XYZ.com");
//         userSafoane.setActive(true); userSafoane.setCurrency(UserCurrency.EUR); userSafoane.setDateCreation(new Date());
//         //Adding users to Database:
//         userRepository.save(userOussama);
//         userRepository.save(userZakaria);
//         userRepository.save(userSafoane);
//
//         userRepository.findAll().forEach(u -> {
//             System.out.println("------------ User details ------------");
//             System.out.println("User ID: " +u.getId());
//             System.out.println("User Name: " +u.getName());
//             System.out.println("User Currency: " +u.getCurrency());
//             System.out.println();
//         });
//         //Creation of Incomes:
//         Income income = new Income(null, 9900.0,
//                 "Salary Income this Morning", new Date());
//         Income incomeSales = new Income(null, 200000.50,
//                 "Sale of 2 Cars", new Date());
//         income.setCategoryIncomeType(CategoryIncomeType.SALARY);
//         income.setUser(userZakaria);
//        // income.setCategoryIncomeType(Categ);
//         incomeSales.setCategoryIncomeType(CategoryIncomeType.TRADE_SALES);
//         incomeSales.setUser(userSafoane);
//         //Adding Incomes to DB:
//         incomeRepository.save(income); incomeRepository.save(incomeSales);
//
//         //Creation of Expanses:
//         Expanse expanse = new Expanse(null, 5500.0,
//                 "Buying Refrigerator this afternoon", new Date());
//         Expanse expanseRent = new Expanse(null, 1600.0,
//                 "Rent Pay", new Date());
//         Expanse expanseFood = new Expanse(null, 75.70,
//                 "Buying Food", new Date());
//         expanse.setCategoryExpanseType(CategoryExpanseType.ELECTRONICS);
//         expanse.setUser(userZakaria);
//         expanseRent.setCategoryExpanseType(CategoryExpanseType.HOUSING);
//         expanseRent.setUser(userSafoane);
//         expanse.setCategoryExpanseType(CategoryExpanseType.FOOD_AND_DRINKS);
//         expanseFood.setUser(userOussama);
//         //Adding Expanses to DB:
//         expanseRepository.save(expanse); expanseRepository.save(expanseRent);
//         expanseRepository.save(expanseFood);
//
//         //Creation of Goals:
//         Goal goal = new Goal(null, "Save 500 DH this week", "Save money step by step ..", new Date(),
//                 LocalDate.of(2022, 7, 23));
//         goal.setGoalCategoryType(GoalCategoryType.SAVING_AMOUNT);
//         goal.setUser(userSafoane);
//         Goal goalBuyCar = new Goal(null, "Save 1200 DH every Month", "Save 1200 DH every Month for 2 years ..", new Date(),
//                 LocalDate.of(2024, 8, 15));
//         goalBuyCar.setGoalCategoryType(GoalCategoryType.NEW_VEHICLE);
//         goalBuyCar.setUser(userZakaria);
//         //Adding Goals to DB:
//         goalRepository.save(goal);
//         goalRepository.save(goalBuyCar);
//
//         //Creation of Budgets:
//         Budget budgetFor7awli = new Budget(null, "Buy 7awli", "Buy 7awli Sardi ...", new Date(),
//                               LocalDate.of(2023, 11, 5));
//         Budget budgetForMonthlyFood = new Budget(null, "Food of a Month", "Food of a Month and ...", new Date(),
//                 LocalDate.of(2022, 8, 16));
//         Budget budgetForPC = new Budget(null, "Buy PC", "Description of pc ...", new Date(),
//                 LocalDate.of(2023, 1, 1));
//         budgetFor7awli.setCategoryExpanseType(CategoryExpanseType.FOOD_AND_DRINKS);
//         budgetFor7awli.setUser(userSafoane);
//         budgetForMonthlyFood.setCategoryExpanseType(CategoryExpanseType.FOOD_AND_DRINKS);
//         budgetForMonthlyFood.setUser(userSafoane);
//         budgetForPC.setCategoryExpanseType(CategoryExpanseType.LIFE_AND_ENTERTAINMENT);
//         budgetForPC.setUser(userZakaria);
//         //Adding Budgets to DB:
//         budgetRepository.save(budgetFor7awli); budgetRepository.save(budgetForMonthlyFood);
//         budgetRepository.save(budgetForPC);

         dataInitiation.initCategoryExpanseGroupWithCategoriesData();
         dataInitiation.initCategoryIncomeWithCategoriesData();
         dataInitiation.initCategoryExpanseWithCategoriesData();

         dataInitiation.initUsers();
         dataInitiation.getUsersAfterDataInit();
         dataInitiation.initExpanses();
         dataInitiation.initIncomes();
         dataInitiation.initGoals();
         dataInitiation.initBudgets();


     };
    }
}
