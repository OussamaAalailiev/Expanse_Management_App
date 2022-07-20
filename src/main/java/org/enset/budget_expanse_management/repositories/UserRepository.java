package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
