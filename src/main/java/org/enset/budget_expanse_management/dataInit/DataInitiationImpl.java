package org.enset.budget_expanse_management.dataInit;

import org.enset.budget_expanse_management.enums.CategoryExpanseType;
import org.enset.budget_expanse_management.enums.CategoryGroupExpanseType;
import org.enset.budget_expanse_management.enums.CategoryIncomeType;
import org.enset.budget_expanse_management.enums.UserCurrency;
import org.enset.budget_expanse_management.model.*;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.enset.budget_expanse_management.enums.CategoryExpanseType.*;
import static org.enset.budget_expanse_management.enums.CategoryGroupExpanseType.*;
import static org.enset.budget_expanse_management.enums.CategoryIncomeType.*;
import static org.enset.budget_expanse_management.enums.UserCurrency.*;

@Transactional
@Service//This class is used to just enters some example data into Database:
public class DataInitiationImpl implements DataInitiation {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpanseRepository expanseRepository;
    private final GoalRepository goalRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryIncomeRepository categoryIncomeRepository;
    private final CategoryExpanseRepository categoryExpanseRepository;
    private final CategoryGroupExpansesRepository categoryGroupExpansesRepository;

    int userCounter = 0;
    int incomeCounter = 0;
    int categoryIncomeCounter = 0;
    int expanseCounter = 0;
    int budgetCounter = 0;

    UserCurrency [] currencies = {MAD, USD, EUR, TRY, QAR, SAR, AED, BRL, DZD, GBP, CNY};
    String [] emails = {"Oussama@XYZ.com", "Zakaria@XYZ.com", "Safoane@XYZ.com"};
    CategoryExpanseType[] categoryExpanseTypes = {Internet, Phone, Cell_Phone, Apps, Games,
            Clothes_and_Shoes, Drug_store_Chemist, Electronics_Accessories, Free_time,
            Gifts, Health_and_Beauty, Home_and_Garden, Jewels_and_Accessories, Kids,
            Pets_Animals, Tools, Ancient_Money,  Cafe, Restaurant, Fast_Food, Groceries,
            Mortgage, Property_Insurance, Rent, Services, Energy_and_Utilities, Taxi,
            Public_transport, Plane_LongDistance, Business_trip, Fuel, Leasing, Parking,
            Rentals, Vehicle_insurance, Vehicle_maintenance, Sport_Fitness,
            Books_audio_subscriptions, Charity, Zakat, Culture_SportEvents, Education_development,
            HealthCare_Doctor, Hobbies, Life_events, Tv_Streaming, Cosmetics_beauty,
            Alcohol_tobacco, Lottery_gambling, Charges_Fees, Child_Support, Fines, Insurance,
            Loan, Taxes,  Collections, Financial_Investments, Self_Improvement, Savings, Vehicles_chattels
    };


    Double [] amounts = {9900.0, 200000.90};
    CategoryIncomeType [] categoryIncomeTypes = {RENTAL_INCOME, GIFTS, DIVIDENDS, SALARY, TAX_REFUND
            , TRADE_SALES, CHILD_SUPPORT};
    Double [] amountsExpanses = {5500.0, 1600.0, 75.70};
//    GoalCategoryType [] goalCategoryTypes = {SAVING_AMOUNT,
//            NEW_VEHICLE, NEW_HOME, HOLIDAY_TRIP, EDUCATION,
//            HEALTH_CARE, PARTY, CHARITY, ZAKAT};

    public DataInitiationImpl(UserRepository userRepository, IncomeRepository incomeRepository,
                              ExpanseRepository expanseRepository, GoalRepository goalRepository,
                              BudgetRepository budgetRepository, CategoryIncomeRepository categoryIncomeRepository,
                              CategoryExpanseRepository categoryExpanseRepository,
                              CategoryGroupExpansesRepository categoryGroupExpansesRepository) {
        this.userRepository = userRepository;
        this.incomeRepository = incomeRepository;
        this.expanseRepository = expanseRepository;
        this.goalRepository = goalRepository;
        this.budgetRepository = budgetRepository;
        this.categoryIncomeRepository = categoryIncomeRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
        this.categoryGroupExpansesRepository = categoryGroupExpansesRepository;
    }

    public void initCategoryExpanseGroupWithCategoriesData(){
        for (int k = 0; k < CategoryGroupExpanseType.values().length; k++) {
            CategoryGroupExpanseType[] categoryGroupExpanseTypes = CategoryGroupExpanseType.values();
            CategoryGroup categoryGroup = new CategoryGroup();
            categoryGroup.setId(k+1);
            categoryGroup.setCategoryGroupExpanseType(categoryGroupExpanseTypes[k]);

            categoryGroupExpansesRepository.save(categoryGroup);
        }
    }

