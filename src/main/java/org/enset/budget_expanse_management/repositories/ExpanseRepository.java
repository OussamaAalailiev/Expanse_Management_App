package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Expanse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Transactional
public interface ExpanseRepository extends JpaRepository<Expanse, Long> {

    @Query("SELECT e, b FROM Expanse e, Budget b WHERE e.user.id=b.user.id " +
            "AND (e.categoryExpanse.id=:x AND b.categoryExpanse.id=:x) " +
            "AND (e.createdDate BETWEEN b.dateDebut AND b.endDate)")
   public List<Object> getAllExpansesAndBudgetsWithSameUserDateAndCategoryExp(@Param("x") Integer categoryExpanseId);
}
