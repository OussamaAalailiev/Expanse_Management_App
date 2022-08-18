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
import java.util.Calendar;
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
         /**Testing On Add a new expanse, if we compute common budgets well: Working for now! */

//         Expanse newExpanse = new Expanse();
//         User user = userRepository
//                 .findById(UUID.fromString("dfa735ec-328b-43c3-ad70-f5dba33eb585")).get();
//         CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(60).get();
//         //newExpanse.setId(10L);
//         newExpanse.setAmount(600.90); newExpanse.setCreatedDate(new Date());
//         newExpanse.setCategoryExpanse(categoryExpanse); newExpanse.setUser(user);
//         managementService.calculateBudgetsOnAddExpanseService(newExpanse);


         /**Testing On Add or Update an expanse, if we compute common budgets well: Not sure*/
//         Expanse expanseN7 = expanseRepository.findById(8L).get();
//         expanseN7.setAmount(10000.00);
//
//         managementService.calculateBudgetsOnAddOrUpdateExpanseService(expanseN7); //Not working anymore!

        // managementService.calculateBudgetsOnAddExpanseService(newExpanse);

         /**Testing On Add or Update a Budget, if we compute common expanses well if they exist: */
       /*
        Budget budget= new Budget();
        budget.setTitle("Achat de 2 Chat"); budget.setDescription("Some Budget Description ....");
        budget.setAmountSpent(0.0);
        budget.setAmount(10000.00); budget.setDateDebut(new Date());
        budget.setEndDate(LocalDate.of(2022, 8,31));
        budget.setAmountRemains(budget.getAmount());//This will always apply on add new Budget.
        CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(14).get();
        User user = userRepository
                .findById(UUID.fromString("653eb6f2-a817-4184-af31-4cff631692f8")).get();
        budget.setCategoryExpanse(categoryExpanse);
        budget.setUser(user);

        managementService.calculateExpansesOnAddBudgetService(budget);

        */

         /**Testing On Add a new Budget, if we compute common expanses well if they exist: Working for now!*/
         /*
         Budget budget= new Budget();
         budget.setTitle("Subscription of Monthly Tramway"); budget.setDescription("Some Budget Description ....");
         budget.setAmountSpent(0.0);
         budget.setAmount(230.0); budget.setDateDebut(new Date());
         budget.setEndDate(LocalDate.of(2022, 8,18));
         budget.setAmountRemains(budget.getAmount());//This will always apply on add new Budget.
         CategoryExpanse categoryExpanseTransport = categoryExpanseRepository.findById(2).get();
         User userOussama = userRepository
                 .findById(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1")).get();
         budget.setCategoryExpanse(categoryExpanseTransport);
         budget.setUser(userOussama);

         managementService.calculateExpansesOnAddBudgetService(budget);
          */

         /*
         Expanse newExpanse = new Expanse();
         User user = userRepository
                 .findById(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1")).get();
         CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(2).get();
         //newExpanse.setId(10L);
         newExpanse.setTitle("Tramway 2 tickets");
         newExpanse.setAmount(14.0); newExpanse.setCreatedDate(new Date());
         newExpanse.setCategoryExpanse(categoryExpanse); newExpanse.setUser(user);
         managementService.calculateBudgetsOnAddExpanseService(newExpanse);
          */

         /**Testing On Update an Expanse, and if we compute common budgets well if they exist: */
         /* Update an expanse that correspond to common budget(s) :
         Expanse expanseToBeUpdate = expanseRepository.findById(13L).get();
         expanseToBeUpdate.setAmount(22.0);
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
          */

         /* Update an expanse that DOES NOT correspond to common budget(s) : */
         Expanse expanseToBeUpdate = expanseRepository.findById(11L).get();
         expanseToBeUpdate.setAmount(599.90); expanseToBeUpdate.setTitle("Hitman 2 Game");
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
     };
    }
}
