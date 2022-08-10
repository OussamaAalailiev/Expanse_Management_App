package org.enset.budget_expanse_management.repositories;

import org.enset.budget_expanse_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:4090")//Means that only pages
//// from this Website are allowed to request resources from This App(Backend), it could be integrated by Spring Security.
public interface UserRepository extends JpaRepository<User, UUID> {
}
