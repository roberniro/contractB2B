package civilCapstone.contractB2B.user.repository;

import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername (String username);
    Boolean existsByUsername(String username);

    @Override
    Optional<User> findById(Long id);
}
