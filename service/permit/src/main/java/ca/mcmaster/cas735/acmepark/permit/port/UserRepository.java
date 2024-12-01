package ca.mcmaster.cas735.acmepark.permit.port;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
}
