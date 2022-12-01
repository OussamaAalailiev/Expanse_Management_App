package org.enset.budget_expanse_management;

import org.enset.budget_expanse_management.dataInit.DataInitiation;
import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.GoalCategoryType;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.enset.budget_expanse_management.mapping.ExpensesByCategory;
import org.enset.budget_expanse_management.mapping.ResultDTOIncomesGoals;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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


         /**Testing On Add or Update 'Amount' expanse, if we compute common budgets well: Not sure*/
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

         /**Testing On Update 'Amount' Expanse, and if we compute common budgets well if they exist: */
         // Update an expanse that correspond to common budget(s) :

         /*
         Expanse expanseToBeUpdate = expanseRepository.findById(14L).get();
         expanseToBeUpdate.setAmount(300.90);
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
          */



         /**Update 'Amount' expanse that DOES Not correspond to common budget(s) : */

         /*
         Expanse expanseToBeUpdate = expanseRepository.findById(34L).get();
         expanseToBeUpdate.setAmount(1.0); //expanseToBeUpdate.setTitle("Hitman 2 Game");
         managementService.calculateBudgetsOnUpdateExpanseService(expanseToBeUpdate);
          */

         /**Testing On Update 'Amount' Budget, if we compute common expanses well if they exist: */
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
//         System.out.println("Query to get Total Amount of Expanses per Month By UserID: ");
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
         /*
          Income income = new Income();
         income.setTitle("Wall Construction 300 DH"); income.setAmount(300.0); income.setCreatedDate(new Date());
         User user = userRepository.findById(UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1")).get();
         income.setUser(user);
         CategoryIncome categoryIncome = categoryIncomeRepository.findById(12).get();
         income.setCategoryIncome(categoryIncome);
         managementService.calculateGoalsOnAddIncomeService(income);
          */


         /**Test On Update 'Amount' Income Compute Common Goals: */

         /*
         Income incomeFromDB = incomeRepository.findById(7L).get();
         incomeFromDB.setAmount(1000.0); incomeFromDB.setTitle("Save 1000.0 DH");
          */

        // managementService.calculateGoalsOnUpdateIncomeService(incomeFromDB);
        // managementService.calculateGoalsOnUpdateFullIncomeService(incomeFromDB);


         /**Test On Delete Income Compute Common Goals: */
        /*
         Income incomeToBeDeleted = incomeRepository.findById(14L).get();
         managementService.calculateGoalsOnDeleteIncomeService(incomeToBeDeleted);
         */

         /**Test On Full Update Of Income Compute Common Goals: */

         /*
         Income incomeFromDB = incomeRepository.findById(7L).get();
         incomeFromDB.setAmount(18400.0); incomeFromDB.setTitle("Save 18999.0 Instead of 19500 DH");
         managementService.calculateGoalsOnUpdateIncomeService(incomeFromDB);
          */

         System.out.println();
         System.out.println("Select Total Expanses By Category & UserID : ");
         System.out.println();
         /** Select Total Expanses By Category & UserID :*/

        /*
                 expanseRepository.getTotalExpensesByCategoryAndUser(
                 UUID.fromString("653eb6f2-a817-4184-af31-4cff631692f8"))
                 .forEach(expensesByCategory -> {
                     System.out.println("Id: " + expensesByCategory.getId()+"\t");
                     System.out.println("Amount: " + expensesByCategory.getAmount()+"\t");
                     System.out.println("CreatedDate: " + expensesByCategory.getCreatedDate()+"\t");
                     System.out.println("Title: " + expensesByCategory.getTitle()+"\t");
                     System.out.println("CategoryExpenseId: " + expensesByCategory.getCategory_expanse_id()+"\t");
                     System.out.println("UserID: " + expensesByCategory.getUserId()+"\t");
                     System.out.println("SumExpensesByCategory: " + expensesByCategory.getSumExpensesByCategory()+"\t");
                     //totalSumsOfExp+=expensesByCategory.getSumExpensesByCategory();
                     System.out.println("PercentOfExpensesPerMonth: " + expensesByCategory.getSumExpensesByCategory()+"\t");
                     System.out.println("---------------------------------------------");
                 });
         */

         /*
         List<ExpensesByCategory> expensesByCategoryAndUser = expanseRepository
                 .getTotalExpensesByCategoryAndUser(
                         UUID.fromString("653eb6f2-a817-4184-af31-4cff631692f8"));
         Double totalSumsOfExp = 0.0;
         for (ExpensesByCategory expensesByCategory: expensesByCategoryAndUser) {
             totalSumsOfExp+=expensesByCategory.getSumExpensesByCategory();
         }
         double percentOfExpensesPerMonth = 0.0;
         for (ExpensesByCategory expensesByCategory: expensesByCategoryAndUser) {
             System.out.println("Id: " + expensesByCategory.getId()+"\t");
             System.out.println("Amount: " + expensesByCategory.getAmount()+"\t");
             System.out.println("CreatedDate: " + expensesByCategory.getCreatedDate()+"\t");
             System.out.println("Title: " + expensesByCategory.getTitle()+"\t");
             System.out.println("CategoryExpenseId: " + expensesByCategory.getCategory_expanse_id()+"\t");
             System.out.println("UserID: " + expensesByCategory.getUserId()+"\t");
             System.out.println("SumExpensesByCategory: " + expensesByCategory.getSumExpensesByCategory()+"\t");
             percentOfExpensesPerMonth =  ((expensesByCategory.getSumExpensesByCategory() / totalSumsOfExp) * 100);
             expensesByCategory.setPercentOfExpensesPerMonth(percentOfExpensesPerMonth);
             System.out.println("PercentOfExpensesPerMonth: " + Math.round(expensesByCategory.getPercentOfExpensesPerMonth()) +"%\t");
             System.out.println("---------------------------------------------");
         }
          */

         /** Test for Inner Join Income & Goals On providing 3 Params Instead of 1 (userId): */

        /*
         System.out.println("-------------------------------------");
         System.out.println("Test for Inner Join Income & Goals On providing 3 Params Instead of 1 (userId): ");
         Income incomeUpdated = incomeRepository.findById(2L).get();
         */

         /*
          String dateFromStringFormat = "2022-12-31";
         Date dateUpdatedFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(dateFromStringFormat);
         //incomeUpdated.setCreatedDate(new Date(2022, 11, 6));
         incomeUpdated.setCreatedDate(dateUpdatedFormatted);
         CategoryIncome categoryIncome = categoryIncomeRepository.findById(6).get();
         incomeUpdated.setCategoryIncome(categoryIncome);
         //The 'oldResultDTOIncomesGoals' does not take into consideration any info updated
         // from user's incomeUpdated except (userId) while the incomeUpdated isn't saved yet into DB:
         List<ResultDTOIncomesGoals> oldResultDTOIncomesGoals =
                 incomeRepository.onOneIncomeComputeOnCommonGoals(incomeUpdated.getId());
         //The 'newResultDTOIncomesGoals' does take into consideration any info updated
         // while the user's incomeUpdated isn't saved yet into DB, because we passed 3 Params
         // (userId, categoryIncomeId and createdDate) instead of just 1 (userId):
         List<ResultDTOIncomesGoals> newResultDTOIncomesGoals
                 = incomeRepository.onOneIncomeComputeOnCommonGoalsV2(incomeUpdated.getId(),
                 incomeUpdated.getCategoryIncome().getId(),
                 incomeUpdated.getCreatedDate());
         System.out.println("newResultDTOIncomesGoals .....");
         if (newResultDTOIncomesGoals.isEmpty()){
             System.out.println("newResultDTOIncomesGoals is Empty()..... !");
         }else {
             newResultDTOIncomesGoals.forEach(resultDTOIncomesGoals -> {
                 System.out.println("Id Goal: "+resultDTOIncomesGoals.getIdGoal());
                 System.out.println("Title Goal: "+resultDTOIncomesGoals.getTitleGoal());
                 System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getDateDebut());
                 System.out.println("End Date Goal: "+resultDTOIncomesGoals.getEndDate());
                 System.out.println("Amount Goal: "+resultDTOIncomesGoals.getAmountGoal());
                 System.out.println("CategoryIncomeId Goal: "+resultDTOIncomesGoals.getCategory_income_id_Goal());
                 System.out.println("UserId of Goal: "+resultDTOIncomesGoals.getUserIdGoal());
                 System.out.println("Id Income: "+resultDTOIncomesGoals.getIdIncome());
                 System.out.println("Title Income: "+resultDTOIncomesGoals.getTitleIncome());
                 System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getCreatedDate());
                 System.out.println("End Date Goal: "+resultDTOIncomesGoals.getAmountIncome());
                 System.out.println("CategoryIncomeId Income: "+resultDTOIncomesGoals.getCategory_income_id_Income());
                 System.out.println("UserId of Income: "+resultDTOIncomesGoals.getUserIdIncome());
                 System.out.println("------------------------------------");
             });
         }

         System.out.println("*********** oldResultDTOIncomesGoals *********** ");
         oldResultDTOIncomesGoals.forEach(resultDTOIncomesGoals -> {
             System.out.println("Id Goal: "+resultDTOIncomesGoals.getIdGoal());
             System.out.println("Title Goal: "+resultDTOIncomesGoals.getTitleGoal());
             System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getDateDebut());
             System.out.println("End Date Goal: "+resultDTOIncomesGoals.getEndDate());
             System.out.println("Amount Goal: "+resultDTOIncomesGoals.getAmountGoal());
             System.out.println("CategoryIncomeId Goal: "+resultDTOIncomesGoals.getCategory_income_id_Goal());
             System.out.println("UserId of Goal: "+resultDTOIncomesGoals.getUserIdGoal());
             System.out.println("Id Income: "+resultDTOIncomesGoals.getIdIncome());
             System.out.println("Title Income: "+resultDTOIncomesGoals.getTitleIncome());
             System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getCreatedDate());
             System.out.println("End Date Goal: "+resultDTOIncomesGoals.getAmountIncome());
             System.out.println("CategoryIncomeId Income: "+resultDTOIncomesGoals.getCategory_income_id_Income());
             System.out.println("UserId of Income: "+resultDTOIncomesGoals.getUserIdIncome());
             System.out.println("------------------------------------");
         });
          */

