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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class BudgetExpanseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetExpanseManagementApplication.class, args);
    }

    /**CORS Global Configuration: */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4090"));
        //corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "File-Name"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    CommandLineRunner start(UserRepository userRepository,
                            IncomeRepository incomeRepository,
                            ExpanseRepository expanseRepository,
                            GoalRepository goalRepository,
                            BudgetRepository budgetRepository,
                            CategoryExpanseRepository categoryExpanseRepository,
                            CategoryIncomeRepository categoryIncomeRepository,
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

         /*
         Expanse newExpanse = new Expanse();
         User user = userRepository
                 .findById(UUID.fromString("dfa735ec-328b-43c3-ad70-f5dba33eb585")).get();
         CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(33).get();
         //newExpanse.setId(10L);
         newExpanse.setAmount(200.90); newExpanse.setCreatedDate(new Date());
         newExpanse.setCategoryExpanse(categoryExpanse); newExpanse.setUser(user);
         managementService.calculateBudgetsOnAddExpanseService(newExpanse);
          */


         /**Testing On Add or Update an expanse, if we compute common budgets well: Not sure*/
//         Expanse expanseN7 = expanseRepository.findById(8L).get();
//         expanseN7.setAmount(10000.00);
//
//         managementService.calculateBudgetsOnAddOrUpdateExpanseService(expanseN7); //Not working anymore!

        // managementService.calculateBudgetsOnAddExpanseService(newExpanse);

         /**Testing On Add a Budget, if we compute common expanses well if they exist: */
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
         budget.setTitle("Amazon Books Subscription"); budget.setDescription("Some Budget Description ....");
         //budget.setAmountSpent(0.0);
         budget.setAmount(100.0); budget.setDateDebut(new Date());
         budget.setEndDate(LocalDate.of(2022, 9,30));
         budget.setAmountRemains(budget.getAmount());//This will always apply on add new Budget.
         CategoryExpanse categoryExpanseTransport = categoryExpanseRepository.findById(33).get();
         User userSafwane = userRepository
                 .findById(UUID.fromString("dfa735ec-328b-43c3-ad70-f5dba33eb585")).get();
         budget.setCategoryExpanse(categoryExpanseTransport);
         budget.setUser(userSafwane);

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
         // Update an expanse that correspond to common budget(s) :

         /*
         Expanse expanseToBeUpdate = expanseRepository.findById(14L).get();
         expanseToBeUpdate.setAmount(300.90);
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
          */



         /**Update an expanse that DOES correspond to common budget(s) : */

         /*
         Expanse expanseToBeUpdate = expanseRepository.findById(34L).get();
         expanseToBeUpdate.setAmount(1.0); //expanseToBeUpdate.setTitle("Hitman 2 Game");
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
          */

         /**Testing On Update a new Budget, if we compute common expanses well if they exist: */
         /*
         Budget budgetToBeUpdated = budgetRepository.findById(22).get();
         budgetToBeUpdated.setAmount(100.0);
         managementService.updateBudgetService(budgetToBeUpdated);
          */

         /**Testing On Delete an Expanse, if we compute common Budget(s) well if they exist: */
         /*
         Expanse expanseToDelete = expanseRepository.findById(16L).get();
         managementService.calculateBudgetsOnDeleteExpanseService(expanseToDelete);
         */

         /**Testing On Delete a Budget: */
         /*
         Budget budgetToDelete = budgetRepository.findById(24).get();
         managementService.deleteBudgetService(budgetToDelete);
          */

         /**Get Total Amount of Expanses per Month: */

         /*
                  expanseRepository.getTotalAmountExpansesOnEveryMonth().forEach(totalExpansePerMonthDTO -> {
             System.out.println();
             System.out.println(totalExpansePerMonthDTO.getYear());
             System.out.println(totalExpansePerMonthDTO.getMonth());
             System.out.println(totalExpansePerMonthDTO.getTotalExpanse());
             System.out.println("-----------------------------");
         });
          */
         /**Initiate AmountSpent of all old Budgets in DB to '0' if AmountSpent==null: */
