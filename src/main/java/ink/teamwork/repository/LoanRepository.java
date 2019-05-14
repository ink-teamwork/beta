package ink.teamwork.repository;

import ink.teamwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
