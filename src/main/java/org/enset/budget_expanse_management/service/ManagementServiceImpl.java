package org.enset.budget_expanse_management.service;

import jdk.jfr.RecordingState;
import org.enset.budget_expanse_management.formModel.BudgetFormSubmission;
import org.enset.budget_expanse_management.formModel.GoalFormSubmission;
import org.enset.budget_expanse_management.formModel.IncomeFormSubmission;
import org.enset.budget_expanse_management.mapping.*;
import org.enset.budget_expanse_management.model.*;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Transactional
@Service
public class ManagementServiceImpl implements BudgetExpanseManagementService {

    private final ExpanseRepository expanseRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryExpanseRepository categoryExpanseRepository;
    private final IncomeRepository incomeRepository;
    private final GoalRepository goalRepository;
    private final CategoryIncomeRepository categoryIncomeRepository;
//
    private final UserRepository userRepository;

    public ManagementServiceImpl(ExpanseRepository expanseRepository,
                                 BudgetRepository budgetRepository,
                                 CategoryExpanseRepository categoryExpanseRepository,
                                 IncomeRepository incomeRepository,
                                 GoalRepository goalRepository,
                                 CategoryIncomeRepository categoryIncomeRepository,
                                 UserRepository userRepository
    ) {
        this.expanseRepository = expanseRepository;
        this.budgetRepository = budgetRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
        this.incomeRepository = incomeRepository;
        this.goalRepository = goalRepository;
        this.categoryIncomeRepository = categoryIncomeRepository;
        this.userRepository = userRepository;
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
      //  return expanseRepository.findByTitleContaining(title, PageRequest.of(page, size));
        return expanseRepository.findByTitleContainingOrderByCreatedDateDesc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Expanse> getExpansesByPageAndSizeAndTitleAndUserIdService(String title, String userId,
                                                                          int page, int size) {
        try {
            return expanseRepository
//                    .findByTitleContainingAndUserId(title, UUID.fromString(userId), PageRequest.of(page, size));
                    .findByTitleContainingAndUserIdOrderByCreatedDateDesc(title, UUID.fromString(userId), PageRequest.of(page, size));
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
            return computeTotalSumAndPercentOnExpCat(expensesByCategoryAndUser);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Expenses By Category Ordered By Date!...");
        }
    }

    @Override
    public List<ExpensesByCategory> getExpensesSumByCategoryAndUserIdAmountDescService(String userId) {
        try {
            List<ExpensesByCategory> expensesByCategoryAndUserAmountDesc = expanseRepository
                    .getTotalExpensesByCategoryAndUserAmountDesc(
                            UUID.fromString(userId));
            return computeTotalSumAndPercentOnExpCat(expensesByCategoryAndUserAmountDesc);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Expenses By Category Ordered By Amount!...");
        }
    }

    private List<ExpensesByCategory> computeTotalSumAndPercentOnExpCat(List<ExpensesByCategory> expensesByCategoryAndUser) {
        System.out.println();
        System.out.println("Refactored block of code: computeTotalSumAndPercentOnExpCat(...) ");
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

    @Override
    public TotalExpansePerMonthDTO getTotalExpansesPerLifeTimeAndUserService(String userId) {
        try {
            TotalExpansePerMonthDTO expansesOnLifeTimeByUserId =
                    expanseRepository.getTotalAmountExpansesOnLifeTimeByUserId(UUID.fromString(userId));
            if(expansesOnLifeTimeByUserId.getTotalExpanses()==null){
                expansesOnLifeTimeByUserId.setTotalExpanses(0.0);
            }
            return expansesOnLifeTimeByUserId;
//            return expanseRepository.getTotalAmountExpansesOnLifeTimeByUserId(UUID.fromString(userId));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while getting Total Expenses for a Life Time by user!");
        }
    }

    /** Query to get Total Amount of Expanses per Month By UserID: */
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
                    .findByTitleContainingAndUserIdOrderByDateDebutDesc(title, UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Budget(s) were Not Found!");
        }
    }

    @Override
    public Page<Goal> getGoalsByPageAndSizeAndTitleAndUserIdService(String title, String userId, int page, int size) {
        try {
            Page<Goal> goalPage = goalRepository.findByTitleContainingAndUserIdOrderByDateDebutDescEndDateDesc(title,
                    UUID.fromString(userId),
                    PageRequest.of(page, size));
            for (Goal goal: goalPage){
                if (goal.getAmountAchieved()==null){
                    goal.setAmountAchieved(0.0);
                }
                if (goal.getGoalAchieved()==null){
                    goal.setGoalAchieved(false);
                }
            }
            return goalPage;
//            return goalRepository.findByTitleContainingAndUserIdOrderByDateDebutDescEndDateDesc(title,
//                    UUID.fromString(userId),
//                    PageRequest.of(page, size));

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
                   // findByTitleContainingAndUserId(incomeTitle, UUID.fromString(userId), PageRequest.of(page, size));
                    findByTitleContainingAndUserIdOrderByCreatedDateDesc(incomeTitle, UUID.fromString(userId), PageRequest.of(page, size));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("User OR Income(s) were Not Found!");
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
    public TotalIncomesPerMonthDTO getTotalIncomesPerLifeTimeAndUserService(String userId) {
        try {
            TotalIncomesPerMonthDTO totalAmountIncomeOnLifeTime =
                    incomeRepository.getTotalAmountIncomeOnLifeTime(UUID.fromString(userId));
            if(totalAmountIncomeOnLifeTime.getTotalIncomes()==null){
                totalAmountIncomeOnLifeTime.setTotalIncomes(0.0);
            }
            return totalAmountIncomeOnLifeTime;
//            return incomeRepository.getTotalAmountIncomeOnLifeTime(UUID.fromString(userId));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while getting Total Incomes for a Life Time by user!");
        }
    }

    @Override
    public List<IncomesByCategory> getIncomesSumByCategoryAndUserIdService(String userId) {
        try {
            List<IncomesByCategory> incomesByCategoriesAndUser = incomeRepository
                    .getTotalIncomesByCategoryAndUser(
                            UUID.fromString(userId));
            return computeTotalSumAndPercentOnIncomesCat(incomesByCategoriesAndUser);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Incomes By Category & userId!...");
        }
    }

    private List<IncomesByCategory> computeTotalSumAndPercentOnIncomesCat(List<IncomesByCategory> incomesByCategoriesAndUser) {
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
    }

    @Override
    public List<IncomesByCategory> getIncomesSumByCategoryAndUserIdDateDescService(String userId) {
        try {
            List<IncomesByCategory> incomesByCategoriesAndUser = incomeRepository
                    .getTotalIncomesByCategoryAndUserOrderedByDate(
                            UUID.fromString(userId));
            return computeTotalSumAndPercentOnIncomesCat(incomesByCategoriesAndUser);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something Went wrong on Getting Sum of Incomes By Category & userId!...");
        }
    }

    @Override
    public void calculateIncomesOnAddGoalService(Goal goal) {
        try {
            //1) Save new Goal TO DB Before Computation On GoalAchievement & AmountGoal:
            goalRepository.save(goal);
            //Convert 'LocalDate' Of Goal into 'Date':
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date goalEndDate = Date.from(goal.getEndDate().atStartOfDay(defaultZoneId).toInstant());
            //2) Get Common Incomes if exists:
            ResultDTOGoalAndIncomes resultDTOGoalAndIncomes =
                    goalRepository.onAddGoalComputeOnCommonIncomes(goal.getId(), goal.getDateDebut(), goalEndDate);
            //3) Loop On List & Check If the list is empty Or Not:
            if (resultDTOGoalAndIncomes.getIdGoal()!=null){
                Double amountAchieved = resultDTOGoalAndIncomes.getAmountIncomeSum();
                goal.setAmountAchieved(amountAchieved);
                if (goal.getDescription()==null) {
                    goal.setDescription("");
                }
                if (goal.getAmount() > amountAchieved){
                    goal.setGoalAchieved(false);
                } else if (goal.getAmount() <= amountAchieved) {
                    goal.setGoalAchieved(true);
                }
                //4) Save new Goal TO DB Before Computation On GoalAchievement & AmountGoal:
                goalRepository.save(goal);
            }else if (resultDTOGoalAndIncomes.getIdGoal()==null){
                eliminateNullsOnNewGoal(goal);
//                if (goal.getDescription()==null) goal.setDescription("");
//                if (goal.getAmountAchieved()==null) goal.setAmountAchieved(0.0);
//                if (goal.getGoalAchieved()==null) goal.setGoalAchieved(false);
                goalRepository.save(goal);
                System.out.println("No Common Incomes related to New Goal Added ...");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("e.getMessage(): " + e.getMessage());
        }
    }

    @Override
    public void calculateIncomesOnAddGoalServiceV2(Goal goal) {
        try {//1) Get Common Incomes with Goal:
            //Convert 'LocalDate' Of Goal into 'Date':
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date goalEndDate = Date.from(goal.getEndDate().atStartOfDay(defaultZoneId).toInstant());
            List<CommonIncome> commonIncomesOnAddNewGoalList =
                    goalRepository.getCommonIncomesOnAddNewGoal(goal.getUser().getId(), goal.getCategoryIncome().getId(),
                            goal.getDateDebut(), goalEndDate);
            //2) Check If the CommonIncomes List is Empty:
            if (commonIncomesOnAddNewGoalList.get(0).getId()==null){
                eliminateNullsOnNewGoal(goal);
                goalRepository.save(goal);
            }else {
                eliminateNullsOnNewGoal(goal);
                goal.setAmountAchieved(commonIncomesOnAddNewGoalList.get(0).getAmountSum());
                //3) Check if Goal is Achieved or Not:
                if (goal.getAmount() > goal.getAmountAchieved()){
                    goal.setGoalAchieved(false);
                } else if (goal.getAmount() <= goal.getAmountAchieved()) {
                    goal.setGoalAchieved(true);
                }
                goalRepository.save(goal);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("e.getCause(): "+e.getCause());
            System.out.println("e.getMessage(): "+e.getMessage());
        }
    }

    @Override
    public void deleteGoalService(Integer goalId) {
        try {
            Goal goalToDelete = goalRepository.findById(goalId).orElseThrow(
                        () -> {
                            throw new RuntimeException("Goal was Not found! ...");
                        }
                );
            goalRepository.delete(goalToDelete);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while delete Goal! ...");
        }
    }

    @Override
    public void updateGoalService(Goal goalUpdated) {
        try {
            Goal goalToUpdateFromDB = goalRepository.findById(goalUpdated.getId())
                    .orElseThrow(
                            () -> {
                                throw new RuntimeException("Cannot find Goal to update from DB!");
                            }
                    );
            if (goalToUpdateFromDB!=null){//1)If goal is Not Null
                //2)If User Modify just the Goal's amount:
                if (Objects.equals(goalUpdated.getCategoryIncome().getId(), goalToUpdateFromDB.getCategoryIncome().getId())
                    && goalUpdated.getEndDate()==goalToUpdateFromDB.getEndDate()
                    && goalUpdated.getDateDebut()==goalToUpdateFromDB.getDateDebut()){
                    if (goalUpdated.getAmount() > goalUpdated.getAmountAchieved()){
                        goalUpdated.setGoalAchieved(false);
                        goalRepository.save(goalUpdated);
                    } else if (goalUpdated.getAmount() <= goalUpdated.getAmountAchieved()) {
                        goalUpdated.setGoalAchieved(true);
                        goalRepository.save(goalUpdated);
                    }
                } else if (!Objects.equals(goalUpdated.getCategoryIncome().getId(), goalToUpdateFromDB.getCategoryIncome().getId())
                        || goalUpdated.getEndDate()!=goalToUpdateFromDB.getEndDate()
                        || goalUpdated.getDateDebut()!=goalToUpdateFromDB.getDateDebut()) {//3)If User Modify also CategoryIncome OR Date Range:
                    //4)Empty the Goal from 'amountAchieved' & 'goalAchieved' THEN Recalculate them by getting
                    //   Common Incomes from DB:
                    //Convert 'LocalDate' Of Goal into 'Date':
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    Date goalUpdatedEndDate = Date.from(goalUpdated.getEndDate().atStartOfDay(defaultZoneId).toInstant());
                    List<CommonIncome> commonIncomesOnUpdatedGoal = goalRepository
                            .getCommonIncomesOnAddNewGoal(goalUpdated.getUser().getId(),
                                                          goalUpdated.getCategoryIncome().getId(),
                                                          goalUpdated.getDateDebut(),
                                                          goalUpdatedEndDate);

                    if (commonIncomesOnUpdatedGoal.get(0).getId()!=null){//5)Check if it has Common Incomes:
                        //6)Empty the 'amountAchieved' & 'goalAchieved' in order to Recalculate them:
                        goalUpdated.setAmountAchieved(commonIncomesOnUpdatedGoal.get(0).getAmountSum());
                        goalUpdated.setGoalAchieved(false);
                        if (goalUpdated.getAmount() <= goalUpdated.getAmountAchieved()){
                            goalUpdated.setGoalAchieved(true);
                            goalRepository.save(goalUpdated);
                        } else if (goalUpdated.getAmount() > goalUpdated.getAmountAchieved()) {
                            goalUpdated.setGoalAchieved(false);
                            goalRepository.save(goalUpdated);
                        }
                    }else {//7)Check if it has change 'CategoryIncome' or 'Date' Range BUT still got NO Common Incomes:
                        goalUpdated.setAmountAchieved(0.0); goalUpdated.setGoalAchieved(false);
                        goalRepository.save(goalUpdated);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Goal mapNewFormGoalObjToGoalObj(GoalFormSubmission goalFormSubmission) {
        Goal goal = new Goal();
        goal.setTitle(goalFormSubmission.getTitle());
        goal.setDescription(goalFormSubmission.getDescription());
        goal.setAmount(goalFormSubmission.getAmount());
        goal.setDateDebut(goalFormSubmission.getDateDebut());
        goal.setEndDate(goalFormSubmission.getEndDate());
        CategoryIncome categoryIncome = categoryIncomeRepository
                .findById(goalFormSubmission.getCategoryIncome()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Cannot find 'Category of Income' from DB!");
                        }
                );
        User user = userRepository.findById(UUID.fromString(goalFormSubmission.getUserId()))
                .orElseThrow(
                        () -> {
                            throw new RuntimeException("Cannot find 'User' from DB!");
                        }
                );
        goal.setUser(user);
        goal.setCategoryIncome(categoryIncome);
        return goal;
    }

    @Override
    public Income mapNewFormIncomeObjToIncomeObj(IncomeFormSubmission incomeFormSubmission) {
        Income income = new Income();
        income.setTitle(incomeFormSubmission.getTitle());
        income.setAmount(incomeFormSubmission.getAmount());
        income.setCreatedDate(incomeFormSubmission.getCreatedDate());
        CategoryIncome categoryIncome = getCategoryIncomeFromIncomeForm(incomeFormSubmission);
        User user = getUserFromIncomeFormObj(incomeFormSubmission);
        income.setUser(user);
        income.setCategoryIncome(categoryIncome);
        return income;
    }

    private User getUserFromIncomeFormObj(IncomeFormSubmission incomeFormSubmission) {
        return userRepository.findById(UUID.fromString(incomeFormSubmission.getUserId()))
                .orElseThrow(
                        () -> {
                            throw new RuntimeException("Cannot find 'User' from DB!");
                        }
                );
    }

    private CategoryIncome getCategoryIncomeFromIncomeForm(IncomeFormSubmission incomeFormSubmission) {
       return categoryIncomeRepository
                .findById(incomeFormSubmission.getCategoryIncome()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Cannot find 'Category of Income' from DB!");
                        }
                );
    }

    @Override
    public Budget mapNewFormBudgetObjToBudgetObj(BudgetFormSubmission budgetFormSubmission) {
        Budget budget = new Budget();
        budget.setAmount(budgetFormSubmission.getAmount());
        budget.setTitle(budgetFormSubmission.getTitle());
        budget.setDescription(budgetFormSubmission.getDescription());
        budget.setDateDebut(budgetFormSubmission.getDateDebut());
        budget.setEndDate(budgetFormSubmission.getEndDate());
        CategoryExpanse categoryExpanse = categoryExpanseRepository
                .findById(budgetFormSubmission.getCategoryExpanse()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Category of Expanse was not found from DB!");
                        }
                );
        User user = userRepository
                .findById(UUID.fromString(budgetFormSubmission.getUserId())).orElseThrow(
                        () -> {
                            throw new RuntimeException("User was not found from DB!");
                        }
                );
        budget.setCategoryExpanse(categoryExpanse);
        budget.setUser(user);
        return budget;
    }

    @Override
    public void calculateGoalsOnAddIncomeServiceV2(Income newIncome) {
        try {
            //1) Get Common Goals:
            List<CommonGoal> commonGoalsFromDB = goalRepository
                    .getCommonGoalsOnAddNewIncome(newIncome.getUser().getId()
                            ,newIncome.getCategoryIncome().getId()
                            ,newIncome.getCreatedDate());
            //2) Prepare List to save edited Common Goals If exists:
            List<Goal> goalList = new ArrayList<>();
            //3) Check If the common goals exists:
            if (!commonGoalsFromDB.isEmpty()){
                computeOnCommonGoalsOnAmountIncomeUpdate(newIncome, commonGoalsFromDB, goalList);
                goalRepository.saveAll(goalList);//Save the Updated Common Goal(s) & Save the Income:
                incomeRepository.save(newIncome);
            }else {//4: if there was No common goals:
                incomeRepository.save(newIncome);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIncomeService(Long incomeId) {
        try {
            Income incomeToDelete = incomeRepository.findById(incomeId)
                    .orElseThrow(
                            () -> {
                                throw new RuntimeException("Income is not found, please delete an existing income!");
                    });
            //1) Get Common Goals:
            List<CommonGoal> commonGoalsFromDB = goalRepository
                    .getCommonGoalsOnAddNewIncome(incomeToDelete.getUser().getId()
                            ,incomeToDelete.getCategoryIncome().getId()
                            ,incomeToDelete.getCreatedDate());
            //2) Prepare List to save edited Common Goals If exists:
            List<Goal> goalList = new ArrayList<>();
            //3) Check If the common goals exists:
            if (!commonGoalsFromDB.isEmpty()){
                for (CommonGoal commonGoalFromDB: commonGoalsFromDB){
                    System.out.println("Inside Common Goals & Income For LOOP:");
                    Goal goal = goalRepository.findById(commonGoalFromDB.getId())
                            .orElseThrow(() -> {
                                throw new RuntimeException("Error, Cannot get Goal from DB!");
                            });
                    if (goal!=null && (goal.getAmountAchieved()==null || goal.getAmountAchieved()==0) ){
//                        goal.setAmountAchieved(0.0);goal.setGoalAchieved(false);
//                        goalList.add(goal);
                        continue;
                    }else if (goal!=null && (goal.getAmountAchieved()!=null || goal.getAmountAchieved()>=0)){
                        removeIncomeAmountFromGoalThenCompute(incomeToDelete ,goal , goalList);
                    }
                }
                goalRepository.saveAll(goalList);//Save the Updated Common Goal(s) & Save the Income:
                incomeRepository.delete(incomeToDelete);
            }else {//4: if there was No common goals:
                incomeRepository.delete(incomeToDelete);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeIncomeAmountFromGoalThenCompute(Income income, Goal goal, List<Goal> goalList) {
        Double intervalAmountAchieved = goal.getAmountAchieved() - income.getAmount();
        goal.setAmountAchieved(intervalAmountAchieved);
        if (goal.getAmount() > goal.getAmountAchieved()){
            goal.setGoalAchieved(false);
            goalList.add(goal);
        }else if (goal.getAmount() <= goal.getAmountAchieved()){
            goal.setGoalAchieved(true);
            goalList.add(goal);
        }
    }

    @Override
    public void updateIncomeService(Income incomeUpdated) {
        try {
            //1) Get OLD Income from DB before update:
            Income incomeBeforeUpdateDB = incomeRepository.findById(incomeUpdated.getId())
                    .orElseThrow(
                            () -> {
                                throw new RuntimeException("Cannot find old Income from DB");
                            }
                    );
            //2) Get old Common Goals if exists:
            List<CommonGoal> oldCommonGoalsFromDB = goalRepository.getCommonGoalsOnAddNewIncome
                    (incomeUpdated.getUser().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId(),
                            incomeBeforeUpdateDB.getCreatedDate());
            //2) Get Current Common Goals if exists:
            List<CommonGoal> newCommonGoalsFromDB = goalRepository.getCommonGoalsOnAddNewIncome
                            (incomeUpdated.getUser().getId(), incomeUpdated.getCategoryIncome().getId(),
                            incomeUpdated.getCreatedDate());
            //3) Prepare the list of edited goals to be saved late:
            List<Goal> goalListToUpdate = new ArrayList<>();

            //4) Check if just the Amount of income is updated:
            if (!Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())
                && Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId())
                && incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())==0){
                //4.1) Should get same Old Goals if exists:
                //should act as if I should add new Income to DB:
                System.out.println("************** Same Category Of Income & createdDate are Not changed, BUT 'Amount' was changed ******************");
                if (!newCommonGoalsFromDB.isEmpty()){
                    computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                    goalRepository.saveAll(goalListToUpdate);
                }
            }else if (!Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId())
                      && incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())==0
                      && Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())){
                //4.1) Should get same Old Goals if exists:
                //   ) If only the Category of Income is changed:
                //should act as if I should add new Income to DB:
                System.out.println("************** Same Amount & createdDate of income were Not changed, BUT 'Category' was changed ******************");
                if (!newCommonGoalsFromDB.isEmpty()){
                    computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromOldGoalsThenComputeOnLoop(oldCommonGoalsFromDB, incomeBeforeUpdateDB, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            }else if (incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())!=0
                      && Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId())
                      && Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())){
                //4.1) Should get same Old Goals if exists:
                //   ) If only the Category of Income is changed:
                //should act as if I should add new Income to DB:
                System.out.println("************** Same Category AND Amount Of Income were Not changed But 'createdDate' was changed ******************");
                if (!newCommonGoalsFromDB.isEmpty()){//TODO: Changed on 11/12/2022:
                    computeOnCommonGoalsOnAmountIncomeUpdate(incomeUpdated, newCommonGoalsFromDB, goalListToUpdate);
                    //computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromGoalThenComputeOnLoopBasedOnDate(oldCommonGoalsFromDB, incomeBeforeUpdateDB, incomeUpdated, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            } else if ((!Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId())
                        && incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())!=0
                        && Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount()))
                        ){
            //5) Check if Category OR CreatedDate of income is also updated:
                //5.1) Should get New Goals or + also some Old Goals if exists:
                //TODO: I should check(Duplicate Ids of oldCommonGoals) if the 'oldCommonGoals' List aren't included also
                // in 'newCommonGoals' List in order to decide weather we should erase
                // amount of Income from 'oldCommonGoals' List OR Not Before
                // calling 'removeIncomeAmountFromOldGoalsThenComputeOnLoop(...)':
                System.out.println("************** 'Category' Of Income AND 'CreatedDate' are changed, BUT Not the Amount******************");
                if (!newCommonGoalsFromDB.isEmpty()){
                    //computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                    computeOnCommonGoalsOnAmountIncomeUpdate(incomeUpdated, newCommonGoalsFromDB, goalListToUpdate);
                    goalRepository.saveAll(goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromOldGoalsThenComputeOnLoop(oldCommonGoalsFromDB, incomeBeforeUpdateDB, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            }else if ((!Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())
                        && incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())!=0
                        && Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId()))
                        ){
            //5) Check if Category OR CreatedDate of income is also updated:
                //5.1) Should get New Goals or + also some Old Goals if exists:
                System.out.println("************** 'Amount' Of Income AND 'CreatedDate' were changed, BUT Not Category ******************");
                System.out.println("-- 'incomeUpdated.getCreatedDate()"+ incomeUpdated.getCreatedDate()+"!="+
                        incomeBeforeUpdateDB.getCreatedDate()+"incomeBeforeUpdateDB.getCreatedDate()--");
                System.out.println();
                boolean isDatesEqual = incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())==0;
                System.out.println(" Date Comparison: " + isDatesEqual);
                if (!newCommonGoalsFromDB.isEmpty()){
                    computeNewCommonGoalsOnUpdateIncome(incomeUpdated,
                            incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                    goalRepository.saveAll(goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromGoalThenComputeOnLoopBasedOnDate(oldCommonGoalsFromDB,
                            incomeBeforeUpdateDB, incomeUpdated, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            }else if ((!Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())
                        && !Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId())
                        && incomeUpdated.getCreatedDate().compareTo(incomeBeforeUpdateDB.getCreatedDate())==0 )
                        ){
            //5) Check if Category OR CreatedDate of income is also updated:
                //5.1) Should get New Goals or + also some Old Goals if exists:
                System.out.println("************** 'Amount' Of Income AND 'Category' were changed, BUT Not createdDate ******************");
                if (!newCommonGoalsFromDB.isEmpty()){
                    computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                    goalRepository.saveAll(goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromOldGoalsThenComputeOnLoop(oldCommonGoalsFromDB, incomeBeforeUpdateDB, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            }else if ((!Objects.equals(incomeUpdated.getAmount(), incomeBeforeUpdateDB.getAmount())
                        && !Objects.equals(incomeUpdated.getCategoryIncome().getId(), incomeBeforeUpdateDB.getCategoryIncome().getId()))
                        && incomeUpdated.getCreatedDate() != incomeBeforeUpdateDB.getCreatedDate()){
            //5) Check if Category OR CreatedDate of income is also updated:
                //5.1) Should get New Goals or + also some Old Goals if exists:
                System.out.println("************** 'Amount' Of Income AND 'Category' AND 'createdDate' were changed ******************");
                if (!newCommonGoalsFromDB.isEmpty()){
                    //computeNewCommonGoalsOnUpdateIncome(incomeUpdated, incomeBeforeUpdateDB, newCommonGoalsFromDB, goalListToUpdate);
                    computeOnCommonGoalsOnAmountIncomeUpdate(incomeUpdated, newCommonGoalsFromDB, goalListToUpdate);
                    goalRepository.saveAll(goalListToUpdate);
                }
                if (!oldCommonGoalsFromDB.isEmpty()){
                    System.out.println("*** !oldCommonGoalsFromDB.isEmpty() ***");
                    List<Goal> oldGoalList = new ArrayList<>();
                    removeIncomeAmountFromOldGoalsThenComputeOnLoop(oldCommonGoalsFromDB, incomeBeforeUpdateDB, oldGoalList);
                    goalRepository.saveAll(oldGoalList);
                }
            }

            incomeRepository.save(incomeUpdated);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeIncomeAmountFromGoalThenComputeOnLoopBasedOnDate(
            List<CommonGoal> oldCommonGoalsFromDB,
            Income incomeBeforeUpdateDB,
            Income incomeUpdated,
            List<Goal> oldGoalList) {
        for (CommonGoal commonGoal: oldCommonGoalsFromDB) {
            System.out.println("Inside Loop & removeIncomeAmountFromGoalThenComputeOnLoopBasedOnDate(...)");
            Goal oldGoal = goalRepository.findById(commonGoal.getId())
                    .orElseThrow(
                            () -> {
                                throw new RuntimeException("Cannot find old goal from DB!");
                            }
                    );
            if (oldGoal!=null && (oldGoal.getAmountAchieved()==null || oldGoal.getAmountAchieved()==0) ){
                continue;
            }else if (oldGoal!=null && (oldGoal.getAmountAchieved()!=null || oldGoal.getAmountAchieved()>=0)){
                //removeIncomeAmountFromGoalThenCompute(incomeBeforeUpdateDB, oldGoal, goalListToUpdate);
                Date goalEndDate = Date.from(oldGoal.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (incomeUpdated.getCreatedDate().compareTo(oldGoal.getDateDebut()) < 0
                        || incomeUpdated.getCreatedDate().compareTo(goalEndDate) > 0){
                    System.out.println("**** CreatedDate 3 Of Income is Changed ******************");
                    removeIncomeAmountFromGoalThenCompute(incomeBeforeUpdateDB, oldGoal, oldGoalList);
                }
            }
        }
    }

    private void removeIncomeAmountFromOldGoalsThenComputeOnLoop(List<CommonGoal> commonGoalsFromDB,
                                                             Income incomeBeforeUpdateDB, List<Goal> goalListToUpdate) {
        for (CommonGoal commonGoal: commonGoalsFromDB) {
            Goal oldGoal = goalRepository.findById(commonGoal.getId())
                    .orElseThrow(
                            () -> {
                                throw new RuntimeException("Cannot find old goal from DB!");
                            }
                    );
            if (oldGoal!=null && (oldGoal.getAmountAchieved()==null || oldGoal.getAmountAchieved()==0) ){
                continue;
            }else if (oldGoal!=null && (oldGoal.getAmountAchieved()!=null || oldGoal.getAmountAchieved()>=0)){
                removeIncomeAmountFromGoalThenCompute(incomeBeforeUpdateDB, oldGoal, goalListToUpdate);
            }
        }
    }

    private void computeNewCommonGoalsOnUpdateIncome(Income incomeUpdated,Income incomeBeforeUpdateDB,
                                                     List<CommonGoal> newCommonGoalsFromDB, List<Goal> goalListToUpdate){
        for (CommonGoal commonGoal: newCommonGoalsFromDB) {
            Goal goal = goalRepository.findById(commonGoal.getId()).orElseThrow(
                    () -> { throw new RuntimeException("Cannot find Goal from DB!"); }
            );
            System.out.println("Inside -> computeNewCommonGoalsOnUpdateIncome(...)");
            Double intervalAmountIncome = incomeUpdated.getAmount() - incomeBeforeUpdateDB.getAmount();
            if (goal!=null && (goal.getAmountAchieved()==null || goal.getAmountAchieved()==0) ){
                goal.setAmountAchieved(incomeUpdated.getAmount());
                if (goal.getAmount() > goal.getAmountAchieved()){
                    goal.setGoalAchieved(false);
                    goalListToUpdate.add(goal);
                }else if (goal.getAmount() <= goal.getAmountAchieved()){
                    goal.setGoalAchieved(true);
                    goalListToUpdate.add(goal);
                }
            }else if (goal!=null && (goal.getAmountAchieved()!=null || goal.getAmountAchieved()>=0)){
                goal.setAmountAchieved(goal.getAmountAchieved()+ intervalAmountIncome);
                if (goal.getAmount() > goal.getAmountAchieved()){
                    goal.setGoalAchieved(false);
                    goalListToUpdate.add(goal);
                }else if (goal.getAmount() <= goal.getAmountAchieved()){
                    goal.setGoalAchieved(true);
                    goalListToUpdate.add(goal);
                }
            }

        }
    }

    private void computeOnCommonGoalsOnAmountIncomeUpdate(Income incomeUpdated, List<CommonGoal> commonGoalsFromDB, List<Goal> goalList) {
        for (CommonGoal commonGoalFromDB: commonGoalsFromDB){
            Goal goal = goalRepository.findById(commonGoalFromDB.getId())
                    .orElseThrow(() -> {
                        throw new RuntimeException("Error, Cannot get Goal from DB!");
                    });
            if (goal!=null && (goal.getAmountAchieved()==null || goal.getAmountAchieved()==0) ){
                goal.setAmountAchieved(incomeUpdated.getAmount());
                if (goal.getAmount() > goal.getAmountAchieved()){
                    goal.setGoalAchieved(false);
                    goalList.add(goal);
                }else if (goal.getAmount() <= goal.getAmountAchieved()){
                    goal.setGoalAchieved(true);
                    goalList.add(goal);
                }
            }else if (goal!=null && (goal.getAmountAchieved()!=null || goal.getAmountAchieved()>=0)){
                //TODO: should check if Date changed recently is the still in Goal's range OR Not:
                if (goal.getIncomeId()==incomeUpdated.getId()){
                    goal.setAmountAchieved(goal.getAmountAchieved());
                }else if (goal.getIncomeId()==null){
                    goal.setAmountAchieved(goal.getAmountAchieved() + incomeUpdated.getAmount());
                    goal.setIncomeId(incomeUpdated.getId());
                }
                if (goal.getAmount() > goal.getAmountAchieved()){
                    goal.setGoalAchieved(false);
                    goalList.add(goal);
                }else if (goal.getAmount() <= goal.getAmountAchieved()){
                    goal.setGoalAchieved(true);
                    goalList.add(goal);
                }
            }
        }
    }

    private void eliminateNullsOnNewGoal(Goal goal){
        if (goal.getDescription()==null) goal.setDescription("");
        if (goal.getAmountAchieved()==null) goal.setAmountAchieved(0.0);
        if (goal.getGoalAchieved()==null) goal.setGoalAchieved(false);
    }

}