//         budgetRepository.findAll().forEach(budget -> {
//             if (budget.getAmountSpent()==null){
//                 budget.setAmountSpent(0.0);
//                 budgetRepository.save(budget);
//             }
//         });
         /**Get Expanses By Page based on title of expanse + page N° + Size N° && UserID: */

         /*
         System.out.println("Expanses For Oussama");
         expanseRepository.findByTitleContainingAndUserId("",
                 UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1"), PageRequest.of(0,20))
                 .forEach(expanse -> {
                     System.out.println("************* ********** ***************");
                     System.out.println("Expanse Id: " + expanse.getId());
                     System.out.println("Expanse Title: " + expanse.getTitle());
                     System.out.println("Expanse Category: " + expanse.getCategoryExpanse().getCategoryExpanseType());
                     System.out.println("Expanse CreatedDate: " + expanse.getCreatedDate());
                     System.out.println("User Id: " + expanse.getUser().getId());
                     System.out.println("User Name: " + expanse.getUser().getName());
                 });
         System.out.println();
         System.out.println();
          */

         /**Get Budgets By Page based on title of expanse + page N° + Size N° && UserID: */

         /*
         System.out.println("Budgets By Oussama....");
         budgetRepository.findByTitleContainingAndUserId("",
                         UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1"), PageRequest.of(0,20))
                 .forEach(budget -> {
                     System.out.println("************* ********** ***************");
                     System.out.println("Budget Id: " + budget.getId());
                     System.out.println("Budget Title: " + budget.getTitle());
                     System.out.println("Budget Category: " + budget.getCategoryExpanse().getCategoryExpanseType());
                     System.out.println("Budget CreatedDate: " + budget.getAmount());
                     System.out.println("User Id: " + budget.getUser().getId());
                     System.out.println("User Name: " + budget.getUser().getName());
                 });
          */

         /**Insert Goal & Income to 'Zakaria' User: */
//         Goal goal = new Goal();
//         goal.setAmount(5000.0); goal.setTitle("Triathlon Africa Cup 2022."); goal.setDateDebut(new Date());
//         goal.setEndDate(LocalDate.of(2022, 12, 12)); goal.setUser();
//         goal.setCategoryIncome();
//         goalRepository.save()

         /**-- Query to get Total Amount of Expanses per Month By UserID: */
         System.out.println("Query to get Total Amount of Expanses per Month By UserID: ");
//         expanseRepository.getTotalAmountExpansesOnEveryMonthV2(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1"),
//                         PageRequest.of(0,3))

         /*
         expanseRepository.getTotalAmountExpansesOnEveryMonthV2(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1"))
                 .forEach(totalExpansePerMonthDTO -> {
                     System.out.println("Year "+"        "+" Month "+ "  "+ "Expanse Sum"+"   "+ "    User ID"+"   "+ "                   User Name");
                     System.out.print(totalExpansePerMonthDTO.getYear()+"            ");
                     System.out.print(totalExpansePerMonthDTO.getMonth()+"  ");
                     System.out.print(totalExpansePerMonthDTO.getTotalExpanses()+"            ");
                     System.out.print(totalExpansePerMonthDTO.getUserId()+"   ");
                     System.out.print(totalExpansePerMonthDTO.getUserName());
                     System.out.println(); System.out.println();
                 });
          */

         /**Test On Getting Common Income(s) WITH Common Goal(s):   */
        /*
         incomeRepository.onOneIncomeComputeOnCommonGoals(2L)
                 .forEach(resultDTOIncomesGoals -> {
                     System.out.println("----------- Common Income --------------");
                     System.out.println(resultDTOIncomesGoals.getIdIncome());
                     System.out.println(resultDTOIncomesGoals.getAmountIncome());
                     System.out.println(resultDTOIncomesGoals.getCreatedDate());
                     System.out.println(resultDTOIncomesGoals.getCategory_income_id_Income());
                     System.out.println(resultDTOIncomesGoals.getUserIdIncome());
                     System.out.println("----------- Common Goal --------------");
                     System.out.println(resultDTOIncomesGoals.getIdGoal());
                     System.out.println(resultDTOIncomesGoals.getAmountGoal());
                     System.out.println(resultDTOIncomesGoals.getDateDebut());
                     System.out.println(resultDTOIncomesGoals.getEndDate());
                     System.out.println(resultDTOIncomesGoals.getCategory_income_id_Goal());
                     System.out.println(resultDTOIncomesGoals.getUserIdGoal());

                 });
         */

         /**Test On Add Income Compute Common Goals: */



         Income income = new Income();
         income.setTitle("Wall Construction 300 DH"); income.setAmount(300.0); income.setCreatedDate(new Date());
         User user = userRepository.findById(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1")).get();
         income.setUser(user);
         CategoryIncome categoryIncome = categoryIncomeRepository.findById(12).get();
         income.setCategoryIncome(categoryIncome);
         managementService.calculateGoalsOnAddIncomeService(income);



         /**Test On Update Income Compute Common Goals: */
         /*
         Income incomeFromDB = incomeRepository.findById(8L).get();
         incomeFromDB.setAmount(18400.0); incomeFromDB.setTitle("Save 18999.0 Instead of 19500 DH");
         managementService.calculateGoalsOnUpdateIncomeService(incomeFromDB);
          */




     };
    }
}
