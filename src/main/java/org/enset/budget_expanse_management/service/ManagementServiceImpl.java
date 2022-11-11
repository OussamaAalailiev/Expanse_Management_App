package org.enset.budget_expanse_management.service;

import jdk.jfr.RecordingState;
import org.enset.budget_expanse_management.mapping.*;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.Goal;
import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;

@Transactional
@Service
public class ManagementServiceImpl implements BudgetExpanseManagementService {

    private final ExpanseRepository expanseRepository;
    private final BudgetRepository budgetRepository;
//    private final CategoryExpanseRepository categoryExpanseRepository;
    private final IncomeRepository incomeRepository;
    private final GoalRepository goalRepository;
//    private final CategoryIncomeRepository categoryIncomeRepository;
//
//    private final UserRepository userRepository;

    public ManagementServiceImpl(ExpanseRepository expanseRepository,
                                 BudgetRepository budgetRepository,
//                                 ,CategoryExpanseRepository categoryExpanseRepository,
                                 IncomeRepository incomeRepository,
                                 GoalRepository goalRepository
//                                 CategoryIncomeRepository categoryIncomeRepository,
//                                 UserRepository userRepository
    ) {
        this.expanseRepository = expanseRepository;
        this.budgetRepository = budgetRepository;
//        this.categoryExpanseRepository = categoryExpanseRepository;
        this.incomeRepository = incomeRepository;
        this.goalRepository = goalRepository;
//        this.categoryIncomeRepository = categoryIncomeRepository;
//        this.userRepository = userRepository;
    }

