package org.enset.budget_expanse_management;

import org.enset.budget_expanse_management.dataInit.DataInitiation;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.GoalCategoryType;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.enset.budget_expanse_management.model.*;
import org.enset.budget_expanse_management.repositories.*;
import org.enset.budget_expanse_management.service.BudgetExpanseManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
                            CategoryExpanseRepository categoryExpanseRepository,
                            DataInitiation dataInitiation,
                            BudgetExpanseManagementService managementService
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



//         dataInitiation.initCategoryExpanseGroupWithCategoriesData();
//         dataInitiation.initCategoryIncomeWithCategoriesData();
//         dataInitiation.initCategoryExpanseWithCategoriesData();
//
//         dataInitiation.initUsers();
//         // dataInitiation.getUsersAfterDataInit();
//         dataInitiation.initExpanses();
//         dataInitiation.initIncomes();
//         dataInitiation.initGoals();
//         dataInitiation.initBudgets();
//
//         managementService.addExpanseToBudgetServiceInit();


         dataInitiation.getUsersAfterDataInit();
         System.out.println("******************************");
//         System.out.println("********** Getting All Expanses & Budgets that have same user & *******");
//         System.out.println("**********  & same CategoryExpanseId & during same period of time: *******");

         //managementService.getAllExpAndBudWithSameUserDateAndCatExpService();

         //To Initiate amountRemains = budget.amount on Every new Budget entered (temporarily Solution):
         //dataInitiation.updateAmountRemainsOfAllNewBudgetsInit();

         System.out.println();
         System.out.println();
         /** Check if budgets were Respected: */
         System.out.println("************************************");
         System.out.println("**************** Check if budgets were Respected: ********************");
         budgetRepository.getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp3()
                 .forEach(resultDTOExpansesBudgets -> {
                     System.out.println(resultDTOExpansesBudgets.toString());
                 });

//         User user = userRepository.findByNameContains("Oussama");
//         System.out.println("User -----------");
//         System.out.println("User Id: " + user.getId());
//         System.out.println("User Name: " + user.getName());
//         CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(20).get();
//
//         System.out.println("categoryExpanse -----------");
//         System.out.println("categoryExpanse Id: " + categoryExpanse.getId());
//         System.out.println("categoryExpanse Name: " + categoryExpanse.getCategoryExpanseType());

//
//         Budget budget1 = new Budget();
//         budget1.setAmount(1000.00);
//         budget1.setCategoryExpanse(categoryExpanse); budget1.setUser(user);
//         budget1.setDateDebut(new Date()); budget1.setEndDate(LocalDate.of(2022,11,1));
//         budgetRepository.save(budget1);
//
//         Budget budget2 = new Budget();
//         budget1.setAmount(2000.00);
//         budget1.setCategoryExpanse(categoryExpanse); budget2.setUser(user);
//         budget1.setDateDebut(new Date()); budget1.setEndDate(LocalDate.of(2022,10,1));
//         budgetRepository.save(budget2);

//         Expanse expanseAddedByUser = new Expanse();
//         expanseAddedByUser.setAmount(333.0);
//         expanseAddedByUser.setCreatedDate(new Date());
//         expanseAddedByUser.setCategoryExpanse(categoryExpanse);
//         expanseAddedByUser.setUser(user);
//         expanseRepository.save(expanseAddedByUser);
//
//         managementService.checkIfBudgetIsRespectedOnAddExpanse(expanseAddedByUser);

         //dataInitiation.testIfBudgetsWereRespectedOnAddExpanseInit();


//         System.out.println("------------- resultDTOExpansesBudgets ---------------");
//         budgetRepository.getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp2()
//                 .forEach(resultDTOExpansesBudgets -> {
//                     System.out.println(resultDTOExpansesBudgets.toString());
//         });

         System.out.println("----------------------------------------------------------------");
         System.out.println("---------------- Testing if budget is well respected & Calculated --------------------");
         //managementService.checkIfBudgetIsRespectedByCalculation();

//         Expanse expanse = new Expanse();
//         User user = userRepository
//                 .findById(UUID.fromString("dfa735ec-328b-43c3-ad70-f5dba33eb585")).get();
//         CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(33).get();
//         expanse.setAmount(99.0); expanse.setCreatedDate(new Date());
//         expanse.setCategoryExpanse(categoryExpanse); expanse.setUser(user);
//
//         expanseRepository.save(expanse);

         Expanse expanseN7 = expanseRepository.findById(7L).get();
         expanseN7.setAmount(119.0);
        // expanseRepository.save(expanseN7);

         managementService.calculateBudgetsOnAddOrUpdateExpanseService(expanseN7);

     };
    }
}
