package ca.mcmaster.cas735.acmepark.permit.port;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserDataRepo extends JpaRepository<User, Integer>{
    Optional<User> findByUserId(int userId);
}