    //User can update Only the 'amount' of Expanse, but if user want to update
    // other fields he/she better delete the expanse and create a new one expanse:
    /** Function is completed! */
    @Override
    public void calculateBudgetsOnUpdateExpanseService(Expanse expanse) {
        try {
            List<Budget> ListBudgetsToUpdate=new ArrayList<>();
            List<ResultDTOExpansesBudgets> expanseBudgetsDTO = expanseRepository
                    .onOneExpanseComputeOnCommonBudgets(expanse.getCategoryExpanse().getId(),
                            expanse.getId());
            //In case for Expanse Update, before update, I'll get the Old amountExp from DB
            // THEN I'll do a comparison between the old value and new value entered to be updated
            // to calculate commonBudgets Then I'll UPDATE the Expanse & (common Budgets if exists) by re-save it or them:
            if (!expanseBudgetsDTO.isEmpty()){
                if (expanseRepository.findById(expanse.getId()).isPresent()){
//                //Before Expanse Update Algorithm Calculation:
                    for (int i = 0; i < expanseBudgetsDTO.size(); i++) {
                        System.out.println("Inside Loop ....");
                        Integer budgetId= expanseBudgetsDTO.get(i).getIdBudget();
                        Double amountSpent = expanseBudgetsDTO.get(i).getAmountSpent();
                        //Double amountRemains = expanseBudgetsDTO.get(i).getAmountRemains();
                        Double oldAmountExpanseFromDB = expanseBudgetsDTO.get(i).getAmountExpanse();
                        Double amountExpanseInterval = 0.0;
                        System.out.println("expanse.getAmount(): "+expanse.getAmount());
                        System.out.println("oldAmountExpanseFromDB: "+oldAmountExpanseFromDB);
                        System.out.println("amountSpent: "+amountSpent);
                        if (expanse.getAmount() < oldAmountExpanseFromDB && amountSpent !=null){//In case Of update with Less new amountExp than the Old one:
                            Budget budget = budgetRepository.findById(budgetId).get();
                            amountExpanseInterval = expanse.getAmount() - oldAmountExpanseFromDB;// -(someNumber) -> means less amountExpanse
                            budget.setAmountSpent(budget.getAmountSpent() + amountExpanseInterval);
                            budget.setAmountRemains(budget.getAmountRemains() - amountExpanseInterval);//Always add amountExpanseInterval
                            // (amRemains-(-amExp)) => (amRemains + amExp))
                            ListBudgetsToUpdate.add(budget);
                            //budgetRepository.save(budget);
                            //expanseRepository.save(expanse);
                        } else if (expanse.getAmount() > oldAmountExpanseFromDB && amountSpent !=null) {//In case Of update with new amountExp Greater than the Old one:
                            Budget budget = budgetRepository.findById(budgetId).get();
                            amountExpanseInterval = expanse.getAmount() - oldAmountExpanseFromDB;// Inverse Calc In comparison of the above interval:
                            budget.setAmountSpent(budget.getAmountSpent() + amountExpanseInterval);//(-(-)) => (+())
                            budget.setAmountRemains(budget.getAmountRemains() - amountExpanseInterval);
                            ListBudgetsToUpdate.add(budget);
                            //budgetRepository.save(budget);
                            //expanseRepository.save(expanse);
                        } else if (amountSpent == null || amountSpent==0.0) {
                            Budget budget = budgetRepository.findById(budgetId).get();
                            budget.setAmountSpent(expanse.getAmount());
                            ListBudgetsToUpdate.add(budget);
                            //budgetRepository.save(budget);
//                    expanseRepository.save(expanse);
                        }

                    }
                    System.out.println("Expanse saved with Common Budget(s)...");
                    expanseRepository.save(expanse);
                    System.out.println("List of Budget(s)...: " + ListBudgetsToUpdate.size());
                    budgetRepository.saveAll(ListBudgetsToUpdate);
                }

            }else {//In case there is NO common Budgets, we only save the expanse:
                System.out.println("Expanse saved with NO Common Budget(s)...");
                expanseRepository.save(expanse);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**The new function that computes the budgets already existed
     *  while a user add or update an expanse: */
    /** Function is completed! */
    @Override
    public void calculateBudgetsOnAddExpanseService(Expanse expanse) {
        List<Budget> listBudgetsToUpdateOnAddExp=new ArrayList<>();
        //Expanse Object is not used yet:
        expanseRepository.save(expanse);
        List<ResultDTOExpansesBudgets> expanseBudgetsDTO = expanseRepository
                .onOneExpanseComputeOnCommonBudgets(expanse.getCategoryExpanse().getId(),
                        expanse.getId());
        if (!expanseBudgetsDTO.isEmpty() ){
            if (expanseRepository.findById(expanse.getId()).isPresent()){
                for (int i = 0; i < expanseBudgetsDTO.size()
                    //  && expanseRepository.findById(expanse.getId()).isEmpty()
                        ; i++) {
                    Integer budgetId= expanseBudgetsDTO.get(i).getIdBudget();
                    Double amountSpent = expanseBudgetsDTO.get(i).getAmountSpent();
                    Double amountRemains = expanseBudgetsDTO.get(i).getAmountRemains();
                    Double amountExpanse = expanseBudgetsDTO.get(i).getAmountExpanse();
                    if (amountSpent==null || amountSpent==0){
                        Budget budget = budgetRepository.findById(budgetId).get();
                        budget.setAmountSpent(amountExpanse);
                        budget.setAmountRemains(amountRemains - amountExpanse);
                       // budgetRepository.save(budget);
                        listBudgetsToUpdateOnAddExp.add(budget);
                    } else {
                        Budget budget = budgetRepository.findById(budgetId).get();
                        budget.setAmountSpent(amountSpent + amountExpanse);
                        budget.setAmountRemains(amountRemains - amountExpanse);
                        //budgetRepository.save(budget);
                        listBudgetsToUpdateOnAddExp.add(budget);
                    }
                }
                budgetRepository.saveAll(listBudgetsToUpdateOnAddExp);
            }
        }else {//In case there is NO common Budgets, we only save the expanse:
          //  expanseRepository.save(expanse);
            System.out.println("No Budget(s) related to this expanse! ");
        }
    }

    @Override
    public void calculateBudgetsOnDeleteExpanseService(Expanse expanse) {
        try {
            List<Budget> listBudgetsToUpdateOnDeleteExp=new ArrayList<>();
            List<ResultDTOExpansesBudgets> dtoExpansesBudgets = expanseRepository
                    .onOneExpanseComputeOnCommonBudgets
                            (expanse.getCategoryExpanse().getId(), expanse.getId());

            if (!dtoExpansesBudgets.isEmpty()){
                for (int i = 0; i < dtoExpansesBudgets.size(); i++) {
                    System.out.println("Delete Expanse + Update Common Budget(s)");
                    Double amountExpToBeDeleted = dtoExpansesBudgets.get(0).getAmountExpanse();
                    Double amountRemains = dtoExpansesBudgets.get(i).getAmountRemains();
                    Double amountSpent = dtoExpansesBudgets.get(i).getAmountSpent();
                    Integer budgetId = dtoExpansesBudgets.get(i).getIdBudget();
                    if (amountSpent==null || amountSpent==0.0){
                        Budget budget = budgetRepository.findById(budgetId).get();
                        listBudgetsToUpdateOnDeleteExp.add(budget);
                    } else { //Meaning: else if (amountSpent != null)
                        Budget budget = budgetRepository.findById(budgetId).get();
                        budget.setAmountSpent(amountSpent - amountExpToBeDeleted);
                        budget.setAmountRemains(amountRemains + amountExpToBeDeleted);
                        listBudgetsToUpdateOnDeleteExp.add(budget);
                    }
                }
                budgetRepository.saveAll(listBudgetsToUpdateOnDeleteExp);
                expanseRepository.delete(expanse);
            }else {// If There is No common Budget(s) on the Expanse:
                expanseRepository.delete(expanse);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public Page<Expanse> getExpansesByPageAndSizeAndTitleService(String title, int page, int size) {
        return expanseRepository.findByTitleContaining(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Expanse> getExpansesByPageAndSizeAndTitleAndUserIdService(String title, String userId,
                                                                          int page, int size) {
        try {
            return expanseRepository
                    .findByTitleContainingAndUserId(title, UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Expanse(s) were Not Found!");
        }
    }

    @Override
    public List<ExpensesByCategory> getExpensesSumByCategoryAndUserIdService(String userId) {
        try {
            List<ExpensesByCategory> expensesByCategoryAndUser = expanseRepository
                    .getTotalExpensesByCategoryAndUser(
                            UUID.fromString(userId));
            Double totalSumsOfExp = 0.0;
            for (ExpensesByCategory expensesByCategory: expensesByCategoryAndUser) {
                totalSumsOfExp+=expensesByCategory.getSumExpensesByCategory();
            }
            double percentOfExpensesPerMonth;
            for (ExpensesByCategory expensesByCategory: expensesByCategoryAndUser) {
                percentOfExpensesPerMonth =  ((expensesByCategory.getSumExpensesByCategory() / totalSumsOfExp) * 100);
                expensesByCategory.setPercentOfExpensesPerMonth(percentOfExpensesPerMonth);
            }
            return expensesByCategoryAndUser;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Expenses By Category & userId!...");
        }
       // return null;
    }


    /** Function 'updateBudgetService(..)' is completed! */
    @Override//In case a user adds a new Budget:
    public void calculateExpansesOnAddBudgetService(Budget budget) {
        /**BEFORE Saving a new Budget: */
        budget.setAmountRemains(budget.getAmount());
        budget.setAmountSpent(0.0);
        Budget newSavedBudgetToDB = budgetRepository.save(budget);
        /**AFTER Saving a new Budget to DB: */
        List<ResultDTOExpansesBudgets> expansesBudgets = budgetRepository.onAddBudgetComputeOnCommonExpanses
                (newSavedBudgetToDB.getId(), newSavedBudgetToDB.getCategoryExpanse().getId());
        if (expansesBudgets.isEmpty()){//If the budget have no common expanses, means the List above will be empty then we save the budget without calculation:
            //budgetRepository.save(budget);
            System.out.println("No Common Expanses with the recently saved Budget...");
        } else if (!expansesBudgets.isEmpty() && expansesBudgets.get(0).getAmountExpanseSum()!=null){
            //Do some Calculation...
            System.out.println("amountRemains: " + newSavedBudgetToDB.getAmountRemains());
            System.out.println("getAmountExpanseSum: " + expansesBudgets.get(0).getAmountExpanseSum());
            Double amountRemainsCalculated = newSavedBudgetToDB.getAmountRemains()
                    - expansesBudgets.get(0).getAmountExpanseSum();
            newSavedBudgetToDB.setAmountSpent(expansesBudgets.get(0).getAmountExpanseSum());
            newSavedBudgetToDB.setAmountRemains(amountRemainsCalculated);
            budgetRepository.save(newSavedBudgetToDB);
        }

    }

    @Override
    public List<TotalExpansePerMonthDTO> getTotalExpansesPerYearMonthAndUserService(String userId) {
        try {
            List<TotalExpansePerMonthDTO> totalExpansePerMonthDTOS
                    = expanseRepository.getTotalAmountExpansesOnEveryMonthV2(UUID.fromString(userId));
            for (int i = 0; i < totalExpansePerMonthDTOS.size(); i++) {

                double amountInterval;
                double percentOfAmountInterval;
               if (i < totalExpansePerMonthDTOS.size()-1){
                   amountInterval = ( totalExpansePerMonthDTOS.get(i).getTotalExpanses()
                           - totalExpansePerMonthDTOS.get(i+1).getTotalExpanses() );
                   percentOfAmountInterval = ( amountInterval /
                           totalExpansePerMonthDTOS.get(i).getTotalExpanses() ) * 100;

                   totalExpansePerMonthDTOS.get(i).setAmountInterval(amountInterval);
                   totalExpansePerMonthDTOS.get(i).setPercentOfAmountInterval(percentOfAmountInterval);
               } else if (i== totalExpansePerMonthDTOS.size()-1) {
                    if (totalExpansePerMonthDTOS.get(i).getAmountInterval()==null){
                        totalExpansePerMonthDTOS.get(i).setAmountInterval(0.0);
                    }
                    if (totalExpansePerMonthDTOS.get(i).getPercentOfAmountInterval()==null){
                        totalExpansePerMonthDTOS.get(i).setPercentOfAmountInterval(0.0);
                    }
               }

            }
            return totalExpansePerMonthDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while getting Expanse Sum by user!");
        }
    }

    /**-- Query to get Total Amount of Expanses per Month By UserID: */
   /*
    @Override
    public Page<TotalExpansePerMonthDTO> getTotalExpansesPerYearMonthAndUserService(String userId,
                                                                                    int page,
                                                                                    int size) {
        try {
            return expanseRepository.getTotalAmountExpansesOnEveryMonthV2(UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while getting Expanse Sum by user!");
        }
    }
    */

    /** Function 'updateBudgetService(..)' is not yet completed! */
    /**The user cannot update 'amountSpent' & 'amountRemains' & also Probably 'CategoryExId' of Budget for now: */

    @Override
    public void updateBudgetService(Budget budget) {
        if (budgetRepository.findById(budget.getId()).isPresent()){
            Budget budgetToUpdateFromDB = budgetRepository.findById(budget.getId()).get();
            Double oldBudgetAmount = budgetToUpdateFromDB.getAmount();
            Double oldBudgetAmountRemains = budgetToUpdateFromDB.getAmountRemains();
            Double newAmountUpdated = budget.getAmount();
            if (newAmountUpdated > oldBudgetAmount){//In case user Increase the amount Budget, the
                budget.setAmount(newAmountUpdated); // amountRemains should be increased too.
                budget.setAmountRemains(oldBudgetAmountRemains
                        + (newAmountUpdated - oldBudgetAmount));
                budgetRepository.save(budget);
            } else if (newAmountUpdated < oldBudgetAmount) {//amountRemains will be decreased in case user update amountBudget < than the old amount budget:
                budget.setAmount(newAmountUpdated);
                budget.setAmountRemains(oldBudgetAmountRemains - (oldBudgetAmount - newAmountUpdated));
                budgetRepository.save(budget);
            }else {//In case: Nothing is changed:
                budgetRepository.save(budget);
            }
        }
    }

    @Override
    public void deleteBudgetService(Budget budget) {
        try {
            budgetRepository.delete(budget);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Page<Budget> getBudgetsByPageAndSizeAndTitleService(String title, int page, int size) {
        return budgetRepository.findByTitleContaining(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Budget> getBudgetsByPageAndSizeAndTitleAndUserIdService(String title, String userId, int page, int size) {
        try {
            return budgetRepository
                    .findByTitleContainingAndUserId(title, UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Budget(s) were Not Found!");
        }
    }

    @Override
    public Page<Goal> getGoalsByPageAndSizeAndTitleAndUserIdService(String title, String userId, int page, int size) {
        try {
            return goalRepository.findByTitleContainingAndUserId(title,
                    UUID.fromString(userId),
                    PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Goal(s) were Not Found!");
        }
    }

    @Override
    public Page<Income> getIncomesByPageAndSizeAndTitleAndUserIdService(String incomeTitle, String userId,
                                                                        int page, int size) {
        try {
            return incomeRepository.
                    findByTitleContainingAndUserId(incomeTitle, UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Income(s) were Not Found!");
        }
    }

    /**Algorithm Works well :) */
    @Override
    public void calculateGoalsOnAddIncomeService(Income income) {
        //Compute 'amountAchieved' of Goal & is 'goalAchieved':
        //Check for Nulls:
        //Check If the Common List Of "ResultDTOIncomesGoals" is Empty OR Not:
        incomeRepository.save(income);
        List<ResultDTOIncomesGoals> resultDTOIncomesGoals = incomeRepository
                .onOneIncomeComputeOnCommonGoals(income.getId());
        List<Goal> listGoalsToUpdateOnAddIncome=new ArrayList<>();
        try {
            if (!resultDTOIncomesGoals.isEmpty()){//If Common Goals were founds:
                for (ResultDTOIncomesGoals resultDTOIncomeGoal: resultDTOIncomesGoals){
                    System.out.println("Inside Common Goals & Income For LOOP:");
                    Goal goal = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                            .orElseThrow(() -> {
                                throw new RuntimeException("Error, Cannot get Goal from Database!");
                            });
                    if (goal!=null && (goal.getAmountAchieved()==null || goal.getAmountAchieved()==0) ){
                        goal.setAmountAchieved(resultDTOIncomeGoal.getAmountIncome());
                        //if (goal.getGoalAchieved()==null){
                         if (goal.getAmount() > goal.getAmountAchieved()){
                                goal.setGoalAchieved(false);
                         }else if (goal.getAmount() <= goal.getAmountAchieved()){
                                goal.setGoalAchieved(true);
                         }
                        listGoalsToUpdateOnAddIncome.add(goal);
                    }else if (goal!=null && (goal.getAmountAchieved()!=null || goal.getAmountAchieved()>=0)){
                        goal.setAmountAchieved(goal.getAmountAchieved() + income.getAmount());
                        if (goal.getAmount() > goal.getAmountAchieved()){
                            goal.setGoalAchieved(false);
                        }else if (goal.getAmount() <= goal.getAmountAchieved()){
                            goal.setGoalAchieved(true);
                        }
                        listGoalsToUpdateOnAddIncome.add(goal);
                    }
                }
                goalRepository.saveAll(listGoalsToUpdateOnAddIncome);
            }else {//Means No Common Goals were founds:
                System.out.println("No Common Goals were founds For Income Added ....");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("An error Occurred while Compute Common Income & Common Goal(s)");
        }

    }

    // Completed Algorithm to compute Goals On Update Only 'AMOUNT' of Income.
    @Override
    public void calculateGoalsOnUpdateIncomeService(Income income) {
        //Compute 'amountAchieved' of Goal & is 'goalAchieved':
        //Check for Nulls:
        //Check If the Common List Of "ResultDTOIncomesGoals" is Empty OR Not:
        List<Goal> goalListUpdatedOnUpdateIncome = new ArrayList<>();
        List<ResultDTOIncomesGoals> resultDTOIncomesGoals =
                incomeRepository.onOneIncomeComputeOnCommonGoals(income.getId());
        try {
            if (!resultDTOIncomesGoals.isEmpty()){

                Double oldIncomeAmount = resultDTOIncomesGoals.get(0).getAmountIncome();
                Double newIncomeAmount = income.getAmount();
                Double intervalNewOldAmountIncome = newIncomeAmount - oldIncomeAmount;
                for (ResultDTOIncomesGoals resultDTOIncomeGoal: resultDTOIncomesGoals) {
                    Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                            .orElseThrow(() -> {
                               throw new RuntimeException("Error On get Goal From DB");
                            });
                    if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
                        goalFromDBToUpdate.setAmountAchieved(newIncomeAmount);
                        if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                            goalFromDBToUpdate.setGoalAchieved(false);
                        } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                            goalFromDBToUpdate.setGoalAchieved(true);
                        }
                        goalListUpdatedOnUpdateIncome.add(goalFromDBToUpdate);
                    } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
                        goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() + intervalNewOldAmountIncome);
                        if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                            goalFromDBToUpdate.setGoalAchieved(false);
                        } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                            goalFromDBToUpdate.setGoalAchieved(true);
                        }
                        goalListUpdatedOnUpdateIncome.add(goalFromDBToUpdate);
                    }
                }
                incomeRepository.save(income);
                goalRepository.saveAll(goalListUpdatedOnUpdateIncome);
            }else {//Normal Update If there is No common goals:
                incomeRepository.save(income);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //TODO: Uncompleted Full Update of Income!
    @Override
    public void calculateGoalsOnUpdateFullIncomeServiceV2(Income incomeUpdated) {
        //Check IF Income has Common Goals:
        List<ResultDTOIncomesGoals> newResultDTOIncomesGoals
                = incomeRepository.onOneIncomeComputeOnCommonGoalsV2(incomeUpdated.getId(),
                                                                     incomeUpdated.getCategoryIncome().getId(),
                                                                     incomeUpdated.getCreatedDate());
        List<ResultDTOIncomesGoals> oldResultDTOIncomesGoals
                = incomeRepository.onOneIncomeComputeOnCommonGoals(incomeUpdated.getId());
        List<Goal> goalListToUpdate = new ArrayList<>();

        //excludeOldIncomeAmountUpdatedFromAllOldGoals(oldResultDTOIncomesGoals);
        if (!oldResultDTOIncomesGoals.isEmpty()){
            boolean sameCategoryIncomeId = Objects.equals(incomeUpdated.getCategoryIncome().getId()
                    , oldResultDTOIncomesGoals.get(0).getCategory_income_id_Income());

            if (!sameCategoryIncomeId){
                excludeOldIncomeAmountUpdatedFromAllOldGoals(oldResultDTOIncomesGoals);
                //incomeRepository.save(incomeUpdated);
//                List<ResultDTOIncomesGoals> newResultDTOIncomesGoals = incomeRepository
//                        .onOneIncomeComputeOnCommonGoals(incomeUpdated.getId());
                if (!newResultDTOIncomesGoals.isEmpty()){
                    includeIncomeAmountToNewDiffGoals(incomeUpdated, newResultDTOIncomesGoals);
                }
            } else {
                // Loop On Old Joint & Check for Date Inclusion:
                for (ResultDTOIncomesGoals resultDTOIncomeGoal: oldResultDTOIncomesGoals) {
                    Date fromLocalDate = Date.from(resultDTOIncomeGoal.getEndDate().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant());
                    boolean dateIncludedFromIncomeNewDate = incomeUpdated.getCreatedDate()
                            .compareTo(resultDTOIncomeGoal.getDateDebut()) == 0
                      || incomeUpdated.getCreatedDate().compareTo(fromLocalDate) == 0
                      || (incomeUpdated.getCreatedDate().compareTo(resultDTOIncomeGoal.getDateDebut())>0
                            && incomeUpdated.getCreatedDate().compareTo(fromLocalDate)<0);
                    if (dateIncludedFromIncomeNewDate){
                      //  incomeRepository.save(incomeUpdated);
                        includeIncomeAmountToOneOldGoal(incomeUpdated, resultDTOIncomeGoal);
                    }else {
                        excludeOldIncomeAmountUpdatedFromAllOldGoals(oldResultDTOIncomesGoals);
//                        incomeRepository.save(incomeUpdated);
//                        List<ResultDTOIncomesGoals> newResultDTOIncomesGoals = incomeRepository
//                                .onOneIncomeComputeOnCommonGoals(incomeUpdated.getId());
                        if (!newResultDTOIncomesGoals.isEmpty()){
                            includeIncomeAmountToNewDiffGoals(incomeUpdated, newResultDTOIncomesGoals);
                        }
                    }
                }
                //THEN save
            }
        }else {//Case Old Joint is Empty:
            //Normal Update Computations On New Joint if exists... :
        }


    }

    private void includeIncomeAmountToOldGoals(Income incomeUpdated, List<ResultDTOIncomesGoals> oldResultDTOIncomesGoals) {
        List<Goal> goalListToUpdate = new ArrayList<>();
        Double oldIncomeAmount = oldResultDTOIncomesGoals.get(0).getAmountIncome();
        Double newIncomeAmount = incomeUpdated.getAmount();
        Double intervalNewOldAmountIncome = newIncomeAmount - oldIncomeAmount;
        for (ResultDTOIncomesGoals resultDTOIncomeGoal: oldResultDTOIncomesGoals) {
            Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
                goalFromDBToUpdate.setAmountAchieved(newIncomeAmount);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                goalListToUpdate.add(goalFromDBToUpdate);
            } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
                goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() + intervalNewOldAmountIncome);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                goalListToUpdate.add(goalFromDBToUpdate);
            }
        }
        //incomeRepository.save(income);
        goalRepository.saveAll(goalListToUpdate);
    }

    private void includeIncomeAmountToOneOldGoal(Income incomeUpdated, ResultDTOIncomesGoals oldResultDTOIncomeGoal) {
        List<Goal> goalListToUpdate = new ArrayList<>();
        Double oldIncomeAmount = oldResultDTOIncomeGoal.getAmountIncome();
        Double newIncomeAmount = incomeUpdated.getAmount();
        Double intervalNewOldAmountIncome = newIncomeAmount - oldIncomeAmount;

            Goal goalFromDBToUpdate = goalRepository.findById(oldResultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
                goalFromDBToUpdate.setAmountAchieved(newIncomeAmount);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                //goalListToUpdate.add(goalFromDBToUpdate);
                goalRepository.save(goalFromDBToUpdate);
            } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
                goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() + intervalNewOldAmountIncome);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                goalRepository.save(goalFromDBToUpdate);
               // goalListToUpdate.add(goalFromDBToUpdate);
            }
        //incomeRepository.save(income);
        //goalRepository.saveAll(goalListToUpdate);
    }

    /**Case Income Full Update To exclude 1 Old Goal at a time: Income Has Old common Goals. */
    private void excludeIncomeAmountFromOldGoal(List<Goal> goalListToUpdate,
                                                        ResultDTOIncomesGoals resultDTOIncomeGoal){
        Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                .orElseThrow(() -> {
                    throw new RuntimeException("Error On get Goal From DB");
                });
        if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
            goalFromDBToUpdate.setAmountAchieved(0.0);
            goalFromDBToUpdate.setGoalAchieved(false);
            goalListToUpdate.add(goalFromDBToUpdate);
        } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
            goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() - resultDTOIncomeGoal.getAmountIncome());
            if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                goalFromDBToUpdate.setGoalAchieved(false);
            } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                goalFromDBToUpdate.setGoalAchieved(true);
            }
            goalListToUpdate.add(goalFromDBToUpdate);
        }
        goalRepository.saveAll(goalListToUpdate);
    }

    /**Case Income Full Update To exclude Old Goals: Income Has Old common Goals. */
    private void excludeIncomeAmountFromAllOldGoals(List<Goal> goalListToUpdate,
                                                 List<ResultDTOIncomesGoals> resultDTOIncomesGoals){
        for (ResultDTOIncomesGoals resultDTOIncomeGoal: resultDTOIncomesGoals) {
            Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
                goalFromDBToUpdate.setAmountAchieved(0.0);
                goalFromDBToUpdate.setGoalAchieved(false);
                goalListToUpdate.add(goalFromDBToUpdate);
            } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
                goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() - resultDTOIncomeGoal.getAmountIncome());
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                goalListToUpdate.add(goalFromDBToUpdate);
            }

        }
        goalRepository.saveAll(goalListToUpdate);
    }

