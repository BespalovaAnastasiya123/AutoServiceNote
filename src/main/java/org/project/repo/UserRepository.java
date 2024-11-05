package org.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.project.models.User;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
