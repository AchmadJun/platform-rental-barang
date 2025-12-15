package project_java_ajun.platform_rental_barang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_java_ajun.platform_rental_barang.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
