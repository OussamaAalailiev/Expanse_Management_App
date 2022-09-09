package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.Income;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ManagementServiceImpl implements BudgetExpanseManagementService {

    private final ExpanseRepository expanseRepository;
    private final BudgetRepository budgetRepository;
//    private final CategoryExpanseRepository categoryExpanseRepository;
//    private final IncomeRepository incomeRepository;
//    private final GoalRepository goalRepository;
//    private final CategoryIncomeRepository categoryIncomeRepository;
//
//    private final UserRepository userRepository;

    public ManagementServiceImpl(ExpanseRepository expanseRepository,
                                 BudgetRepository budgetRepository
//                                 ,CategoryExpanseRepository categoryExpanseRepository,
//                                 IncomeRepository incomeRepository,
//                                 GoalRepository goalRepository,
//                                 CategoryIncomeRepository categoryIncomeRepository,
//                                 UserRepository userRepository
    ) {
        this.expanseRepository = expanseRepository;
        this.budgetRepository = budgetRepository;
//        this.categoryExpanseRepository = categoryExpanseRepository;
//        this.incomeRepository = incomeRepository;
//        this.goalRepository = goalRepository;
//        this.categoryIncomeRepository = categoryIncomeRepository;
//        this.userRepository = userRepository;
    }

//    @Override
//    public void addExpanseToOneOrZeroBudgetService(Expanse expanse, Budget budget) {
//        if(expanse != null && budget==null){//If Expanse added doesn't belong to any Budget:
//            expanseRepository.save(expanse);
//        } else if (expanse != null && budget!=null) {//Algorithm Calculation ...
//
//
////            if (expanse.getCategoryExpanse().getCategoryExpanseType() ==
////                    budget.getCategoryExpanse().getCategoryExpanseType()){
////
////                if (expanse.getCreatedDate().compareTo(budget.getDateDebut()) >= 0 &&
////                    expanse.getCreatedDate().compareTo(budget.getEndDate()) <=0){
////
////                }
////
////            }
//            List<Budget> budgets = new ArrayList<>();
//            //budgets = budgetRepository.
//        } else if (expanse==null) {
//            throw new RuntimeException("Expanse cannot be null !");
//        }
//
//    }

//    @Override
//    public void addExpanseToBudgetServiceInit() {
//        List<Expanse> expanseList = expanseRepository.findAll();
//        List<Budget> budgetList = budgetRepository.findAll();
//
//            for (int i = 0; i < expanseList.size(); i++) {
//                if ((i+1) % 2!=0){
//                    expanseList.get(i).setBudget(budgetList.get(1));
//            }
//        }
//
//    }

  /*
    public void getAllExpAndBudWithSameUserDateAndCatExpService(){

        List<Object> objectList = expanseRepository
                .getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp(20);

//        for (int i = 0; i < objectList.size(); i++) {
//            if (objectList.get(i) instanceof Expanse){
//                Expanse expanse = new Expanse(((Expanse) objectList.get(i)).getId(),
//                        ((Expanse) objectList.get(i)).getAmount(), ((Expanse) objectList.get(i)).getTitle(),
//                        ((Expanse) objectList.get(i)).getCreatedDate());
//                expanse.setUser(((Expanse) objectList.get(i)).getUser());
//                System.out.println("Expanse: " +expanse.getId()+" " + expanse.getAmount() +" " + expanse.getTitle()
//                        + " " + expanse.getCreatedDate() + " " + expanse.getUser().getId());
//                System.out.println("---------------------------------------");
//            } else if (objectList.get(i) instanceof Budget) {
//                Budget budget = new Budget(((Budget) objectList.get(i)).getId(),
//                        ((Budget) objectList.get(i)).getTitle(), ((Budget) objectList.get(i)).getDescription(),
//                        ((Budget) objectList.get(i)).getDateDebut(),((Budget) objectList.get(i)).getEndDate() ,
//                        ((Budget) objectList.get(i)).getAmount());
//
//                budget.setUser(((Budget) objectList.get(i)).getUser());
//                System.out.println("Budget: " +budget.getId()+" " + budget.getAmount() +" " + budget.getTitle()
//                        + " " + budget.getDateDebut() +" "+ budget.getEndDate()+" " + budget.getUser().getId());
//            }
//        }
    }
   */

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

//    @Override
//    public void checkIfBudgetIsRespectedOnAddExpanse(Expanse expanse) {
//        //Algorithm To check If a Budget is respected On Add an Expanse,
//        // by getting from Exp 'user.id' + 'amount' + 'CategoryExpanse' + 'createdDate':
////        List<Budget> allBudgetList = budgetRepository.findAll();
//        List<Budget> allBudgetList = budgetRepository.findAll();
//        if (!allBudgetList.isEmpty()){
//
//            for (Budget budget : allBudgetList) {
//                Double totalAmountSpent;
//                Double amountRemainsCalculated;
//
//               // Boolean isUserIsNotNull = expanse.getUser()!=null && budget.getUser()!=null;
//                Boolean sameUserId = (expanse.getUser().getId() == budget.getUser().getId());
//                Boolean sameCategoryExpanse = (expanse.getCategoryExpanse().getId().equals(budget.getId()));
//                Boolean betweenSameDate = (expanse.getCreatedDate().compareTo(budget.getDateDebut()) == 0)
//                        || (expanse.getCreatedDate().compareTo(Date.valueOf(budget.getEndDate())) == 0)
//                        && (expanse.getCreatedDate().compareTo(Date.valueOf(budget.getEndDate())) <= 0);
//                Boolean isBudgetAmountSpentStillLess = budget.getAmountSpent() < budget.getAmount();
//
//                if (sameUserId && sameCategoryExpanse && betweenSameDate
//                        && isBudgetAmountSpentStillLess ) {
//                    totalAmountSpent = budget.getAmountSpent() + budget.getAmount();
//                    amountRemainsCalculated = budget.getAmountRemains() - expanse.getAmount();
//                    budget.setAmountSpent(totalAmountSpent);
//                    budget.setAmountRemains(amountRemainsCalculated);
//                    budgetRepository.save(budget);
//                }
//            }
//        }
//        //return ;
//    }



//    @Override
//    public Budget checkIfBudgetIsRespectedByCalculation() {
//        List<ResultDTOExpansesBudgets> expansesBudgets = budgetRepository
//                .getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp3();
//        Budget budget = new Budget();
//        Double totalExpansesByBudgetSpent=0.0;
//        int i=0;
//        //for (int i = 0; i < expansesBudgets.size(); i++) {
//            do {
//                if (expansesBudgets.get(i).getAmountSpent() < expansesBudgets.get(i).getAmountRemains()){
//                    totalExpansesByBudgetSpent = totalExpansesByBudgetSpent
//                            + expansesBudgets.get(i).getAmountExpanse() + expansesBudgets.get(i).getAmountSpent();
//                    budget = budgetRepository.findById(expansesBudgets.get(i).getIdBudget()).get();
//                    budget.setAmountSpent(totalExpansesByBudgetSpent);
//                }
//                i++;
//            }while (totalExpansesByBudgetSpent==0.0);
//        //}
//        budgetRepository.save(budget);
//        return budget;
//    }


//    @Override
//    public void checkIfBudgetIsRespectedByCalculation(){
//        List<ResultDTOExpansesBudgets> expansesBudgets = budgetRepository
//                .getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp3();
//        Budget budgetFromDB = new Budget();
//        Double totalExpansesByBudgetSpent=0.0;
//        //int i=0;
//        for (int i = 0; i < expansesBudgets.size()
//                //&& budgetRepository.findById(expansesBudgets.get(i).getIdBudget()).isPresent()
//                ; i++) {
//        //do {
//            Integer budgetId;
//            //budget = budgetRepository.findById(expansesBudgets.get(0).getIdBudget()).get();
//            if (expansesBudgets.get(i).getAmountSpent() == null){
//                totalExpansesByBudgetSpent = totalExpansesByBudgetSpent
//                        + expansesBudgets.get(i).getAmountExpanse();
//                budgetFromDB.setAmountSpent(totalExpansesByBudgetSpent);
//                System.out.println("------------------------");
//                System.out.println("If amountSpent is Null: "+ totalExpansesByBudgetSpent);
//                budgetId = expansesBudgets.get(i).getIdBudget();
//                budgetFromDB = budgetRepository.findById(budgetId).get();
//                budgetRepository.save(budgetFromDB);
//            }
//            else if (expansesBudgets.get(i).getAmountSpent() < expansesBudgets.get(i).getAmountRemains() ){
//                totalExpansesByBudgetSpent = totalExpansesByBudgetSpent
//                        + expansesBudgets.get(i).getAmountExpanse() + expansesBudgets.get(i).getAmountSpent();
//                System.out.println("------------------------");
//                System.out.println("If amountSpent is Not Null: "+ totalExpansesByBudgetSpent);
//                budgetId = expansesBudgets.get(i).getIdBudget();
//                budgetFromDB = budgetRepository.findById(budgetId).get();
//                budgetFromDB.setAmountSpent(totalExpansesByBudgetSpent);
//                budgetRepository.save(budgetFromDB);
//            }
//          //  i++;
//        //}while (totalExpansesByBudgetSpent==0.0);
//        }
//    }

//    @Override
//    public void checkIfBudgetIsRespectedByCalculationSumAmountExp() {
//        List<ResultDTOExpansesBudgets> expansesBudgets =
//                budgetRepository.getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp3();
//        List<Budget> budgetList = new ArrayList<>();
//        Double totalAmountSpentByBudget = 0.0;
//        Double totalAmountRemainsByBudget = 0.0;
//        for (int i = 0; i < expansesBudgets.size(); i++) {
//            Integer budgetId= expansesBudgets.get(i).getIdBudget();
//            Double amountSpent = expansesBudgets.get(i).getAmountSpent();
//            Double amountRemains = expansesBudgets.get(i).getAmountRemains();
//            if (amountSpent==null){
//                Budget budget = budgetRepository.findById(budgetId).get();
//                totalAmountSpentByBudget = expansesBudgets.get(i).getAmountExpanseSum();
//                totalAmountRemainsByBudget = expansesBudgets.get(i).getAmountRemains() - totalAmountSpentByBudget;
//                budget.setAmountSpent(totalAmountSpentByBudget);
//                budget.setAmountRemains(totalAmountRemainsByBudget);
//                budgetRepository.save(budget);
//            }
//           else if ((amountSpent < amountRemains) && amountSpent!=null){ // '|| amountSpent!=null' may be the right thing
//                Budget budget = budgetRepository.findById(budgetId).get();//    instead of '&& amountSpent!=null':
//                totalAmountSpentByBudget = expansesBudgets.get(i).getAmountExpanseSum()
//                        + expansesBudgets.get(i).getAmountSpent();
//                totalAmountRemainsByBudget = budget.getAmountRemains() - totalAmountSpentByBudget;
//                budget.setAmountSpent(totalAmountSpentByBudget);
//                budget.setAmountRemains(totalAmountRemainsByBudget);
//                budgetRepository.save(budget);
//            } else if ((amountSpent > amountRemains) && amountSpent!=null) {
//                Budget budget = budgetRepository.findById(budgetId).get();
//               totalAmountSpentByBudget = amountSpent + (expansesBudgets.get(i).getAmountExpanse()
//                                             - amountSpent);
//               totalAmountRemainsByBudget = amountRemains - (expansesBudgets.get(i).getAmountExpanse()
//                       - amountSpent);
//               budget.setAmountSpent(totalAmountSpentByBudget);
//               budget.setAmountRemains(totalAmountRemainsByBudget);
//               budgetRepository.save(budget);
//            }
//            //budgetList.add(budget);
//        }
//
//    }

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

    // TODO: Incomplete Algorithm to compute Goals On add a new Income.
    @Override
    public void calculateGoalsOnAddIncomeService(Income income) {

    }

}