    public void initCategoryIncomeWithCategoriesData(){
        for (int i = 0; i < CategoryIncomeType.values().length; i++) {
            CategoryIncomeType[] categoryIncomeTypes = CategoryIncomeType.values();
            CategoryIncome categoryIncome = new CategoryIncome();
            categoryIncome.setId(i+1);
            categoryIncome.setCategoryIncomeType(categoryIncomeTypes[i]);

            categoryIncomeRepository.save(categoryIncome);
        }
    }

    public void initCategoryExpanseWithCategoriesData(){
        for (int j = 0; j < CategoryExpanseType.values().length; j++) {
            CategoryExpanseType[] categoryExpanseTypes = CategoryExpanseType.values();
            CategoryGroupExpanseType[] categoryGroupExpanseTypes = CategoryGroupExpanseType.values();
            CategoryExpanse categoryExpanse = new CategoryExpanse();
            categoryExpanse.setId(j+1);
            categoryExpanse.setCategoryExpanseType(categoryExpanseTypes[j]);

            int i=0;
            if (j <= 3 && categoryGroupExpanseTypes[i]==TRANSPORT) {
                CategoryGroup groupExpansesRepositoryById = categoryGroupExpansesRepository.findById(i + 1).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById);
            }else if (j > 3 && j<=15 && categoryGroupExpanseTypes[i+1]==SHOPPING){
                CategoryGroup groupExpansesRepositoryById_2 = categoryGroupExpansesRepository.findById(i + 2).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_2);
            }else if (j > 15 && j<=19 && categoryGroupExpanseTypes[i+2]==FOOD_AND_DRINKS){
                CategoryGroup groupExpansesRepositoryById_3 = categoryGroupExpansesRepository.findById(i + 3).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_3);
            }else if (j > 19 && j<=24 && categoryGroupExpanseTypes[i+3]==HOUSING){
                CategoryGroup groupExpansesRepositoryById_4 = categoryGroupExpansesRepository.findById(i + 4).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_4);
            }else if (j > 24 && j<=30 && categoryGroupExpanseTypes[i+4]==VEHICLE){
                CategoryGroup groupExpansesRepositoryById_5 = categoryGroupExpansesRepository.findById(i + 5).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_5);
            }else if (j > 30 && j<=43 && categoryGroupExpanseTypes[i+5]==LIFE_AND_ENTERTAINMENT){
                CategoryGroup groupExpansesRepositoryById_6 = categoryGroupExpansesRepository.findById(i + 6).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_6);
            }else if (j > 43 && j<=49 && categoryGroupExpanseTypes[i+6]==Financial_expanses){
                CategoryGroup groupExpansesRepositoryById_7 = categoryGroupExpansesRepository.findById(i + 7).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_7);
            }else if (j > 49 && j<=54 && categoryGroupExpanseTypes[i+7]==INVESTMENT){
                CategoryGroup groupExpansesRepositoryById_8 = categoryGroupExpansesRepository.findById(i + 8).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_8);
            }else if (j > 54 && j<= 59 && categoryGroupExpanseTypes[i+9]==COMMUNICATION_PC){
                CategoryGroup groupExpansesRepositoryById_9 = categoryGroupExpansesRepository.findById(i + 10).get();
                categoryExpanse.setCategoryGroup(groupExpansesRepositoryById_9);
            }

            categoryExpanseRepository.save(categoryExpanse);
        }
    }

    @Override
    public void initUsers() {
        Stream.of("Oussama", "Zakaria", "Safwane")
                .forEach(userName ->{
                    User user = new User();
                    user.setName(userName);
                    user.setDateCreation(new Date());
                    user.setCurrency(currencies[new Random().nextInt(currencies.length)]);
                    user.setActive(new Random().nextBoolean());
                    user.setEmail(emails[userCounter]);
                    userRepository.save(user);
                    userCounter++;
                });
    }

    @Override
    public void getUsersAfterDataInit() {
        userRepository.findAll().forEach(user -> {
            System.out.println("------------ User details ------------");
            System.out.println("User ID: " +user.getId());
            System.out.println("User Name: " +user.getName());
            System.out.println("User Currency: " +user.getCurrency());
            System.out.println();
        });
    }

    @Override
    public void initIncomes() {
        List<User> userList = userRepository.findAll();

        Stream.of("Salary Income this Morning", "Sale of 2 Cars")
                .forEach(incomeTitle->{
                    Income income = new Income();
//                    CategoryIncome categoryIncome = new CategoryIncome(incomeCounter,
//                            categoryIncomeTypes[new Random().nextInt(categoryIncomeTypes.length)]);
                    CategoryIncome categoryIncome = categoryIncomeRepository.findById(
                            new Random().nextInt(categoryIncomeTypes.length)).get();
                    income.setCategoryIncome(categoryIncome);
                    income.setTitle(incomeTitle);
                    income.setAmount(amounts[incomeCounter]);
                    income.setCreatedDate(new Date());
                    income.setUser(userList.get(new Random().nextInt(userList.size())));
//                    income.setCategoryIncomeType(
//                            categoryIncomeTypes[new Random().nextInt(categoryIncomeTypes.length)]);

                    income.setCategoryIncome(categoryIncome);
                    incomeCounter++;
                    incomeRepository.save(income);

                });
    }

    @Override
    public void initExpanses() {
        List<User> userList = userRepository.findAll();

        Stream.of("Buying Refrigerator this afternoon" , "Rent Pay", "Buying Food")
                .forEach(expanseTitle->{
                    Expanse expanse = new Expanse();
//                    CategoryExpanse categoryExpanse = new CategoryExpanse(expanseCounter,
//                            categoryExpanseTypes[new Random().nextInt(categoryExpanseTypes.length)]);
                    CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(
                            new Random().nextInt(categoryExpanseTypes.length)).get();

                    expanse.setCategoryExpanse(categoryExpanse);

                    expanse.setTitle(expanseTitle);
                    expanse.setAmount(amountsExpanses[expanseCounter]);
                    expanse.setCreatedDate(new Date());
                    expanse.setUser(userList.get(new Random().nextInt(userList.size())));

//                    expanse.setCategoryExpanseType(
//                            categoryExpanseTypes[new Random().nextInt(categoryExpanseTypes.length)]);

//                    categoryExpanse.setCategoryExpanseType(
//                            categoryExpanseTypes[new Random().nextInt(categoryExpanseTypes.length)]
//                    );
                    expanseCounter++;

                    expanseRepository.save(expanse);

                });
    }

    @Override
    public void initGoals() {
        List<User> userList = userRepository.findAll();
        Stream.of("Save 1200 DH every Month", "Saving money for a Trip")
                .forEach(goalTitle->{
                    Goal goal = new Goal();
                    //Integer id = new Random().nextInt(categoryIncomeTypes.length);
                    CategoryIncome categoryIncome = categoryIncomeRepository.findById(
                            new Random().nextInt(7)).get();
                    goal.setCategoryIncome(categoryIncome);
                    goal.setTitle(goalTitle);
                    goal.setDateDebut(new Date());
                    goal.setDescription("Some Goal Description ...");
                    goal.setEndDate(LocalDate.of(new Random().nextInt(6)+2022,
                            new Random().nextInt(11)+1,
                            new Random().nextInt(30)+1));
//                    goal.setGoalCategoryType(
//                            goalCategoryTypes[new Random().nextInt(goalCategoryTypes.length)]);
                    goal.setUser(userList.get(new Random().nextInt(userList.size())));
                    goal.setAmount(new Random().nextInt(150000)+0.50);

                    goalRepository.save(goal);
                    categoryIncomeCounter++;
                    incomeCounter++;
                });
    }

    @Override
    public void initBudgets() {
        List<User> userList = userRepository.findAll();
        Stream.of("Buy 7awli", "Food of a Month", "Buy PC")
                .forEach(budgetTitle->{
                    Budget budget = new Budget();
//                    CategoryExpanse categoryExpanse = new CategoryExpanse(budgetCounter+10,
//                            categoryExpanseTypes[new Random().nextInt(categoryExpanseTypes.length)]);
                    CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(
                            new Random().nextInt(categoryExpanseTypes.length)).get();

                    budget.setCategoryExpanse(categoryExpanse);
                    budget.setTitle(budgetTitle);
                    budget.setDescription("Some Budget Description ...");
                    budget.setDateDebut(new Date());
//                    budget.setEndDate(LocalDate.of(new Random().nextInt(4)+2022,
//                            new Random().nextInt(11)+1,
//                            new Random().nextInt(30)+1));
                    budget.setEndDate(LocalDate.of(new Random().nextInt(4)+2022,
                            new Random().nextInt(11)+1,
                            new Random().nextInt(30)+1));

//                    budget.setCategoryExpanseType(
//                            categoryExpanseTypes[new Random().nextInt(categoryExpanseTypes.length)]
//                    );
                    budget.setUser(userList.get(new Random().nextInt(userList.size())));
                    budget.setAmount(new Random().nextInt(20000)+0.7);

                    budgetCounter++;
                    budgetRepository.save(budget);
                });
    }


}