    /**Case Income Full Update To exclude Income Amount's Interval From All Old Goals: Income Has Old common Goals. */
    private void excludeOldIncomeAmountUpdatedFromAllOldGoals(
                                                    List<ResultDTOIncomesGoals> resultDTOIncomesGoals){
        List<Goal> goalListToUpdate = new ArrayList<>();
        for (ResultDTOIncomesGoals resultDTOIncomeGoal: resultDTOIncomesGoals) {
            Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0)){
                goalFromDBToUpdate.setAmountAchieved(0.0);
                goalFromDBToUpdate.setGoalAchieved(false);
                goalListToUpdate.add(goalFromDBToUpdate);
            } else if (goalFromDBToUpdate!=null && (goalFromDBToUpdate.getAmountAchieved()!=null || goalFromDBToUpdate.getAmountAchieved()>=0)) {
                //Double incomeUpdatedAmount = incomeUpdated.getAmount();
                Double oldIncomeAmount = resultDTOIncomeGoal.getAmountIncome();
//                Double intervalAmountIncome = newIncomeAmount - oldIncomeAmount;
                goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() - oldIncomeAmount);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                goalListToUpdate.add(goalFromDBToUpdate);
            }

        }
        goalRepository.saveAll(goalListToUpdate);
    }

    /**Case Income Full Update: Income Has No Old common Goals.*/
    private void includeIncomeAmountToNewDiffGoals(Income incomeUpdated,
                                         List<ResultDTOIncomesGoals> newResultDTOIncomesGoals
                                         ){
        List<Goal> goalListToUpdate = new ArrayList<>();
        Double incomeUpdatedAmount = incomeUpdated.getAmount();
        for (ResultDTOIncomesGoals resultDTOIncomeGoal: newResultDTOIncomesGoals) {
            Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            goalFromDBToUpdate.setAmountAchieved(0.0);
            goalFromDBToUpdate.setGoalAchieved(false);
            goalListToUpdate.add(goalFromDBToUpdate);
        }
        goalRepository.saveAll(goalListToUpdate);
        incomeRepository.save(incomeUpdated);
        /**New Joint for every Goal with its Incomes below : */
        List<Goal> newGoalListToUpdate = new ArrayList<>();
        for (ResultDTOIncomesGoals resultDTOIncomeGoal: newResultDTOIncomesGoals) {
            //Get the New Joint Result for Every Goal:
            Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error On get Goal From DB");
                    });
            Double incomeAmountFromNewJoint=0.0;//TODO: temporarily 0.0, It should be changed later!
            if (goalFromDBToUpdate != null && (goalFromDBToUpdate.getAmountAchieved() == null || goalFromDBToUpdate.getAmountAchieved() == 0)) {
                goalFromDBToUpdate.setAmountAchieved(incomeAmountFromNewJoint);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                newGoalListToUpdate.add(goalFromDBToUpdate);
            } else if (goalFromDBToUpdate != null && (goalFromDBToUpdate.getAmountAchieved() != null || goalFromDBToUpdate.getAmountAchieved() >= 0)) {
                goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() + incomeAmountFromNewJoint);
                if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(false);
                } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                    goalFromDBToUpdate.setGoalAchieved(true);
                }
                newGoalListToUpdate.add(goalFromDBToUpdate);
            }
        }
        //save again 2nd Update of All Goals After the second New Joint of every Goal to Incomes:
        goalRepository.saveAll(newGoalListToUpdate);
    }

    @Override
    public void calculateGoalsOnDeleteIncomeService(Income income) {
        //Compute 'amountAchieved' of Goal & is 'goalAchieved':
        //Check for Nulls:
        //Check If the Common List Of "ResultDTOIncomesGoals" is Empty OR Not:
        List<Goal> goalListUpdatedOnDeleteIncome = new ArrayList<>();
        List<ResultDTOIncomesGoals> resultDTOIncomesGoals =
                incomeRepository.onOneIncomeComputeOnCommonGoals(income.getId());
        try {
            if (!resultDTOIncomesGoals.isEmpty()){
                for (ResultDTOIncomesGoals resultDTOIncomeGoal : resultDTOIncomesGoals){
                    Goal goalFromDBToUpdate = goalRepository.findById(resultDTOIncomeGoal.getIdGoal())
                            .orElseThrow(() -> {
                                throw new RuntimeException("Error On get Goal From DB");
                            });
                        if (goalFromDBToUpdate.getAmountAchieved()==null || goalFromDBToUpdate.getAmountAchieved()==0){//I was setting 'amountAchieved' to 0.0 & 'goalAchieved' to false THEN add the 'Goal' to 'listGoalUpd'..
                            System.out.println("'AmountAchieved' OR 'goalAchieved' is NULL, No Computation Needed..");
                            continue;
                        }
                        else if (goalFromDBToUpdate.getAmountAchieved() != null || goalFromDBToUpdate.getAmountAchieved() > 0) {

                            goalFromDBToUpdate.setAmountAchieved(goalFromDBToUpdate.getAmountAchieved() - income.getAmount());
                            if (goalFromDBToUpdate.getAmount() > goalFromDBToUpdate.getAmountAchieved()){
                                goalFromDBToUpdate.setGoalAchieved(false);
                            } else if (goalFromDBToUpdate.getAmount() <= goalFromDBToUpdate.getAmountAchieved()) {
                                goalFromDBToUpdate.setGoalAchieved(true);
                            }
                            goalListUpdatedOnDeleteIncome.add(goalFromDBToUpdate);

                        }
                }
                goalRepository.saveAll(goalListUpdatedOnDeleteIncome);
                incomeRepository.delete(income);

            }else {//If there is No Common Goals with Income:
                incomeRepository.delete(income);
                System.out.println("No Common Goals were founds For Income Delete ....");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Unexpected Error Occurred while Delete Income!");
        }
    }

    @Override
    public List<TotalIncomesPerMonthDTO> getTotalIncomesPerYearMonthAndUserService(String userId) {
        try {
            List<TotalIncomesPerMonthDTO> totalIncomesPerMonthDTOS
                    = incomeRepository.getTotalAmountIncomesOnEveryMonth(UUID.fromString(userId));
            for (int i = 0; i < totalIncomesPerMonthDTOS.size(); i++) {

                double amountInterval;
                double percentOfAmountInterval;
                if (i < totalIncomesPerMonthDTOS.size()-1){
                    amountInterval = ( totalIncomesPerMonthDTOS.get(i).getTotalIncomes()
                            - totalIncomesPerMonthDTOS.get(i+1).getTotalIncomes() );
                    percentOfAmountInterval = ( amountInterval /
                            totalIncomesPerMonthDTOS.get(i).getTotalIncomes() ) * 100;

                    totalIncomesPerMonthDTOS.get(i).setAmountInterval(amountInterval);
                    totalIncomesPerMonthDTOS.get(i).setPercentOfAmountInterval(percentOfAmountInterval);
                } else if (i== totalIncomesPerMonthDTOS.size()-1) {
                    if (totalIncomesPerMonthDTOS.get(i).getAmountInterval()==null){
                        totalIncomesPerMonthDTOS.get(i).setAmountInterval(0.0);
                    }
                    if (totalIncomesPerMonthDTOS.get(i).getPercentOfAmountInterval()==null){
                        totalIncomesPerMonthDTOS.get(i).setPercentOfAmountInterval(0.0);
                    }
                }

            }
            return totalIncomesPerMonthDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while getting Incomes Sum by user!");
        }
    }

    @Override
    public List<IncomesByCategory> getIncomesSumByCategoryAndUserIdService(String userId) {
        try {
            List<IncomesByCategory> incomesByCategoriesAndUser = incomeRepository
                    .getTotalIncomesByCategoryAndUser(
                            UUID.fromString(userId));
            Double totalSumsOfIncome = 0.0;
            for (IncomesByCategory incomesByCategory: incomesByCategoriesAndUser) {
                totalSumsOfIncome+=incomesByCategory.getTotalIncomesByCategory();
            }
            double percentOfIncomesPerMonth;
            for (IncomesByCategory incomesByCategory: incomesByCategoriesAndUser) {
                percentOfIncomesPerMonth =  ((incomesByCategory.getTotalIncomesByCategory() / totalSumsOfIncome) * 100);
                incomesByCategory.setPercentOfIncomesPerMonth(percentOfIncomesPerMonth);
            }
            return incomesByCategoriesAndUser;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Incomes By Category & userId!...");
        }
    }

}