//         List<ResultDTOIncomesGoals> oldResultDTOIncomesGoals =
//                 incomeRepository.onOneIncomeComputeOnCommonGoals(incomeUpdated.getId());
//         System.out.println("*********** oldResultDTOIncomesGoals *********** ");
//         oldResultDTOIncomesGoals.forEach(resultDTOIncomesGoals -> {
//             System.out.println("Id Goal: "+resultDTOIncomesGoals.getIdGoal());
//             System.out.println("Title Goal: "+resultDTOIncomesGoals.getTitleGoal());
//             System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getDateDebut());
//             System.out.println("End Date Goal: "+resultDTOIncomesGoals.getEndDate());
//             System.out.println("Amount Goal: "+resultDTOIncomesGoals.getAmountGoal());
//             System.out.println("CategoryIncomeId Goal: "+resultDTOIncomesGoals.getCategory_income_id_Goal());
//             System.out.println("UserId of Goal: "+resultDTOIncomesGoals.getUserIdGoal());
//             System.out.println("Id Income: "+resultDTOIncomesGoals.getIdIncome());
//             System.out.println("Title Income: "+resultDTOIncomesGoals.getTitleIncome());
//             System.out.println("Date Debut Goal: "+resultDTOIncomesGoals.getCreatedDate());
//             System.out.println("End Date Goal: "+resultDTOIncomesGoals.getAmountIncome());
//             System.out.println("CategoryIncomeId Income: "+resultDTOIncomesGoals.getCategory_income_id_Income());
//             System.out.println("UserId of Income: "+resultDTOIncomesGoals.getUserIdIncome());
//             System.out.println("------------------------------------");
//         });


         /** Select Total Incomes By Month & UserID :*/
         /*
         System.out.println();
         System.out.println("Select Total Incomes By Month & UserID : ");
         System.out.println();
          incomeRepository.getTotalAmountIncomesOnEveryMonth(
                 UUID.fromString("3a300bc8-8954-4e93-9136-2b11ad2461b1"))
                 .forEach(totalIncomesPerMonthDTO -> {
                     System.out.println("Year: " + totalIncomesPerMonthDTO.getYear());
                     System.out.println("Month: " + totalIncomesPerMonthDTO.getMonth());
                     System.out.println("Total Incomes: " + totalIncomesPerMonthDTO.getTotalIncomes());
                     System.out.println("UserName: " + totalIncomesPerMonthDTO.getUserName());
                     System.out.println("------------------------------------------");
                 });

          */

         /** Select Total Incomes By Category & UserID : */
         /*
         System.out.println("********* Select Total Incomes By Category & UserID Ordered By Amount********");
         incomeRepository.getTotalIncomesByCategoryAndUser(UUID.fromString(
                 "3a300bc8-8954-4e93-9136-2b11ad2461b1"))
                 .forEach(incomesByCategory -> {
                     System.out.println("Created Date: "+ incomesByCategory.getCreatedDate());
                     System.out.println("CategoryIncome ID: "+ incomesByCategory.getCategory_income_id());
                     System.out.println("User ID: "+ incomesByCategory.getUserId());
                     System.out.println("Total Amount by category: "+ incomesByCategory.getTotalIncomesByCategory());
                     System.out.println("------------------------------");
                 });
          */

         /** Select Total Incomes By Category & UserID : */
         /*
         System.out.println();
         System.out.println("********* Select Total Incomes By Category & UserID Ordered By Date DESC ********");
         incomeRepository.getTotalIncomesByCategoryAndUserOrderedByDate(UUID.fromString(
                 "3a300bc8-8954-4e93-9136-2b11ad2461b1"))
                 .forEach(incomesByCategory -> {
                     System.out.println("Created Date: "+ incomesByCategory.getCreatedDate());
                     System.out.println("CategoryIncome ID: "+ incomesByCategory.getCategory_income_id());
                     System.out.println("User ID: "+ incomesByCategory.getUserId());
                     System.out.println("Total Amount by category: "+ incomesByCategory.getTotalIncomesByCategory());
                     System.out.println("------------------------------");
                 });
          */

         /** On add New Goal compute common Incomes: */
         System.out.println("************** On add New Goal compute common Incomes: ******************");
         //        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
         //default time zone
         ZoneId defaultZoneId = ZoneId.systemDefault();
         Goal goal = goalRepository.findById(2).get();
         goalRepository.onAddGoalComputeOnCommonIncomes(goal.getId(), goal.getDateDebut(),
                         Date.from(goal.getEndDate().atStartOfDay(defaultZoneId).toInstant()))
                 .forEach(resultDTOGoalAndIncomes -> {
                     System.out.println();
                     System.out.println();
                     System.out.println("Goal ID: " + resultDTOGoalAndIncomes.getIdGoal());
                     System.out.println("Goal Debut Date: " + resultDTOGoalAndIncomes.getDateDebut());
                     System.out.println("Goal End Date: " + resultDTOGoalAndIncomes.getEndDate());
                     System.out.println("Goal Amount: " + resultDTOGoalAndIncomes.getAmountGoal());
                     System.out.println("Goal AmountAchieved: " + resultDTOGoalAndIncomes.getAmountAchieved());
                     System.out.println("Goal User's ID: " + resultDTOGoalAndIncomes.getUserIdGoal());
                     System.out.println("Goal CategoryIncome's ID: " + resultDTOGoalAndIncomes.getCategory_income_id_Goal());
                     System.out.println();
                     System.out.println("Income ID: " + resultDTOGoalAndIncomes.getIdIncome());
                     System.out.println("Income createdDate: " + resultDTOGoalAndIncomes.getCreatedDate());
                     System.out.println("Income Amount: " + resultDTOGoalAndIncomes.getAmountIncome());
                     System.out.println("Income User's ID: " + resultDTOGoalAndIncomes.getUserIdIncome());
                     System.out.println("Income CategoryIncome's ID: " + resultDTOGoalAndIncomes.getCategory_income_id_Income());
                     System.out.println("Income SUM: " + resultDTOGoalAndIncomes.getAmountIncomeSum());
                 });






     };
    }
}
