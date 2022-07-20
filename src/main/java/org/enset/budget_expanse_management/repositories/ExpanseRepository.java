package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.Expanse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpanseRepository extends JpaRepository<Expanse, Long> {
}
