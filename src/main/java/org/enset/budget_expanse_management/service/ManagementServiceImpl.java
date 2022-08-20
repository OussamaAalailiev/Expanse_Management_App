package org.enset.budget_expanse_management.service;

import org.enset.budget_expanse_management.mapping.ResultDTOExpansesBudgets;
import org.enset.budget_expanse_management.model.Budget;
import org.enset.budget_expanse_management.model.CategoryExpanse;
import org.enset.budget_expanse_management.model.Expanse;
import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.BudgetRepository;
import org.enset.budget_expanse_management.repositories.CategoryExpanseRepository;
import org.enset.budget_expanse_management.repositories.ExpanseRepository;
import org.enset.budget_expanse_management.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ManagementServiceImpl implements BudgetExpanseManagementService {

    private final ExpanseRepository expanseRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryExpanseRepository categoryExpanseRepository;

    private final UserRepository userRepository;

    public ManagementServiceImpl(ExpanseRepository expanseRepository,
                                 BudgetRepository budgetRepository,
                                 CategoryExpanseRepository categoryExpanseRepository,
                                 UserRepository userRepository) {
        this.expanseRepository = expanseRepository;
        this.budgetRepository = budgetRepository;
        this.categoryExpanseRepository = categoryExpanseRepository;
        this.userRepository = userRepository;
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

    //User can update Only the 'amount' of Expanse, but if user want to update
    // other fields he/she better delete the expanse and create a new one expanse:
    /** Function is completed! */
    @Override
    public void calculateBudgetsOnUpdateExpanseService(Expanse expanse) {
        List<Budget> ListBudgetsToUpdate=new ArrayList<>();
        List<ResultDTOExpansesBudgets> expanseBudgetsDTO = expanseRepository
                .onAddOrUpdateExpanseComputeOnCommonBudgets(expanse.getCategoryExpanse().getId(),
                        expanse.getId());
        //In case for Expanse Update, before update, I'll get the Old amountExp from DB
        // THEN I'll do a comparison between the old value and new value entered to be updated:
        // Then I'll UPDATE the Expanse by re-save it:
        if (!expanseBudgetsDTO.isEmpty()){

            if (expanseRepository.findById(expanse.getId()).isPresent()){
//                //Before Expanse Update Algorithm Calculation:
            for (int i = 0; i < expanseBudgetsDTO.size(); i++) {
                Integer budgetId= expanseBudgetsDTO.get(i).getIdBudget();
                Double amountSpent = expanseBudgetsDTO.get(i).getAmountSpent();
                Double amountRemains = expanseBudgetsDTO.get(i).getAmountRemains();
                Double oldAmountExpanseFromDB = expanseBudgetsDTO.get(i).getAmountExpanse();
                Double amountExpanseInterval = 0.0;
                if (expanse.getAmount() < oldAmountExpanseFromDB && amountSpent !=null){//In case Of update with Less new amountExp than the Old one:
                    Budget budget = budgetRepository.findById(budgetId).get();
                    amountExpanseInterval = expanse.getAmount() - oldAmountExpanseFromDB;// -(someNumber) -> means less amountExpanse
                    budget.setAmountSpent(budget.getAmountSpent() + amountExpanseInterval);
                    budget.setAmountRemains(budget.getAmountRemains() - amountExpanseInterval);//Always add amountExpanseInterval
                    // (amRemains-(-amExp)) => (amRemains + amExp))
                    ListBudgetsToUpdate.add(budget);
                    //budgetRepository.save(budget);
                    expanseRepository.save(expanse);
                } else if (expanse.getAmount() > oldAmountExpanseFromDB && amountSpent !=null) {//In case Of update with new amountExp Greater than the Old one:
                    Budget budget = budgetRepository.findById(budgetId).get();
                    amountExpanseInterval = expanse.getAmount() - oldAmountExpanseFromDB;// Inverse Calc In comparison of the above interval:
                    budget.setAmountSpent(budget.getAmountSpent() + amountExpanseInterval);//(-(-)) => (+())
                    budget.setAmountRemains(budget.getAmountRemains() - amountExpanseInterval);
                    ListBudgetsToUpdate.add(budget);
                    //budgetRepository.save(budget);
                    expanseRepository.save(expanse);
                } else if (amountSpent == null) {
                    Budget budget = budgetRepository.findById(budgetId).get();
                    budget.setAmountSpent(expanse.getAmount());
                    ListBudgetsToUpdate.add(budget);
                    //budgetRepository.save(budget);
                    expanseRepository.save(expanse);
                }
            }
                budgetRepository.saveAll(ListBudgetsToUpdate);
          }

        }else {//In case there is NO common Budgets, we only save the expanse:
            expanseRepository.save(expanse);
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
                .onAddOrUpdateExpanseComputeOnCommonBudgets(expanse.getCategoryExpanse().getId(),
                        expanse.getId());
        if (expanseBudgetsDTO.get(0).getIdBudget()!=null ){
            if (expanseRepository.findById(expanse.getId()).isPresent()){
                for (int i = 0; i < expanseBudgetsDTO.size()
                    //  && expanseRepository.findById(expanse.getId()).isEmpty()
                        ; i++) {
                    Integer budgetId= expanseBudgetsDTO.get(i).getIdBudget();
                    Double amountSpent = expanseBudgetsDTO.get(i).getAmountSpent();
                    Double amountRemains = expanseBudgetsDTO.get(i).getAmountRemains();
                    Double amountExpanse = expanseBudgetsDTO.get(i).getAmountExpanse();
                    if (amountSpent==null){
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
        
    }


    /** Function 'updateBudgetService(..)' is completed! */
    @Override//In case a user adds a new Budget:
    public void calculateExpansesOnAddBudgetService(Budget budget) {
        /**BEFORE Saving a new Budget: */
        /**Test data commented down below: */
//        budget.setTitle("Internet Subscription");
//        budget.setAmountSpent(0.0);
//        budget.setAmount(200.00); budget.setDateDebut(new Date(2022, Calendar.AUGUST,18));
//        budget.setAmountRemains(budget.getAmount());//This will always apply on add new Budget.
//        CategoryExpanse categoryExpanse = categoryExpanseRepository.findById(56).get();
//        User user = userRepository
//                .findById(UUID.fromString("653eb6f2-a817-4184-af31-4cff631692f8")).get();
//        budget.setCategoryExpanse(categoryExpanse);
//        budget.setUser(user);
        budget.setAmountRemains(budget.getAmount());
        Budget newSavedBudgetToDB = budgetRepository.save(budget);
        /**AFTER Saving a new Budget to DB: */
        List<ResultDTOExpansesBudgets> expansesBudgets = budgetRepository.onAddBudgetComputeOnCommonExpanses
                (newSavedBudgetToDB.getId(), newSavedBudgetToDB.getCategoryExpanse().getId());
        if (expansesBudgets.get(0).getId()==null){//If the budget have no common expanses, means the List above will be empty then we save the budget without calculation:
            budgetRepository.save(budget);
        } else{
            //Do some Calculation...
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

//    @Override
//    public void checkIfBudgetIsRespectedOnAddExpanse2(Expanse expanse, Budget budget) {
//        if (expanse!=null && budget==null){//Case 1: user add an Expanse that doesn't belong to any Budget.
//            //Do some data validations:
//            expanseRepository.save(expanse);
//        }
//        if (expanse!=null && budget!=null){//Case 2: user add an Expanse that belongs to a Budget chosen from a list.
//            //Do some data validations:
//            expanse.setBudget(budget);
//            expanseRepository.save(expanse);
//            budgetRepository.save(budget);
//
//        }
//    }

//    @Override
//    public List<Budget> getAllBudgetsFromDBService() {
//        return budgetRepository.findAll();
//    }


}

